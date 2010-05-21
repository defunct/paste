package com.goodworkalan.paste.servlet;

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
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.goodworkalan.deviate.RuleMap;
import com.goodworkalan.dovetail.GlobTree;
import com.goodworkalan.dovetail.Globber;
import com.goodworkalan.dovetail.Match;
import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.ilk.inject.InjectException;
import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.paste.connector.Connector;
import com.goodworkalan.paste.connector.Router;
import com.goodworkalan.paste.controller.Abnormality;
import com.goodworkalan.paste.controller.Actors;
import com.goodworkalan.paste.controller.Criteria;
import com.goodworkalan.paste.controller.InitializationParameters;
import com.goodworkalan.paste.controller.Janitor;
import com.goodworkalan.paste.controller.JanitorQueue;
import com.goodworkalan.paste.controller.Parameters;
import com.goodworkalan.paste.controller.PasteException;
import com.goodworkalan.paste.controller.Reactor;
import com.goodworkalan.paste.controller.Redirection;
import com.goodworkalan.paste.controller.Renderer;
import com.goodworkalan.paste.controller.Response;
import com.goodworkalan.paste.controller.Routes;
import com.goodworkalan.paste.controller.Startup;
import com.goodworkalan.paste.controller.qualifiers.Application;
import com.goodworkalan.paste.controller.qualifiers.Controller;
import com.goodworkalan.paste.controller.qualifiers.Filter;
import com.goodworkalan.paste.controller.qualifiers.Reaction;
import com.goodworkalan.paste.controller.qualifiers.Request;
import com.goodworkalan.paste.controller.scopes.ApplicationScoped;
import com.goodworkalan.paste.controller.scopes.ControllerScoped;
import com.goodworkalan.paste.controller.scopes.FilterScoped;
import com.goodworkalan.paste.controller.scopes.ReactionScoped;
import com.goodworkalan.paste.controller.scopes.RequestScoped;
import com.goodworkalan.paste.controller.scopes.SessionScoped;
import com.goodworkalan.paste.providers.ControllerProvider;
import com.goodworkalan.paste.providers.EnumeratedParametersProvider;
import com.goodworkalan.paste.providers.RequestParametersProvider;
import com.goodworkalan.paste.redirect.Redirector;
import com.goodworkalan.reflective.Reflective;
import com.goodworkalan.reflective.ReflectiveException;
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
class Responder implements Reactor {
    /** The dependency injector. */
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
    private final RuleMap<Pair<Integer, List<InjectorBuilder>>> viewRuleMap;

    /** The list of janitors to run when the filter is shutdown. */
    private final LinkedBlockingQueue<Janitor> janitors = new LinkedBlockingQueue<Janitor>();

    /** The map of annotations to controllers. */
    private final Map<Class<?>, List<Class<?>>> reactions;
    
    // TODO Document.
    private final ThreadLocal<LinkedList<Injector>> INJECTORS = new ThreadLocal<LinkedList<Injector>>() {
         protected java.util.LinkedList<Injector> initialValue() {
             return new LinkedList<Injector>();
         }
    };
    
    /**
     * Create a Paste guicer from using the given filter servlet context and the
     * given Paste filter initialization parameters.
     * 
     * @param servletContext
     *            The Paste filter servlet context.
     * @param initialization
     *            The Paste filter initialization parameters.
     */
    public Responder(final ServletContext servletContext, final Map<String, String> initialization) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        

        List<InjectorBuilder> modules = new ArrayList<InjectorBuilder>();
        if (initialization.containsKey("Modules")) {
            for (String className : initialization.get("Modules").split(",")) {
                Class<?> moduleClass;
                try {
                    moduleClass = classLoader.loadClass(className);
                } catch (ClassNotFoundException e) {
                    throw new PasteException(0, e);
                }
                final Class<? extends InjectorBuilder> injectorBuilderClass = moduleClass.asSubclass(InjectorBuilder.class);
                InjectorBuilder module;
                try {
                    try {
                        module = injectorBuilderClass.newInstance();
                    } catch (Throwable e) {
                        throw new ReflectiveException(Reflective.encode(e), e);
                    }
                } catch (ReflectiveException e) {
                    throw new PasteException(0, e);
                }
                modules.add(module);
            }
        }

