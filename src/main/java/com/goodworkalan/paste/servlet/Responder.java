package com.goodworkalan.paste.servlet;

import static com.goodworkalan.ilk.Types.getRawClass;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.goodworkalan.dovetail.Match;
import com.goodworkalan.dovetail.Path;
import com.goodworkalan.dovetail.PathAssociation;
import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.ilk.association.IlkAssociation;
import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.paste.actor.ControllerException;
import com.goodworkalan.paste.connector.Connector;
import com.goodworkalan.paste.connector.Router;
import com.goodworkalan.paste.controller.Actors;
import com.goodworkalan.paste.controller.Criteria;
import com.goodworkalan.paste.controller.Headers;
import com.goodworkalan.paste.controller.HttpError;
import com.goodworkalan.paste.controller.JanitorQueue;
import com.goodworkalan.paste.controller.NamedValueList;
import com.goodworkalan.paste.controller.Parameters;
import com.goodworkalan.paste.controller.Reactions;
import com.goodworkalan.paste.controller.Reactor;
import com.goodworkalan.paste.controller.Renderer;
import com.goodworkalan.paste.controller.Startup;
import com.goodworkalan.paste.controller.qualifiers.Application;
import com.goodworkalan.paste.controller.qualifiers.Controller;
import com.goodworkalan.paste.controller.qualifiers.Filter;
import com.goodworkalan.paste.controller.qualifiers.Reaction;
import com.goodworkalan.paste.controller.qualifiers.Request;
import com.goodworkalan.paste.controller.qualifiers.Response;
import com.goodworkalan.paste.controller.qualifiers.Verb;
import com.goodworkalan.paste.controller.scopes.ApplicationScoped;
import com.goodworkalan.paste.controller.scopes.ControllerScoped;
import com.goodworkalan.paste.controller.scopes.FilterScoped;
import com.goodworkalan.paste.controller.scopes.ReactionScoped;
import com.goodworkalan.paste.controller.scopes.RequestScoped;
import com.goodworkalan.paste.controller.scopes.SessionScoped;
import com.goodworkalan.winnow.RuleMap;

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
    /** The session attribute name for the session scope storage object. */
    private final static String SESSION_SCOPE_ATTRIBUTE_NAME = "com.goodworkalan.paste.Responder.session";

    /** The dependency injector. */
    private final Injector injector;

    /**
     * A list of URL bindings that map a path to a Deviate rule set that further
     * winnows the matches based on request parameters.
     */
    private final List<PathAssociation<RuleMap<BindKey, Class<?>>>> connections;

    /**
     * The rule map used to select a renderer for a given controller or thrown
     * exception.
     */
    private final RuleMap<BindKey, List<InjectorBuilder>> renderers;

    /** The list of janitors to run when the filter is shutdown. */
    private final LinkedBlockingQueue<Runnable> janitors = new LinkedBlockingQueue<Runnable>();

    /** The reaction types to controllers. */
    private final IlkAssociation<Class<?>> reactionsByClass;
    
    /** The reaction annotations to controllers. */
    private final Map<Class<? extends Annotation>, List<Class<?>>> reactionsByAnnotation;

    /**
     * The thread local stack of injectors, one injector for the first
     * invocation of the filter, and one injector for each subsequent forwarded
     * or included call to the filter.
     */
    private final ThreadLocal<LinkedList<Injector>> INJECTORS = new ThreadLocal<LinkedList<Injector>>() {
         protected java.util.LinkedList<Injector> initialValue() {
             return new LinkedList<Injector>();
         }
    };

    /** The controller type or annotation to interceptor association. */
    private final IlkAssociation<Class<?>> interceptors;

    /**
     * Create a responder from using the given filter servlet context and the
     * given Paste filter initialization parameters.
     * 
     * @param servletContext
     *            The Paste filter servlet context.
     * @param initialization
     *            The Paste filter initialization parameters.
     * @throws ServletException
     *             For any configuration errors.
     */
    public Responder(final ServletContext servletContext, final Map<String, String> initialization) throws ServletException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        List<InjectorBuilder> modules = new ArrayList<InjectorBuilder>();
        if (initialization.containsKey("Modules")) {
            for (String className : initialization.get("Modules").split(",")) {
                Class<?> moduleClass;
                try {
                    moduleClass = classLoader.loadClass(className);
                } catch (ClassNotFoundException e) {
                    String message = String.format("\n\tCannot locate Ilk module.\n\t\tModule [%s]", className);
                    throw new ServletException(message, e);
                }
                InjectorBuilder module;
                try {
                    module = (InjectorBuilder) moduleClass.newInstance();
                } catch (Exception e) {
                    String message = String.format("\n\tCannot construct instance of Ilk module.\n\t\tModule [%s]", moduleClass.getName());
                    throw new ServletException(message, e);
                }
                modules.add(module);
            }
        }

        List<Router> routers = new ArrayList<Router>();
        if (initialization.containsKey("Routers")) {
            for (String className : initialization.get("Routers").split(",")) {
                Class<?> moduleClass;
                try {
                    moduleClass = classLoader.loadClass(className);
                } catch (ClassNotFoundException e) {
                    String message = String.format("\n\tCannot locate router module.\n\t\tModule [%s]", className);
                    throw new ServletException(message, e);
                }
                try {
                    Class<? extends Router> routerClass = moduleClass.asSubclass(Router.class);
                    routers.add(routerClass.newInstance());
                } catch (Exception e) {
                    String message = String.format("\n\tCannot construct instance of router module.\n\t\tModule [%s]", moduleClass.getName());
                    throw new ServletException(message, e);
                }
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
                implementation(ilk(Reactions.class), ilk(Reactions.class), null, ApplicationScoped.class);
                instance(Responder.this, ilk(Reactor.class), null);
                instance(initialization, new Ilk<Map<String, String>>() { }, Application.class);
                instance(servletContext, ilk(ServletContext.class), null);
                instance(new JanitorQueue(janitors), ilk(JanitorQueue.class), Application.class);
                instance(cassette.getRoutes(), new Ilk<Map<Class<?>, Path>>() {}, Application.class);
            }
        });

        for (InjectorBuilder module : modules) {
            newInjector.module(module);
        }

        this.injector = newInjector.newInjector();
        this.connections = cassette.getConnections();
        this.renderers = cassette.getRenderers();
        this.reactionsByClass = cassette.reactionsByType;
        this.reactionsByAnnotation = cassette.reactionsByAnnotation;
        this.interceptors =  cassette.interceptors;
    }

    /**
     * Start the application by generating a {@link Startup} event and
     * triggering and startup reactions.
     */
    public void start() {
        react(new Ilk<Startup>(Startup.class));
    }
    
    /**
     * Trigger a reaction of the type specified by the given type token with the
     * given object instance. Reactions are bound to types so that information
     * can be provided via an instance of the type.
     * 
     * @param <T>
     *            The event type.
     * @param ilk
     *            The super type token of the event type.
     * @param object
     *            The event.
     */
    public <T> void react(Ilk<T> ilk, T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        react(object, ilk, null);
    }

    /**
     * Trigger a reaction of the type specified by the given type token.
     * Reactions are bound to types so that information can be provided via an
     * instance of the type.
     * 
     * @param <T>
     *            The event type.
     * @param ilk
     *            The super type token of the event type.
     */
    public <T> void react(Ilk<T> ilk) {
        react(null, ilk, null);
    }
    
    /**
     * Trigger a reaction that generates an event that qualifies an object with
     * the given annotation.
     * 
     * @param annotation
     *            The annotation.
     */
    public void react(Class<? extends Annotation> annotation) {
        this.<Object>react(null, null, annotation);
    }

    /**
     * Trigger a reaction of the type specified by the given type token with the
     * given object instance or it the object is null, create an instance using
     * the injector. Reactions are bound to types so that information can be
     * provided via an instance of the type.
     * 
     * @param <T>
     *            The event type.
     * @param object
     *            The event.
     * @param ilk
     *            The super type token of the event type.
     */
    private <T> void react(T object, Ilk<T> ilk, Class<? extends Annotation> qualifier) {
        List<Class<?>> controllers = null;
        if (ilk != null) {
            controllers = reactionsByClass.getAll(ilk.key);
        } else {
            controllers = reactionsByAnnotation.get(qualifier);
        }
        if (controllers != null) {
            LinkedList<Injector> injectors = INJECTORS.get();
            boolean isRoot = injectors.isEmpty();
            Injector parentInjector = isRoot ? injector : injectors.getLast();
            List<Runnable> janitors = new ArrayList<Runnable>();
            try {
                InjectorBuilder newInjector = parentInjector.newInjector();
                if (object != null) {
                    newInjector.instance(object, ilk, Reaction.class);
                } else if (ilk != null ){
                    newInjector.implementation(ilk, ilk, null, null);
                } else {
                    newInjector.implementation(new Ilk<Object>(Object.class), new Ilk<Object>(Object.class), qualifier, null);
                }
                if (isRoot) {
                    newInjector.instance(new JanitorQueue(janitors), new Ilk<JanitorQueue>(JanitorQueue.class), Reaction.class);
                    newInjector.scope(ReactionScoped.class);
                }
                Injector reactionInjector = newInjector.newInjector();
                injectors.addLast(reactionInjector);
                for (Class<?> reaction : controllers) {
                    List<Runnable> controllerJanitors = new ArrayList<Runnable>(); 
                    try {
                        controller(reactionInjector, reaction, new HashMap<String, String>(), controllerJanitors);
                    } finally { 
                        cleanUp(controllerJanitors);
                    }
                }
            } finally {
                cleanUp(janitors);
                injectors.removeLast();
                if (isRoot) {
                    INJECTORS.remove();
                }
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

    /**
     * Construct the first injector, the root injector that will contain any
     * child injectors created by subsequent forwarded or included calls that
     * invoke the filter.
     * <p>
     * This will resurrect the sesssion scope, initail 
     * 
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     * @param janitors
     *            The set of request janitors.
     */
    private InjectorBuilder getRequestInjectorBuilder(final HttpServletRequest request, final InterceptingResponse response, final List<Runnable> janitors) {
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
                instance(session, ilk(HttpSession.class), null);
                provider(ilk(EnumeratedParametersProvider.class), ilk(Parameters.class), Request.class, RequestScoped.class);
                provider(ilk(RequestParametersProvider.class), ilk(Parameters.class), Filter.class, RequestScoped.class);
                provider(ilk(RequestParametersProvider.class), ilk(Parameters.class), null, RequestScoped.class);
                instance(new JanitorQueue(janitors), ilk(JanitorQueue.class), Request.class);
                instance(new JanitorQueue(janitors), ilk(JanitorQueue.class), Reaction.class);
                instance(new Criteria(request), ilk(Criteria.class), Request.class);
                provider(ilk(ControllerParametersProvider.class), ilk(Parameters.class), Controller.class, ControllerScoped.class);
                instance(request.getMethod(), ilk(String.class), Verb.class);
            }
        });
        return newInjector;
    }

    /**
     * Construct an injector for an invocation of filter creating either the
     * root filter request for the first invocation of the filter, or that
     * extracts the query parameters from the query string tucked into the
     * request attributes by the servlet engine.
     * <p>
     * Note that this filter must first process the client request before
     * processing any further requests.
     * 
     * @param injectors
     *            The stack of injectors with an element for each invocation of
     *            this filter.
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     * @param janitors
     *            The list of filter invocation janitors.
     * @return An injector builder for either the root request or a forward or
     *         include request.
     */
    private InjectorBuilder getFilterInjectorBuilder(LinkedList<Injector> injectors, final HttpServletRequest request, InterceptingResponse response, List<Runnable> janitors) {
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

    /**
     * Get the query parameters for a subsequent forward or include query.
     * 
     * @param request
     *            The HTTP request.
     * @return The query parameters.
     */
    private static Parameters getParameters(HttpServletRequest request) { 
        String query = (String) request.getAttribute("javax.servlet.include.query_string");
        if (query == null) {
            query = "";
        }
        return new Parameters(new NamedValueList(query));
    }

    /**
     * Create an injector for an invocation of the filter, add the injector to
     * the injector stack, and return the newly created injector.
     * 
     * @param injectors
     *            The injector stack.
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     * @param janitors
     *            The filtration janitor queue.
     * @param responseHeaders
     *            The response headers.
     * @return The filter injector.
     */
    private Injector getFilterInjector(LinkedList<Injector> injectors, final HttpServletRequest request, final InterceptingResponse response, final List<Runnable> janitors, Headers responseHeaders) {
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

    /**
     * Pop an injector from the thread local stack of injectors that maintain an
     * injector for each invocation of the filter during a request.
     * <p>
     * If popping the injector causes the stack to become empty, store the
     * session scope in a session attribute, and clear the thread local storage.
     * 
     * @param injectors
     *            The thread local injector stack.
     * @param request
     *            The HTTP request.
     */
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
        List<Runnable> janitors = new ArrayList<Runnable>();
        LinkedList<Injector> injectors = INJECTORS.get();
        Injector injector = getFilterInjector(injectors, request, response, janitors, responseHeaders);
        try {
            filter(injector, interception, chain);
        } finally {
            cleanUp(janitors);
            popFilter(injectors, request);
        }
    }

    /**
     * Invoke a controller of the given controller class.
     * <p>
     * Prior to running the controller, an interceptors are run as controllers
     * using this method. Then a controller instnace is created that defines the
     * controller scope an binds the given mappings to a <code>Map</code>
     * interface qualified by <code>Controller</code>. Then the before actors
     * are invoked.
     * <p>
     * Then a controller is constructed using the controller scope injector, but
     * externally, so that we call the vendor directly. Then the after actors
     * are invoked, but prior to invoking them a controller injector is created.
     * This is disassembled in this way so that we can create a controller
     * injector for any owning types if the controller is a nested type.
     * Finally, a controller injector is created to return to the caller to sort
     * out rendering.
     * 
     * @param injector
     *            The filter injector.
     * @param controllerClass
     *            The controller class.
     * @param mappings
     *            The mappings obtained by the Dovetail URL pattern match.
     * @return The controller instance injector.
     */
    private Injector controller(Injector injector, final Class<?> controllerClass, final Map<String, String> mappings, final List<Runnable> janitors) {
        for (Class<?> interceptorClass : interceptors.getAll(new Ilk.Key(controllerClass))) {
            List<Runnable> interceptorJanitors = new ArrayList<Runnable>();
            controller(injector, interceptorClass, mappings, interceptorJanitors);
            cleanUp(interceptorJanitors);
        }

        // Clear the controller scope, null the controller.
        InjectorBuilder newControllerScopeInjector = injector.newInjector();
        
        newControllerScopeInjector.module(new InjectorBuilder() {
            protected void build() {
                scope(ControllerScoped.class);
                instance(mappings, new Ilk<Map<String, String>>(){ }, Controller.class);
                instance(new JanitorQueue(janitors), ilk(JanitorQueue.class), Controller.class);
            }
        });

        Injector controllerScopeInjector = newControllerScopeInjector.newInjector();

        before(controllerScopeInjector, controllerClass);
        
        // We do it this way so we can build a controller injector explicitly
        // for the owner instances of a nested controller class when we run the
        // after actions.
        final Ilk.Box controller;
        try {
            controller = controllerScopeInjector.getVendor(new Ilk.Key(controllerClass), Controller.class).get(controllerScopeInjector);
        } catch (InvocationTargetException e) {
            throw new ControllerException(e);
        } catch (Exception e) {
            String message = String.format(
                    "\n\tUnable to construct a controller." +
                    "\n\t\tController [%s]", controllerClass.getName());
            throw new RuntimeException(message, e);
        }
        after(controllerScopeInjector, controller);
        return controllerInstanceInjector(controllerScopeInjector, controller);
    }

    /**
     * Create a child injector of a controller scope injector that binds a
     * controller to <code>Object</code> qualified by <code>Controller</code>.
     * 
     * @param controllerScopeInjector
     *            The injector that defined the controller scope.
     * @param controller
     *            The controller.
     * @return A controller instance injector.
     */
    private Injector controllerInstanceInjector(Injector controllerScopeInjector, final Ilk.Box controller) {
        InjectorBuilder newControllerInstanceInjector = controllerScopeInjector.newInjector();
        newControllerInstanceInjector.module(new InjectorBuilder() {
            protected void build() {
                box(controller, ilk(Object.class), Controller.class);
                // FIXME Can we have the Ilk come in from the DSL?
                instance(controller, controller.key, Controller.class);
            }
        });
        return newControllerInstanceInjector.newInjector();
    }

    /**
     * Run any before actors specified by an {@link Actors} annotation on the
     * controller. A child injector will be created that will define the
     * controller class.
     * 
     * @param controllerScopeInjector
     *            The injector that defines the controller scope.
     * @param controllerClass
     *            The controller class.
     */
    private void before(Injector controllerScopeInjector, final Class<?> controllerClass) {
        if (controllerClass.isMemberClass()) {
            before(controllerScopeInjector, controllerClass.getDeclaringClass());
        }

        InjectorBuilder newBeforeInjector = controllerScopeInjector.newInjector();
        newBeforeInjector.module(new InjectorBuilder() {
            protected void build() {
                instance(controllerClass, InjectorBuilder.ilk(Class.class), Controller.class);
            }
        });

        Injector beforeInjector = newBeforeInjector.newInjector();

        Actors actors = controllerClass.getAnnotation(Actors.class);
        
        if (actors != null) {
            for (Class<? extends Runnable> actor : actors.before()) {
                beforeInjector.instance(actor, null).run();
            }
        }
    }

    /**
     * Run the after actors on the given controller instance, first calling the
     * after actors of the owner instance if the controller is a nested class.
     * The after actors are called using a controller instance injector that
     * binds the controller to <code>Object</code> qualified by
     * <code>Controller</code>.
     * 
     * @param controllerScopeInjector
     *            The injector that defines the controller scope.
     * @param controller
     *            The controller.
     */
    private void after(Injector controllerScopeInjector, Ilk.Box controller) {
        if (getRawClass(controller.key.type).isMemberClass()) {
            after(controllerScopeInjector, controllerScopeInjector.getOwnerInstance(controller.object));
        }
        Injector controllerInstanceInjector = controllerInstanceInjector(controllerScopeInjector, controller);
        after(controllerInstanceInjector, getRawClass(controller.key.type).getAnnotation(Actors.class));
    }

    /**
     * If the given <code>Actors</code> annotation is not null, loop over the
     * after actors, creating the actors using the given injector and running
     * them. The default value actors are executed before the explicitly after
     * actors.
     * 
     * @param injector
     *            The controller injector.
     * @param actors
     *            The controller actors annotation.
     */
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
    
    /** The pattern to match a file suffix. */
    final static Pattern SUFFIX = Pattern.compile("^(.*?)\\.([^/]+)$");

    /**
     * Find the controller for the given path and suffix, run the controller and
     * return the injector created to create the controller.
     * 
     * @param injector
     *            The injector.
     * @param interception
     *            The intercepted flag.
     * @param criteria
     *            The filter criteria.
     * @param path
     *            The path without the file suffix.
     * @param suffix
     *            The file suffix.
     * @param janitors
     *            The list of controller janitors.
     * @return The injector used to create the controller or null if no
     *         controller was found.
     */
    private Injector controller(Injector injector, Interception interception, Criteria criteria, String path, String suffix, List<Runnable> janitors) {
        Injector controllerInjector = null;
        // We try each series of binding definitions in order. There can be
        // multiple bindings that match, applying multiple controllers.
        for (PathAssociation<RuleMap<BindKey, Class<?>>> tree : connections) {
            // If a controller has written a response, we're done.
            if (interception.isIntercepted()) {
                break;
            }
            
            // Attempt to match the path.
            List<Match<RuleMap<BindKey, Class<?>>>> matches = tree.match(path);

            // We can have multiple matches, so we winnow them down by futher
            // matching request parameters, then futher winnowing them by
            // choosing a controller based on priority.
            if (!matches.isEmpty()) {
                Class<?> controllerClass = null;
                Map<String, String> mappings = null;

                HttpServletRequest request = injector.instance(HttpServletRequest.class, Filter.class);
                // Apply the rule set associated with each matched glob.
                for (Match<RuleMap<BindKey, Class<?>>> mapping : matches) {
                    Map<BindKey, Object> conditions = new HashMap<BindKey, Object>();
                    conditions.put(BindKey.METHOD, request.getMethod());
                    conditions.put(BindKey.PATH, criteria.getPath());
                    conditions.put(BindKey.SUFFIX, suffix);
                    List<Class<?>> candidates = mapping.getObject().get(conditions);
                    if (!candidates.isEmpty()) {
                        controllerClass = candidates.get(0);
                        mappings = mapping.getParameters();
                        break;
                    }
                }

                if (controllerClass != null) {
                    controllerInjector = controller(injector, controllerClass, mappings, janitors);
                }
            }
        }
        return controllerInjector;
    }

    /**
     * The heart of the filter.
     * 
     * @param injector
     *            The filtration injector.
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

        String path = criteria.getPath();
        String suffix = null;
        Matcher matcher = SUFFIX.matcher(path);
        if (matcher.matches()) {
            path = matcher.group(1);
            suffix = matcher.group(2);
        }
        
        List<Runnable> janitors = new ArrayList<Runnable>();
        
        try {
            controllerInjector = controller(injector, interception, criteria, path, suffix, janitors);
        } catch (ControllerException e) {
            caught = e;
            // Exceptions are also used for those things in HTTP that feel like
            // an abrupt change of course.
            if (caught.getCause().getCause() instanceof HttpError) {
                // Set the response status.
                injector.instance(HttpServletResponse.class, null).setStatus(((HttpError) caught.getCause().getCause()).getStatus());
            }
        } finally {
            cleanUp(janitors);
        }

        janitors.clear();
        try {
            render(injector, criteria, controllerInjector, caught, suffix);
        } finally {   
            cleanUp(janitors);
        }

        // Down the filter chain, unless we've just sent a response.
        if (!interception.isIntercepted()) {
            chain.doFilter(injector.instance(ServletRequest.class, null), injector.instance(ServletResponse.class, null));
        }
    }

    /**
     * Render the final controller or caught exception.
     * 
     * @param injector
     *            The filtration injector.
     * @param criteria
     *            The filter critiera.
     * @param controllerInjector
     *            The controller injector or null if an exception was thrown.
     * @param caught
     *            The caught exception or null if no exception was thrown.
     * @param suffix
     *            The file suffix.
     * @throws ServletException
     *             For any Servlet engine exceptions thrown during rendering.
     * @throws IOException
     *             For any I/O error.
     */
    private void render(Injector injector, Criteria criteria, 
            Injector controllerInjector,  ControllerException caught, String suffix)
    throws ServletException, IOException {
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
   
            // Get a list of render modules whose rules match the current
            // request values and the current controller or exception.
            Map<BindKey, Object> conditions = new HashMap<BindKey, Object>();
            conditions.put(BindKey.PACKAGE, controller == null ? null : controller.getClass().getPackage().getName());
            conditions.put(BindKey.CONTROLLER, controller);
            conditions.put(BindKey.CONTROLLER_CLASS, controller == null ? null : controller.getClass());
            conditions.put(BindKey.PATH, criteria.getPath());
            conditions.put(BindKey.STATUS, injector.instance(Integer.class, Response.class));
            conditions.put(BindKey.EXCEPTION_CLASS, caught == null ? null : caught.getCause().getCause());
            conditions.put(BindKey.METHOD, injector.instance(HttpServletRequest.class, null).getMethod());
            conditions.put(BindKey.SUFFIX, suffix);
            List<List<InjectorBuilder>> candidates = renderers.get(conditions);
            if (!candidates.isEmpty()) {
                // Use the render module with the highest priority.
                modules = candidates.get(0);
                InjectorBuilder newInjector = controllerInjector == null ? injector.newInjector() : controllerInjector.newInjector();
                for (InjectorBuilder module : modules) {
                    newInjector.module(module);
                }
                if (caught != null) {
                    newInjector.instance(caught.getCause().getCause(), new Ilk<Throwable>(Throwable.class), Controller.class);
                }
                newInjector.newInjector().instance(Renderer.class, null).render();
                break;
            } else if (caught != null) {
                throw caught;
            } else if (controller != null && controller.getClass().isMemberClass()) {
                controllerInjector = controllerInstanceInjector(controllerInjector, controllerInjector.getOwnerInstance(controller));
            } else {
                break;
            }
        }
    }

    /**
     * Invoke the clean up method on each janitor in the given list of janitors.
     * 
     * @param janitors
     *            The collection of janitors to clean up.
     */
    private void cleanUp(Collection<Runnable> janitors) {
        for (Runnable janitor : janitors) {
            try {
                janitor.run();
            } catch (Throwable t) {
                t.printStackTrace();
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
