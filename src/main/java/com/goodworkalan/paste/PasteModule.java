package com.goodworkalan.paste;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.goodworkalan.paste.janitor.Janitor;
import com.goodworkalan.paste.janitor.JanitorQueue;
import com.goodworkalan.paste.util.Parameters;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

/**
 * The Guice module that defines the filter bindings.
 * 
 * @author Alan Gutierrez
 */
class PasteModule extends AbstractModule {
    /** The servlet context of the paste filter. */
    private final ServletContext servletContext;

    /** The routes defined for the filter. */
    private final Routes routes;

    /** The map of filter initialization parameters. */
    private final Map<String, String> initialization;

    /** The janitors to run when the filter shuts down. */
    private final Collection<Janitor> applicationJanitors;
    
    /** The application scope map. */
    private final Map<Key<?>, Object> applicationScope;
    
    private final Reactor reactor;

    /**
     * Create a module with the given routes, given filter initialization
     * parameters, and the given list of janitors to run when the filter shuts
     * down.
     * 
     * @param servletContext
     *            The Servlet context.
     * @param applicationScope
     *            The application scope map.
     * @param routes
     *            The routes defined for the filter.
     * @param initialization
     *            The filter initialization parameters.
     * @param applicationJanitors
     *            The janitors to run when the filter shuts down.
     */
    public PasteModule(ServletContext servletContext, Map<Key<?>, Object> applicationScope, Routes routes, Map<String, String> initialization, Collection<Janitor> applicationJanitors, Reactor reactor) {
        this.applicationScope = applicationScope;
        this.servletContext = servletContext;
        this.routes = routes;
        this.initialization = initialization;
        this.applicationJanitors = applicationJanitors;
        this.reactor = reactor;
    }

    /**
     * Bind the paste filter implementations.
     */
    @Override
    protected void configure()
    {
        bindScope(ApplicationScoped.class, new ApplicationScope(applicationScope));
        bindScope(SessionScoped.class, Scopes.SESSION);
        bindScope(RequestScoped.class, Scopes.REQUEST);
        bindScope(ReactionScoped.class, Scopes.REACTION);
        bindScope(FilterScoped.class, Scopes.FILTER);
        bindScope(ControllerScoped.class, Scopes.CONTROLLER);
        
        bind(Reactor.class).toInstance(reactor);
        bind(new TypeLiteral<Map<String, String>>() {})
            .annotatedWith(InitializationParameters.class)
            .toInstance(initialization);

        bind(ServletContext.class)
            .toProvider(new ServletContextProvider(servletContext));
        
        bind(JanitorQueue.class)
            .annotatedWith(Application.class)
            .toInstance(new JanitorQueue(applicationJanitors));
        bind(JanitorQueue.class)
            .annotatedWith(Request.class)
            .toProvider(RequestJanitorQueueProvider.class)
            .in(RequestScoped.class);
        bind(JanitorQueue.class)
            .toProvider(FilterJanitorQueueProvider.class)
            .in(FilterScoped.class);
        
        bind(Filtration.class)
            .annotatedWith(Request.class)
            .toProvider(RequestFiltrationProvider.class);
        bind(Filtration.class)
            .annotatedWith(Filter.class)
            .toProvider(FilterFiltrationProvider.class);
        
        
        bind(Routes.class)
            .toInstance(routes);
        
        // Need to bind to null provider so that they are bound when you 
        // set the request scope.
        bind(HttpSession.class)
            .toProvider(HttpSessionProvider.class)
            .in(RequestScoped.class);
        
        // The default request is the filter request.
        bind(HttpServletRequest.class)
            .toProvider(FilterHttpServletRequestProvider.class)
            .in(FilterScoped.class);
        bind(ServletRequest.class)
            .toProvider(FilterHttpServletRequestProvider.class)
            .in(FilterScoped.class);
        
        bind(HttpServletResponse.class)
            .toProvider(HttpServletResponseProvider.class)
            .in(FilterScoped.class);
        bind(ServletResponse.class)
            .toProvider(HttpServletResponseProvider.class)
            .in(FilterScoped.class);
        bind(Response.class)
            .toProvider(ResponseProvider.class)
            .in(FilterScoped.class);

        bind(Criteria.class)
            .annotatedWith(Request.class)
            .toProvider(RequestCriteriaProvider.class)
            .in(RequestScoped.class);
        bind(Criteria.class)
            .annotatedWith(Filter.class)
            .toProvider(FilterCriteriaProvider.class)
            .in(FilterScoped.class);
        bind(Criteria.class)
            .toProvider(FilterCriteriaProvider.class)
            .in(FilterScoped.class);
        
        bind(Parameters.class)
            .annotatedWith(Request.class)
            .toProvider(RequestParametersProvider.class)
            .in(RequestScoped.class);
        bind(Parameters.class)
            .annotatedWith(Filter.class)
            .toProvider(FilterParametersProvider.class)
            .in(FilterScoped.class);
        bind(Parameters.class)
            .annotatedWith(Controller.class)
            .toProvider(ControllerParametersProvider.class)
            .in(ControllerScoped.class);
        bind(Parameters.class)
            .toProvider(ControllerParametersProvider.class)
            .in(ControllerScoped.class);
        
        // We cheat here, we don't build these with guice, but seed them into
        // the controller scope's backing map.
        bind(Object.class)
            .annotatedWith(Controller.class)
            .toProvider(new NullProvider<Parameters>())
            .in(ControllerScoped.class);
        bind(new TypeLiteral<Map<String, String>>(){})
            .annotatedWith(Controller.class)
            .toProvider(new NullProvider<Map<String, String>>())
            .in(ControllerScoped.class);
    }
}
