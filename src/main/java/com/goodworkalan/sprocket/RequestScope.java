package com.goodworkalan.sprocket;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;

// TODO Document.
public class RequestScope extends BasicScope
{
    // TODO Document.
    public void init(HttpServletRequest request, HttpServletResponse response)
    {
        enter();

        seed(HttpServletRequest.class, request);
        seed(ServletRequest.class, request);
            
        seed(HttpServletResponse.class, response);
        seed(ServletResponse.class, response);
        
        seed(Key.get(new TypeLiteral<Map<String, String[]>>() {}, RequestParameters.class), getParameterMap(request));
    }
    
    // TODO Document.
    @SuppressWarnings("unchecked")
    private Map<String, String[]> getParameterMap(HttpServletRequest request)
    {
        return request.getParameterMap();
    }
}
