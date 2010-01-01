package com.goodworkalan.paste;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.deviate.RuleMap;
import com.goodworkalan.dovetail.GlobTree;
import com.goodworkalan.dovetail.Globber;
import com.goodworkalan.dovetail.Match;
import com.goodworkalan.paste.intercept.InterceptingRequest;
import com.goodworkalan.paste.intercept.InterceptingResponse;
import com.goodworkalan.paste.intercept.Interception;
import com.goodworkalan.paste.janitor.Janitor;
import com.goodworkalan.paste.redirect.Redirection;
import com.goodworkalan.paste.redirect.Redirector;
import com.goodworkalan.paste.stop.Abnormality;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import com.mallardsoft.tuple.Pair;
import com.mallardsoft.tuple.Tuple;

/**
 * An object internal to the Paste filter that implements the filtration. The
 * Paste filter constructs a Paste guicer during its filter initialization. The
 * Paset filter then delegates all of its calls to the Paste guicer.
 * <p>
 * This separation is simply so that I can make final those properties of the
 * filter that do not change for the lifetime of the filter. This is not
 * possible in the Paste filter because of the two stage initialization of of
 * Java Servlet filters. Having all these properties made variable so they could
 * be set during initialization would only make me nervous, while it is pretty
 * easy to look at the entirety of the Paste filter to see that the Paste filter
 * is only set once, scanning the entirety of the filter to determine what
 * really is and is not variable would be difficult.
 * 
 * @author Alan Gutierrez
 */
public class PasteGuicer {
    /** The Guice injector. */
    private final Injector injector;

    /**
     * A list of Dovetail URL bindings that map a path to a Deviate rule set
     * that further winnows the matches based on request parameters.
     */
    private final List<GlobTree<RuleMap<Pair<Integer, Class<?>>>>> controllerBindings;

    /**
     * The rule map used to select a renderer for a given controller or thrown
     * exception.
     */
    private final RuleMap<Pair<Integer, RenderModule>> viewRuleMap;

    /** The list of janitors to run when the filter is shutdown. */
    private final LinkedBlockingQueue<Janitor> janitors = new LinkedBlockingQueue<Janitor>();

    /** A stack of filter invocations. */
    private static final ThreadLocal<LinkedList<Filtration>> filtrations = new ThreadLocal<LinkedList<Filtration>>();
    
    /** The map of annotations to controllers. */
    private final Map<Class<?>, List<Class<?>>> reactions;
    
    /**
     * Create a Paste guicer from using the given filter servlet context and the
     * given Paste filter initialization parameters.
     * 
     * @param servletContext
     *            The Paste filter servlet context.
     * @param initialization
     *            The Paste filter initialization parameters.
     */
    public PasteGuicer(ServletContext servletContext, Map<String, String> initialization) {
        List<Module> modules = new ArrayList<Module>();
        if (initialization.containsKey("Modules")) {
            try {
                for (String module : initialization.get("Modules").split(",")) {
                    Class<?> moduleClass = Class.forName(module.trim());
                    modules.add((Module) moduleClass.newInstance());
                }
            } catch (Exception e) {
                throw new PasteException(e);
            }
        }

        List<Router> routers = new ArrayList<Router>();
        if (initialization.containsKey("Routers")) {
            try {
                for (String router : initialization.get("Routers").split(",")) {
                    Class<?> routerClass = Class.forName(router.trim());
                    routers.add((Router) routerClass.newInstance());
                }
            } catch (Exception e) {
                throw new PasteException(e);
            }
        }
        
        CoreConnector connector = new CoreConnector();
        for (Router router : routers) {
            router.connect(connector);
        }
        
        modules.add(new PasteModule(servletContext, connector.getRoutes(), initialization, janitors, new Reactor() {
            public <T> void react(T object) {
                PasteGuicer.this.react(object);
            }
        }));

        this.injector = Guice.createInjector(modules);
        this.controllerBindings = connector.getBindingTrees();
        this.viewRuleMap = connector.getViewRules();
        this.reactions = connector.getReactions();
    }

    /**
     * Get the properties of the root invocation of the filter.
     * 
     * @return The filtration structure for the root invocation.
     */
    static Filtration getReactionFiltration() {
        return getFiltrations().getFirst();
    }

    /**
     * Get the properties of the current invocation of the filter.
     * 
     * @return The filtration structure for the current invocation.
     */
    static Filtration getFilterFiltration() {
        return getFiltrations().getLast();
    }
    
    public void start() {
        react(new Startup());
    }
    
