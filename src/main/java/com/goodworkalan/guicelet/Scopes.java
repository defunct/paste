package com.goodworkalan.guicelet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.guicelet.faults.Faults;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

public class Scopes
{
    public static void enterRequest(
                BasicScope scope,
                HttpServletRequest request, HttpServletResponse response,
                List<Janitor> requestJanitors)
    {
        scope.enter();
       
        scope.seed(HttpServletRequest.class, request);
        scope.seed(ServletRequest.class, request);
            
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
    
    @SuppressWarnings("unchecked")
    private static Map<String, String[]> getParameterMap(HttpServletRequest request)
    {
        return request.getParameterMap();
    }
    
    public static void enterController(BasicScope scope, Injector injector, Class<?> controllerClass, Parameters bindings)
    {
        scope.enter();

        ParametersServer parameters = injector.getInstance(ParametersServer.class);
        
        parameters.get(Parameters.BINDING).clear();
        parameters.get(Parameters.BINDING).putAll(bindings);

        scope.seed(Key.get(Parameters.class, Binding.class), parameters.get(Parameters.BINDING));
        scope.seed(Key.get(Object.class, Controller.class), injector.getInstance(controllerClass));
    }
}