        List<Router> routers = new ArrayList<Router>();
        if (initialization.containsKey("Routers")) {
            try {
                for (String className : initialization.get("Routers").split(",")) {
                    Class<?> moduleClass;
                    try {
                        moduleClass = classLoader.loadClass(className);
                    } catch (ClassNotFoundException e) {
                        throw new PasteException(0, e);
                    }
                    Class<? extends Router> routerClass = moduleClass.asSubclass(Router.class);
                    routers.add(routerClass.newInstance());
                }
            } catch (Exception e) {
                throw new PasteException(0, e);
            }
        }
        
        final Cassette cassette = new Cassette();
        Connector connector = new Connector(cassette);
        for (Router router : routers) {
            router.connect(connector);
        }
        
        InjectorBuilder newInjector = new InjectorBuilder();
        newInjector.module(new InjectorBuilder() {
            protected void build() {
                scope(ApplicationScoped.class);
                instance(Responder.this, ilk(Reactor.class), null);
                instance(initialization, new Ilk<Map<String, String>>() { }, InitializationParameters.class);
                instance(servletContext, ilk(ServletContext.class), null);
                instance(new JanitorQueue(janitors), ilk(JanitorQueue.class), Application.class);
                instance(cassette.getRoutes(), ilk(Routes.class), null);
            }
        });

        for (InjectorBuilder module : modules) {
            newInjector.module(module);
        }

