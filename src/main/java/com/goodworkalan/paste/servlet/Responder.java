package com.goodworkalan.paste.servlet;

import static com.goodworkalan.ilk.Types.getRawClass;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.goodworkalan.winnow.RuleMap;
import com.goodworkalan.dovetail.GlobTree;
import com.goodworkalan.dovetail.Globber;
import com.goodworkalan.dovetail.Match;
import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.ilk.association.IlkAssociation;
import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.paste.actor.ControllerException;
import com.goodworkalan.paste.connector.Connector;
import com.goodworkalan.paste.connector.Router;
import com.goodworkalan.paste.controller.Abnormality;
import com.goodworkalan.paste.controller.Actors;
import com.goodworkalan.paste.controller.Criteria;
import com.goodworkalan.paste.controller.Headers;
import com.goodworkalan.paste.controller.InitializationParameters;
import com.goodworkalan.paste.controller.Janitor;
import com.goodworkalan.paste.controller.JanitorQueue;
import com.goodworkalan.paste.controller.NamedValueList;
import com.goodworkalan.paste.controller.Parameters;
import com.goodworkalan.paste.controller.PasteException;
import com.goodworkalan.paste.controller.Reactor;
import com.goodworkalan.paste.controller.Redirection;
import com.goodworkalan.paste.controller.Renderer;
import com.goodworkalan.paste.controller.Routes;
import com.goodworkalan.paste.controller.Startup;
import com.goodworkalan.paste.controller.qualifiers.Application;
import com.goodworkalan.paste.controller.qualifiers.Controller;
import com.goodworkalan.paste.controller.qualifiers.Filter;
import com.goodworkalan.paste.controller.qualifiers.Reaction;
import com.goodworkalan.paste.controller.qualifiers.Request;
import com.goodworkalan.paste.controller.qualifiers.Response;
import com.goodworkalan.paste.controller.scopes.ApplicationScoped;
import com.goodworkalan.paste.controller.scopes.ControllerScoped;
import com.goodworkalan.paste.controller.scopes.FilterScoped;
import com.goodworkalan.paste.controller.scopes.ReactionScoped;
import com.goodworkalan.paste.controller.scopes.RequestScoped;
import com.goodworkalan.paste.controller.scopes.SessionScoped;
import com.goodworkalan.paste.redirect.Redirect;
import com.goodworkalan.reflective.Reflective;
import com.goodworkalan.reflective.ReflectiveException;

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
    // TODO Document.
    private final static String SESSION_SCOPE_ATTRIBUTE_NAME = "com.goodworkalan.paste.Responder.session";

    /** The dependency injector. */
    private final Injector injector;

    /**
     * A list of URL bindings that map a path to a Deviate rule set that further
     * winnows the matches based on request parameters.
     * <p>
     * FIXME Maybe deviate should be called Winnow. Deviate will probably make
     * people think about statistics.
     */
    private final List<GlobTree<RuleMap<Cassette.ControllerCandidate>>> connections;

    /**
     * The rule map used to select a renderer for a given controller or thrown
     * exception.
     */
    private final RuleMap<Cassette.RenderCandidate> renderers;

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

    // TODO Document.
    private final IlkAssociation<Class<?>> interceptors;
    
    /**
     * Create a responder from using the given filter servlet context and the
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

        connector
            .render()
                .exception(Redirection.class)
                .priority(Integer.MIN_VALUE)
                .with(Redirect.class)
                .end()
            .end();

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
        this.connections = cassette.getConnections();
        this.renderers = cassette.getRenderers();
        this.reactions = cassette.reactions;
        this.interceptors =  cassette.interceptors;
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
        Headers responseHeaders = new Headers();
        Interception interception = new Interception();
        filter(new InterceptingRequest(interception, request),
               new InterceptingResponse(interception, response), interception, responseHeaders,
               chain);
    }
    
    // TODO Document.
    private InjectorBuilder getRequestInjectorBuilder(final HttpServletRequest request, final InterceptingResponse response, final List<Janitor> janitors) {
        InjectorBuilder newInjector = injector.newInjector();
        newInjector.module(new InjectorBuilder(){
            protected void build() {
                reflector(new Reflector());
                HttpSession session = request.getSession();
                scope(SessionScoped.class, (Ilk.Box) session.getAttribute(SESSION_SCOPE_ATTRIBUTE_NAME));
                scope(RequestScoped.class);
                scope(ReactionScoped.class);
                provider(new ReponseHeadersProvider(response), ilk(Headers.class), Response.class, null);
                provider(new ResponseStatusProvider(response), ilk(Integer.class), Response.class, null);
                instance(session, ilk(HttpSession.class), Request.class);
                provider(ilk(EnumeratedParametersProvider.class), ilk(Parameters.class), Request.class, RequestScoped.class);
                provider(ilk(RequestParametersProvider.class), ilk(Parameters.class), Filter.class, RequestScoped.class);
                provider(ilk(RequestParametersProvider.class), ilk(Parameters.class), null, RequestScoped.class);
                instance(new JanitorQueue(janitors), ilk(JanitorQueue.class), Request.class);
                instance(new Criteria(request), ilk(Criteria.class), Request.class);
                provider(ilk(ControllerParametersProvider.class), ilk(Parameters.class), Controller.class, ControllerScoped.class);
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
        return new Parameters(new NamedValueList(query));
    }
    
    // TODO Document.
    private Injector getFilterInjector(LinkedList<Injector> injectors, final HttpServletRequest request, final InterceptingResponse response, final List<Janitor> janitors, Headers responseHeaders) {
        InjectorBuilder newInjector = getFilterInjectorBuilder(injectors, request, response, janitors);
        newInjector.module(new InjectorBuilder(){
            protected void build() {
                scope(FilterScoped.class);
                instance(request, ilk(HttpServletRequest.class), Request.class);
                instance(request, ilk(ServletRequest.class), Request.class);
                instance(response, ilk(HttpServletResponse.class), Response.class);
                instance(response, ilk(HttpServletResponse.class), null);
                instance(response, ilk(ServletResponse.class), Response.class);
                instance(response, ilk(ServletResponse.class), null);
                instance(request, ilk(HttpServletRequest.class), Filter.class);
                instance(request, ilk(HttpServletRequest.class), null);
                instance(request, ilk(ServletRequest.class), Filter.class);
                instance(request, ilk(ServletRequest.class), null);
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
            INJECTORS.remove();
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
                        Headers responseHeaders,
                        FilterChain chain)
    throws IOException, ServletException {
        List<Janitor> janitors = new ArrayList<Janitor>();
        LinkedList<Injector> injectors = INJECTORS.get();
        Injector injector = getFilterInjector(injectors, request, response, janitors, responseHeaders);
        try {
            filter(injector, interception, chain);
        } finally {
            cleanUp(janitors);
            popFilter(injectors, request);
        }
    }
    
    // TODO Document.
    private Injector controller(Injector injector, final Class<?> controllerClass, final Map<String, String> mappings) {
        for (Class<?> interceptorClass : interceptors.getAll(new Ilk.Key(controllerClass))) {
            controller(injector, interceptorClass, mappings);
        }

        // Clear the controller scope, null the controller.
        InjectorBuilder newControllerScopeInjector = injector.newInjector();
        
        newControllerScopeInjector.module(new InjectorBuilder() {
            protected void build() {
                scope(ControllerScoped.class);
                instance(mappings, new Ilk<Map<String, String>>(){ }, Controller.class);
            }
        });

        Injector controllerScopeInjector = newControllerScopeInjector.newInjector();

        before(controllerScopeInjector, controllerClass);
        
        final Ilk.Box controller;
        try {
            controller = controllerScopeInjector.getVendor(new Ilk.Key(controllerClass), Controller.class).get(controllerScopeInjector);
        } catch (InvocationTargetException e) {
            throw new ControllerException(e);
        } catch (Throwable e) {
            throw new PasteException(Reflective.encode(e), e);
        }
        after(controllerScopeInjector, controller);
        return controllerInstanceInjector(controllerScopeInjector, controller);
    }
    
    // TODO Document.
    private Injector controllerInstanceInjector(Injector controllerScopeInjector, final Ilk.Box controller) {
        InjectorBuilder newControllerInstanceInjector = controllerScopeInjector.newInjector();
        newControllerInstanceInjector.module(new InjectorBuilder() {
            protected void build() {
                box(controller, ilk(Object.class), Controller.class);
            }
        });
        return newControllerInstanceInjector.newInjector();
    }
    
    private void before(Injector controllerScopeInjector, final Class<?> controllerClass) {
        if (controllerClass.isMemberClass()) {
            before(controllerScopeInjector, controllerClass.getDeclaringClass());
        }

        InjectorBuilder newInterceptorInjector = controllerScopeInjector.newInjector();
        newInterceptorInjector.module(new InjectorBuilder() {
            protected void build() {
                instance(controllerClass, InjectorBuilder.ilk(Class.class), Controller.class);
            }
        });

        Injector interceptorInjector = newInterceptorInjector.newInjector();

        Actors actors = controllerClass.getAnnotation(Actors.class);
        
        if (actors != null) {
            for (Class<? extends Runnable> actor : actors.before()) {
                interceptorInjector.instance(actor, null).run();
            }
        }
    }

    private void after(Injector controllerScopeInjector, Ilk.Box controller) {
        if (getRawClass(controller.key.type).isMemberClass()) {
            after(controllerScopeInjector, controllerScopeInjector.getOwnerInstance(controller.object));
        }
        Injector controllerInstanceInjector = controllerInstanceInjector(controllerScopeInjector, controller);
        after(controllerInstanceInjector, getRawClass(controller.key.type).getAnnotation(Actors.class));
    }

    private void after(Injector injector, Actors actors) {
        if (actors != null) {
            for (Class<? extends Runnable> actor : actors.value()) {
                injector.instance(actor, null).run();
            }
            for (Class<? extends Runnable> actor : actors.after()) {
                injector.instance(actor, null).run();
            }
        }
    }

    private Injector controller(Injector injector, Interception interception, Criteria criteria) {
        Injector controllerInjector = null;
        // We try each series of binding definitions in order. There can be
        // multiple bindings that match, applying multiple controllers.
        for (GlobTree<RuleMap<Cassette.ControllerCandidate>> tree : connections) {
            // If a controller has written a response, we're done.
            if (interception.isIntercepted()) {
                break;
            }
            
            // Create a globber that will apply tests created by the Guice
            // injector or this Paste filter.
            Globber<RuleMap<Cassette.ControllerCandidate>>globber = tree.newGlobber(new InjectedMatchTestFactory(injector));

            // Attempt to match the path.
            List<Match<RuleMap<Cassette.ControllerCandidate>>> matches = globber.map(criteria.getPath());

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
                for (Match<RuleMap<Cassette.ControllerCandidate>> mapping : matches) {
                    Map<Object, Object> conditions = new HashMap<Object, Object>();
                    conditions.put(BindKey.METHOD, request.getMethod());
                    conditions.put(BindKey.PATH, criteria.getPath());
                    List<Cassette.ControllerCandidate> candidates = mapping.getObject().get(conditions);
                    for (Cassette.ControllerCandidate candidate : candidates) {
                        int priority = candidate.priority;
                        if (priority > highestPriority) {
                            found = 1;
                            highestPriority = priority;
                            controllerClass = candidate.controllerClass;
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
                    controllerInjector = controller(injector, controllerClass, mappings);
                }
            }
        }
        return controllerInjector;
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

        ControllerException caught = null;
        Injector controllerInjector = null;

        try {
            controllerInjector = controller(injector, interception, criteria);
        } catch (ControllerException e) {
            caught = e;

            System.out.println(caught.getCause().getCause());

            // Exceptions are also used for those things in HTTP that feel like
            // an abrupt change of course.
            if (caught.getCause().getCause() instanceof Abnormality) {
                // Set the response status.
                injector.instance(HttpServletResponse.class, null).setStatus(((Abnormality) caught.getCause().getCause()).getStatus());
            }
        }

        
        List<InjectorBuilder> modules = null;
        for (;;) {
            Object controller = null;
            if (controllerInjector != null) { 
                controller = controllerInjector.instance(Object.class, Controller.class);
            }

            if (modules != null) {
                break;
            }

            if (controllerInjector == null && caught == null) {
                break;
            }

            // FIXME: Can I use Stash for these bindings?

            // Get a list of render modules whose rules match the current
            // request values and the current controller or exception.
            Map<Object, Object> conditions = new HashMap<Object, Object>();
            conditions.put(BindKey.PACKAGE, controller == null ? null : controller.getClass().getPackage().getName());
            conditions.put(BindKey.CONTROLLER_CLASS, controller);
            conditions.put(BindKey.PATH, criteria.getPath());
            conditions.put(BindKey.STATUS, injector.instance(Integer.class, Response.class));
            conditions.put(BindKey.EXCEPTION_CLASS, caught == null ? null : caught.getCause().getCause());
            conditions.put(BindKey.METHOD, injector.instance(HttpServletRequest.class, null).getMethod());
            List<Cassette.RenderCandidate> candidates = renderers.get(conditions);
            SortedMap<Integer, Cassette.RenderCandidate> choose = new TreeMap<Integer, Cassette.RenderCandidate>();
            if (!candidates.isEmpty()) {
                // Find the render module with the highest priority.
                for (Cassette.RenderCandidate candidate : candidates) {
                    choose.put(candidate.priority, candidate);
                }
                modules = choose.get(choose.firstKey()).modules;
                InjectorBuilder newInjector = controllerInjector == null ? injector.newInjector() : controllerInjector.newInjector();
                for (InjectorBuilder module : modules) {
                    newInjector.module(module);
                }
                if (caught != null) {
                    newInjector.instance(caught.getCause().getCause(), new Ilk<Throwable>(Throwable.class), Controller.class);
                }
                newInjector.newInjector().instance(Renderer.class, null).render();
            } else if (caught != null) {
                throw caught;
            } else if (controller != null && controller.getClass().isMemberClass()) {
                controllerInjector = controllerInstanceInjector(controllerInjector, controllerInjector.getOwnerInstance(controller));
            } else {
                break;
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
