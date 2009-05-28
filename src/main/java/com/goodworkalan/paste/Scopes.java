package com.goodworkalan.paste;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
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
                HttpServletRequest request,
                HttpServletResponse response,
                List<Janitor> requestJanitors) throws IOException
    {
        scope.enter();
       
        scope.seed(ServletContext.class, request.getServletContext());
        
        scope.seed(HttpServletRequest.class, request);
        scope.seed(ServletRequest.class, request);
        
        scope.seed(HttpSession.class, request.getSession(true));
            
        scope.seed(HttpServletResponse.class, response);
        scope.seed(ServletResponse.class, response);
        
        String path = request.getRequestURI().substring(request.getContextPath().length());
        scope.seed(Key.get(String.class, Path.class), path);

        scope.seed(Key.get(String.class, WelcomeFile.class), "index");

        ArrayList<NamedValue> parameters = new ArrayList<NamedValue>();
        
        for (Map.Entry<String, String[]> entry : Request.getParameterMap(request).entrySet())
        {
            for (String value : entry.getValue())
            {
                parameters.add(new NamedValue(NamedValue.REQUEST, entry.getKey(), value));
            }
        }
        
        scope.seed(Parameters.class, new Parameters(parameters));

        scope.seed(JanitorQueue.class, new JanitorQueue(requestJanitors));
        
        scope.seed(Key.get(new TypeLiteral<Map<Object, Object>>() { }, Faults.class), new HashMap<Object, Object>());
        
        scope.seed(OutputStream.class, response.getOutputStream());
        scope.seed(Writer.class, response.getWriter());
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
    public static Throwable enterController(BasicScope scope, Injector injector, Class<?> controllerClass, Map<String, String> mappings)
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