        this.injector = newInjector.newInjector();
        this.controllerBindings = cassette.getBindingTrees();
        this.viewRuleMap = cassette.getViewRules();
        this.reactions = cassette.reactions;
    }

    // TODO Document.
    public void start() {
        react(new Ilk<Startup>(Startup.class), new Startup());
    }
    
    // TODO Document.
    public <T> void react(Ilk<T> ilk, T object) {
        if (reactions.containsKey(object.getClass())) {
            List<Janitor> janitors = new ArrayList<Janitor>();
            InjectorBuilder newInjector = injector.newInjector();
            newInjector.instance(object, ilk, Reaction.class);
            newInjector.instance(new JanitorQueue(janitors), new Ilk<JanitorQueue>(JanitorQueue.class), Reaction.class);
            newInjector.scope(ReactionScoped.class);
            Injector reactionInjector = newInjector.newInjector();
            try {
                for (Class<?> reaction : reactions.get(object.getClass())) {
                    Object child = reactionInjector.instance(reaction, null);
                    if (child instanceof Runnable) {
                        ((Runnable) child).run();
                    }
                }
            } finally {
                cleanUp(janitors);
            }
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
    
    // TODO Document.
    private final static String SESSION_SCOPE_ATTRIBUTE_NAME = "com.goodworkalan.paste.Responder.session";

    // TODO Document.
    private InjectorBuilder getRequestInjectorBuilder(final HttpServletRequest request, final InterceptingResponse response, final List<Janitor> janitors) {
        InjectorBuilder newInjector = injector.newInjector();
        newInjector.module(new InjectorBuilder(){
            protected void build() {
                HttpSession session = request.getSession();
                scope(SessionScoped.class, (Ilk.Box) session.getAttribute(SESSION_SCOPE_ATTRIBUTE_NAME));
                scope(RequestScoped.class);
                scope(ReactionScoped.class);
                instance(request, ilk(HttpServletRequest.class), Request.class);
                instance(request, ilk(ServletRequest.class), Request.class);
                instance(response, ilk(HttpServletResponse.class), Request.class);
                instance(response, ilk(ServletResponse.class), Request.class);
                instance(response, ilk(Response.class), Request.class);
                instance(session, ilk(HttpSession.class), Request.class);
                provider(ilk(EnumeratedParametersProvider.class), ilk(Parameters.class), Request.class, RequestScoped.class);
                provider(ilk(RequestParametersProvider.class), ilk(Parameters.class), Filter.class, RequestScoped.class);
                provider(ilk(RequestParametersProvider.class), ilk(Parameters.class), null, RequestScoped.class);
                instance(new JanitorQueue(janitors), ilk(JanitorQueue.class), Request.class);
                instance(new Criteria(request), ilk(Criteria.class), Request.class);
            }
        });
        return newInjector;
    }
    
    // TODO Document.
    private InjectorBuilder getFilterInjectorBuilder(LinkedList<Injector> injectors, final HttpServletRequest request, InterceptingResponse response, List<Janitor> janitors) {
        if (injectors.isEmpty()) {
            return getRequestInjectorBuilder(request, response, janitors);
        }
        InjectorBuilder newInjector = injectors.getLast().newInjector();
        newInjector.module(new InjectorBuilder() {
            protected void build() {
                Parameters parameters = getParameters(request);
                instance(parameters, ilk(Parameters.class), Filter.class);
                instance(parameters, ilk(Parameters.class), null);
            }
        });
        return newInjector;
    }
    
    // TODO Document.
    private static Parameters getParameters(HttpServletRequest request) { 
        String query = (String) request.getAttribute("javax.servlet.include.query_string");
        if (query == null) {
            query = "";
        }
        return Parameters.fromQueryString(query);
    }
    
    // TODO Document.
    private Injector getFilterInjector(LinkedList<Injector> injectors, final HttpServletRequest request, final InterceptingResponse response, final List<Janitor> janitors) {
        InjectorBuilder newInjector = getFilterInjectorBuilder(injectors, request, response, janitors);
        newInjector.module(new InjectorBuilder(){
            protected void build() {
                scope(FilterScoped.class);
                instance(request, ilk(HttpServletRequest.class), Filter.class);
                instance(request, ilk(ServletRequest.class), Filter.class);
                instance(response, ilk(HttpServletResponse.class), Filter.class);
                instance(response, ilk(ServletResponse.class), Filter.class);
                instance(response, ilk(Response.class), Filter.class);
                instance(new JanitorQueue(janitors), ilk(JanitorQueue.class), Filter.class);
                instance(new JanitorQueue(janitors), ilk(JanitorQueue.class), null);
                instance(new Criteria(request), ilk(Criteria.class), Filter.class);
                instance(new Criteria(request), ilk(Criteria.class), null);
            }
        });
        Injector injector = newInjector.newInjector();
        injectors.addLast(injector);
        return injector;
    }
    
    // TODO Document.
    private void popFilter(LinkedList<Injector> injectors, HttpServletRequest request) {
        Injector injector = injectors.removeLast();
        if (injectors.isEmpty()) {
            request.getSession().setAttribute(SESSION_SCOPE_ATTRIBUTE_NAME, injector.scope(SessionScoped.class));
            injectors.remove();
        }
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
        List<Janitor> janitors = new ArrayList<Janitor>();
        LinkedList<Injector> injectors = INJECTORS.get();
        Injector injector = getFilterInjector(injectors, request, response, janitors);
        try {
            filter(injector, interception, chain);
        } finally {
            cleanUp(janitors);
            popFilter(injectors, request);
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
    private void filter(Injector injector, Interception interception, FilterChain chain)
    throws IOException, ServletException {
        // Use Guice to generate the criteria for this invocation, which will
        // in turn provide us with a path we can match.
        Criteria criteria = injector.instance(Criteria.class, Filter.class);

        PasteControllerException caught = null;
        Injector controllerInjector = null;
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
            Globber<RuleMap<Pair<Integer,Class<?>>>>globber = tree.newGlobber(new InjectedMatchTestFactory(injector));

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

                HttpServletRequest request = injector.instance(HttpServletRequest.class, Filter.class);
                // Apply the rule set associated with each matched glob.
                for (Match<RuleMap<Pair<Integer, Class<?>>>> mapping : matches) {
                    List<Pair<Integer, Class<?>>> bindings = mapping.getObject()
                        .test()
                            .put(BindKey.METHOD, request.getMethod())
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
                    controllerInjector = null;
                    controller = null;

                    // Clear the controller scope, null the controller.
                    InjectorBuilder newControllerInjector = injector.newInjector();
                    
                    // We cheat a little; instead of creating a provider, we're
                    // going to just tuck the mappings into the controller scope.
                    newControllerInjector.instance(mappings, new Ilk<Map<String, String>>(){ }, Controller.class);
                    newControllerInjector.instance(controllerClass, new Ilk<Class<?>>() {}, Controller.class);
                    newControllerInjector.provider(new Ilk<ControllerProvider>() {}, new Ilk<Object>(Object.class), Controller.class, ControllerScoped.class);

                    controllerInjector = newControllerInjector.newInjector();
                    
                    
                    try {
                        controller = controllerInjector.instance(controllerClass, Controller.class);
                    } catch (InjectException e) {
                        if (e.code == Reflective.INVOCATION_TARGET) {
                            try {
                                throw new PasteControllerException(e, 3);
                            } catch (PasteControllerException pce) {
                                caught = pce;
                                break CONTROLLERS;
                            }
                        }
                        // Otherwise, this is a big, bad server error.
                        throw e;
                    }
    
                    // Invoke any actors upon the controller.
                    Actors actors = controller.getClass().getAnnotation(Actors.class);
                    if (actors != null) {
                        for (Class<? extends Runnable> actor : actors.value()) {
                            try {
                                injector.instance(actor, null).run();
                            } catch (PasteControllerException pce) {
                                caught = pce;
                                break CONTROLLERS;
                            }
                        }
                    }
                }
            }
        }

        // Exceptions are also used for those things in HTTP that feel like
        // an abrupt change of course.
        if (caught.getControllerException() instanceof Redirection) {
            // FIXME Who says that this actually gets rendered as a redirect?
            injector.instance(Redirector.class, null).redirect(((Redirection) caught.getControllerException()).getWhere());
        } else if (caught.getControllerException() instanceof Abnormality) {
            // Set the response status.
            injector.instance(Response.class, null).setStatus(((Abnormality) caught.getControllerException()).getStatus());
        }

        if (controllerInjector != null || caught != null) {
            // Get a list of render modules whose rules match the current
            // request values and the current controller or exception.
            List<Pair<Integer, List<InjectorBuilder>>> views = viewRuleMap
                .test()
                    .put(BindKey.PACKAGE, controller == null ? null : controller.getClass().getPackage().getName())
                    .put(BindKey.CONTROLLER_CLASS, controller)
                    .put(BindKey.PATH, criteria.getPath())
                    .put(BindKey.STATUS, injector.instance(Response.class, null).getStatus())
                    .put(BindKey.EXCEPTION_CLASS, caught == null ? null : caught.getControllerException())
                    .put(BindKey.METHOD, injector.instance(HttpServletRequest.class, null).getMethod())
                    .get();

            // Find the render module with the highest priority.
            List<InjectorBuilder> renderModules = null;
            int hightestPriority = Integer.MIN_VALUE;
            int found = 0;
            for (Pair<Integer, List<InjectorBuilder>> view : views) {
                int priority = Tuple.get1(view);
                if (priority > hightestPriority) {
                    found = 1;
                    hightestPriority = priority;
                    renderModules = Tuple.get2(view);
                } else if (priority == hightestPriority) {
                    found++;
                }
            }

            if (found > 1) {
                // Renderer is ambiguous.
                throw new PasteException(0);
            } else if (found == 1) {
                // Render output.
                InjectorBuilder newInjector = controller == null ? injector.newInjector() : controllerInjector.newInjector();
                for (InjectorBuilder module : renderModules) {
                    newInjector.module(module);
                }
                newInjector.newInjector().instance(Renderer.class, null).render();
            } else if (caught != null) {
                throw caught;
            }
        }

        // Down the filter chain, unless we've just sent a response.
        if (!interception.isIntercepted()) {
            chain.doFilter(injector.instance(ServletRequest.class, null), injector.instance(ServletResponse.class, null));
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
