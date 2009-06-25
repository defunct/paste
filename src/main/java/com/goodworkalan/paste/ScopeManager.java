package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.goodworkalan.paste.faults.Faults;
import com.goodworkalan.paste.janitor.Janitor;
import com.goodworkalan.paste.janitor.JanitorQueue;
import com.goodworkalan.paste.util.NamedValue;
import com.goodworkalan.paste.util.Parameters;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;

public class ScopeManager
{
    private boolean startedRequest;
    private final BasicScope requestScope;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final String path;
    private final List<Janitor> requestJanitors;
    private final ServletContext servletContext;
    private final Map<String, String> initialization;
    public ScopeManager(BasicScope scope, HttpServletRequest request, HttpServletResponse response,
        String path, List<Janitor> requestJanitors, ServletContext servletContext, Map<String, String> initialization)
    {
        this.requestScope = scope;
        this.request = request;
        this.response = response;
        this.path = path;
        this.requestJanitors = requestJanitors;
        this.servletContext = servletContext;
        this.initialization = initialization;
    }
    
    // TODO Document.
    public void enterRequest()
    {
        if (!startedRequest)
        {
            startedRequest = true;
            
            requestScope.enter();
            
            requestScope.seed(ServletContext.class, servletContext);
            requestScope.seed(Key.get(new TypeLiteral<Map<String, String>>() {}, InitializationParameters.class), initialization);
            
            requestScope.seed(HttpServletRequest.class, request);
            requestScope.seed(ServletRequest.class, request);
            
            requestScope.seed(HttpSession.class, request.getSession(true));
            
            requestScope.seed(HttpServletResponse.class, response);
            requestScope.seed(ServletResponse.class, response);
            
            requestScope.seed(Key.get(String.class, Path.class), path);
            
            requestScope.seed(Key.get(String.class, WelcomeFile.class), "index");
            
            ArrayList<NamedValue> parameters = new ArrayList<NamedValue>();
            
            for (Map.Entry<String, String[]> entry : Request.getParameterMap(request).entrySet())
            {
                for (String value : entry.getValue())
                {
                    parameters.add(new NamedValue(NamedValue.REQUEST, entry.getKey(), value));
                }
            }
            
            requestScope.seed(Parameters.class, new Parameters(parameters));
            
            requestScope.seed(JanitorQueue.class, new JanitorQueue(requestJanitors));
            
            requestScope.seed(Key.get(new TypeLiteral<Map<Object, Object>>() { }, Faults.class), new HashMap<Object, Object>());
        }
    }


    /**
     * Enter the controller scope creating a controller. Returns the Throwable
     * thrown by the controller constructor if any. This unorthodox return value
     * distinguishes an exception raised in the controller constructor from an
     * exception raised during the creation of other objects in the method. That
     * is, it is an explicit return of an exception raised during construction,
     * that may be processed by the error handling of the web application.
     * 
     * @param scope
     *            The controller scope.
     * @param injector
     *            The Guice injector.
     * @param controllerClass
     *            The class of the controller to create.
     * @param mappings
     *            The map of parameter bindings for the controller.
     * @return The exception raised by controller, if any.
     */
    public Throwable enterController(BasicScope scope, Injector injector, Class<?> controllerClass, Map<String, String> mappings)
    {
        scope.enter();
        
        List<NamedValue> parameters = new ArrayList<NamedValue>();
        for (Map.Entry<String, String> entry : mappings.entrySet())
        {
            parameters.add(new NamedValue(NamedValue.DOVETAIL, entry.getKey(), entry.getValue()));
        }
        
        for (NamedValue namedValue : injector.getInstance(Parameters.class))
        {
            parameters.add(namedValue);
        }
        
        scope.seed(Key.get(new TypeLiteral<List<NamedValue>>() {}, Controller.class), parameters);
        scope.seed(Key.get(Parameters.class, Controller.class), new Parameters(parameters));
        
        try
        {
            scope.seed(Key.get(Object.class, Controller.class), injector.getInstance(controllerClass));
        }
        catch (ProvisionException e)
        {
            if (e.getCause() != null)
            {
                return e.getCause();
            }
            throw e;
        }
        
        return null;
    }
}