    private <T> void react(final T object) {
        Injector childInjector = injector.createChildInjector(new Module() {
            @SuppressWarnings("unchecked")
            public void configure(Binder binder) {
                binder.bind((Class<T>) object.getClass())
                      .annotatedWith(Reaction.class)
                      .toInstance(object);
            }
        });
        LinkedList<Filtration> filtrations = getFiltrations();
        for (Filtration filtration : filtrations) { 
            filtration.setSubsequent();
        }
        Filtration filtration = new Filtration(null, null);
        filtrations.addLast(filtration);
        try {
            if (reactions.containsKey(object.getClass())) {
                for (Class<?> reaction : reactions.get(object.getClass())) {
                    Object child = childInjector.getInstance(reaction);
                    if (child instanceof Runnable) {
                        ((Runnable) child).run();
                    }
                }
            }
        } finally {
            filtrations.removeLast();
            cleanUp(filtration.getJanitors());
        }
    }

    /**
     * Filter the given request and the given response, possibly forwarding them
     * to the given filter chain.
     * 
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     * @param chain
     *            The filter chain.
     * @throws IOException
     *             For any I/O error.
     * @throws ServletException
     *             For any other error.
     */
    public void filter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        Interception interception = new Interception();
        filter(new InterceptingRequest(interception, request),
               new InterceptingResponse(interception, response), interception,
               chain);
    }

    /**
     * Get the linked list of filtrations for this thread.
     * 
     * @return The linked list of filtrations.
     */
    private static LinkedList<Filtration> getFiltrations() {
        LinkedList<Filtration> filtration = filtrations.get();
        if (filtration == null) {
            filtrations.set(new LinkedList<Filtration>());
            return getFiltrations();
        }
        return filtration;
    }

    /**
     * Wrapper around the heart of the filter that wraps filter processing in a
     * try/finally block that pops the filtration context stack and runs and
     * janitors. If the interception flag is flipped by one of the controllers,
     * then the request is not forwarded to the filter chain.
     * 
     * @param request
     *            The interception detecting request wrapper.
     * @param response
     *            The interception detecting response wrapper.
     * @param interception
     *            The interception flag.
     * @param chain
     *            The filter chain.
     * @throws IOException
     *             For any I/O exception.
     * @throws ServletException
     *             For any other exception.
     */
    private void filter(InterceptingRequest request,
                        InterceptingResponse response,
                        Interception interception,
                        FilterChain chain)
    throws IOException, ServletException {
        LinkedList<Filtration> filtrations = getFiltrations();
        for (Filtration filtration : filtrations) { 
            filtration.setSubsequent();
        }
        Filtration filtration = new Filtration(request, response);
        filtrations.addLast(filtration);
        try {
            filter(filtration, interception, chain);
        } finally {
            filtrations.removeLast();
            cleanUp(filtration.getJanitors());
        }
    }

    /**
     * The heart of the filter.
     * 
     * @param filtration
     *            The filtration structure for the current invocation of the
     *            filter.
     * @param interception
     *            A flag to indicate if any of controllers have short-circuited
     *            the filter chain by sending a response.
     * @param chain
     *            The subsequent filter chain.
     * @throws IOException
     *             For any I/O exception.
     * @throws ServletException
     *             For any other exception.
     */
    private void filter(Filtration filtration, Interception interception, FilterChain chain)
    throws IOException, ServletException {
        // Use Guice to generate the criteria for this invocation, which will
        // in turn provide us with a path we can match.
        Criteria criteria = injector.getInstance(Key.get(Criteria.class, Filter.class));

        Throwable throwable = null;
        Object controller = null;

        // We try each series of binding definitions in order. There can be
        // multiple bindings that match, applying multiple controllers.
        CONTROLLERS: for (GlobTree<RuleMap<Pair<Integer, Class<?>>>> tree : controllerBindings) {
            // If a controller has written a response, we're done.
            if (interception.isIntercepted()) {
                break;
            }
            
            // Create a globber that will apply tests created by the Guice
            // injector or this Paste filter.
            Globber<RuleMap<Pair<Integer,Class<?>>>>globber = tree.newGlobber(new GuiceMatchTestFactory(injector));

            // Attempt to match the path.
            List<Match<RuleMap<Pair<Integer, Class<?>>>>> matches = globber.map(criteria.getPath());

            // We can have multiple matches, so we winnow them down by futher
            // matching request parameters, then futher winnowing them by
            // choosing a controller based on priority.
            if (!matches.isEmpty()) {
                int found = 0;
                int highestPriority = Integer.MIN_VALUE;
                Class<?> controllerClass = null;
                Map<String, String> mappings = null;

                // Apply the rule set associated with each matched glob.
                for (Match<RuleMap<Pair<Integer, Class<?>>>> mapping : matches) {
                    List<Pair<Integer, Class<?>>> bindings = mapping.getObject()
                        .test()
                            .put(BindKey.METHOD, filtration.getRequest().getMethod())
                            .put(BindKey.PATH, criteria.getPath())
                            .get();
                    for (Pair<Integer, Class<?>> binding : bindings) {
                        int priority = Tuple.get1(binding);
                        if (priority > highestPriority) {
                            found = 1;
                            highestPriority = priority;
                            controllerClass = Tuple.get2(binding);
                            mappings = mapping.getParameters();
                        } else if (priority == highestPriority) {
                            found++;
                        }
                    }
                }

                if (found > 1) {
                    // If we've found multiple controllers that have the same
                    // priority, then we raise an exception.
                    throw new PasteException(0);
                } if (found == 1) {
                    // Clear the controller scope, null the controller.
                    filtration.getControllerScope().clear();
                    controller = null;

                    // We cheat a little; instead of creating a provider, we're
                    // going to just tuck the mappings into the controller scope.
                    filtration.getControllerScope().put(Key.get(new TypeLiteral<Map<String, String>>(){ }, Controller.class), mappings);

                    try {
                        // Try to build the controller.
                        controller = injector.getInstance(controllerClass);
                        // We cheat a little more; tucking the constructed
                        // controller right into the scope of the controller.
                        filtration.getControllerScope().put(Key.get(Object.class, Controller.class), controller);
                    } catch (ProvisionException e) {
                        if (e.getCause() != null) {
                            // FIXME Test this branch.
                            // If the controller threw an exception, we'll try to
                            // render it.
                            throwable = e.getCause();
                        } else {
                            // Otherwise, this is a big, bad server error.
                            throw e;
                        }
                    }
    
                    if (throwable != null) {
                        // Go to rendering.
                        break;
                    }

                    // Invoke any actors upon the controller.
                    Actors actors = controller.getClass().getAnnotation(Actors.class);
                    if (actors != null) {
                        for (Class<? extends Actor> actor : actors.value()) {
                            throwable = injector.getInstance(actor).actUpon(controller);
                            if (throwable != null) {
                                // Go to rendering.
                                break CONTROLLERS;
                            }
                        }
                    }
                }
            }
        }

        // Exceptions are also used for those things in HTTP that feel like
        // an abrubt change of course.
        if (throwable instanceof Redirection) {
            // FIXME Who says that this actually gets rendered as a redirect?
            injector.getInstance(Redirector.class).redirect(((Redirection) throwable).getWhere());
        } else if (throwable instanceof Abnormality) {
            // Set the response status.
            injector.getInstance(Response.class).setStatus(((Abnormality) throwable).getStatus());
        }

        if (controller != null || throwable != null) {
            // Get a list of render modules whose rules match the current
            // request values and the current controller or exception.
            List<Pair<Integer, RenderModule>> views = viewRuleMap
                .test()
                    .put(BindKey.PACKAGE, controller == null ? null : controller.getClass().getPackage().getName())
                    .put(BindKey.CONTROLLER_CLASS, controller)
                    .put(BindKey.PATH, criteria.getPath())
                    .put(BindKey.STATUS, injector.getInstance(Response.class).getStatus())
                    .put(BindKey.EXCEPTION_CLASS, throwable)
                    .put(BindKey.METHOD, filtration.getRequest().getMethod())
                    .get();

            // Find the render module with the highest priority.
            RenderModule renderModule = null;
            int hightestPriority = Integer.MIN_VALUE;
            int found = 0;
            for (Pair<Integer, RenderModule> view : views) {
                int priority = Tuple.get1(view);
                if (priority > hightestPriority) {
                    found = 1;
                    hightestPriority = priority;
                    renderModule = Tuple.get2(view);
                } else if (priority == hightestPriority) {
                    found++;
                }
            }

            if (found > 1) {
                // Renderer is ambiguous.
                throw new PasteException();
            } else if (found == 1) {
                // Render output.
                injector.createChildInjector(renderModule).getInstance(Renderer.class).render();
            } else if (throwable != null) {
                // No renderer for our exception, to turn it into a big, bad 500.
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException) throwable;
                } else if (throwable instanceof Error) {
                    throw (Error) throwable;
                }
                throw new ServletException(throwable);
            }
        }

        // Down the filter chain, unless we've just sent a response.
        if (!interception.isIntercepted()) {
            chain.doFilter(filtration.getRequest(), filtration.getResponse());
        }
    }

    /**
     * Invoke the clean up method on each janitor in the given list of janitors.
     * 
     * @param janitors
     *            The collection of janitors to clean up.
     */
    private void cleanUp(Collection<Janitor> janitors) {
        for (Janitor janitor : janitors) {
            try {
                janitor.cleanUp();
            } catch (ThreadDeath t) {
                throw t;
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Cleanup the filter by invoking the application wide jantiors.
     */
    public void destroy() {
        cleanUp(janitors);
    }
}
