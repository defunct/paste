package com.goodworkalan.sprocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.goodworkalan.sprocket.faults.Faults;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;

// TODO Document.
public class Scopes
{
    // TODO Document.
    public static void enterRequest(
                BasicScope scope,
                HttpServletRequest request, HttpServletResponse response,
                List<Janitor> requestJanitors)
    {
        scope.enter();
       
        scope.seed(HttpServletRequest.class, request);
        scope.seed(ServletRequest.class, request);
        
        scope.seed(HttpSession.class, request.getSession(true));
            
        scope.seed(HttpServletResponse.class, response);
        scope.seed(ServletResponse.class, response);
        
        scope.seed(Key.get(new TypeLiteral<Map<String, String[]>>() {}, RequestParameters.class), getParameterMap(request));

        String path = request.getRequestURI().substring(request.getContextPath().length());
        scope.seed(Key.get(String.class, Path.class), path);

        scope.seed(Key.get(String.class, WelcomeFile.class), "index");

        scope.seed(Key.get(Headers.class, Request.class), Headers.fromRequest(request));
        scope.seed(Key.get(Headers.class, Response.class), new Headers(request.getMethod()));
    
        ParametersServer parameters = new ParametersServer();
        parameters.get(Parameters.BINDING);
        parameters.get(Parameters.REQUEST).putAll(Parameters.fromStringArrayMap(getParameterMap(request)));
        scope.seed(ParametersServer.class, parameters);
        scope.seed(Key.get(Parameters.class, Binding.class), parameters.get(Parameters.BINDING));
        scope.seed(Key.get(Parameters.class, Request.class), parameters.get(Parameters.REQUEST));

        scope.seed(Key.get(JanitorQueue.class, Request.class), new JanitorQueue(requestJanitors));
        
        scope.seed(Key.get(new TypeLiteral<Map<Object, Object>>() { }, Faults.class), new HashMap<Object, Object>());
    }
    
    // TODO Document.
    @SuppressWarnings("unchecked")
    private static Map<String, String[]> getParameterMap(HttpServletRequest request)
    {
        return request.getParameterMap();
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
     * @param bindings
     *            The map of parameter bindings for the controller.
     * @return The exception raised by controller, if any.
     */
    public static Throwable enterController(BasicScope scope, Injector injector, Class<?> controllerClass, Parameters bindings)
    {
        scope.enter();

        ParametersServer parameters = injector.getInstance(ParametersServer.class);
        
        parameters.get(Parameters.BINDING).clear();
        parameters.get(Parameters.BINDING).putAll(bindings);
        
        scope.seed(Key.get(Parameters.class, Binding.class), parameters.get(Parameters.BINDING));

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
