package com.goodworkalan.paste.servlet;

import java.util.Enumeration;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.paste.controller.NamedValue;
import com.goodworkalan.paste.controller.Parameters;

public class EnumeratedParametersProvider implements Provider<Parameters> {
    private final HttpServletRequest request;
    
    @Inject
    public EnumeratedParametersProvider(HttpServletRequest request) {
        this.request = request;
    }

    public Parameters get() {
        Parameters parameters = new Parameters();
        Enumeration<?> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement().toString();
            for (String value : request.getParameterValues(name)) {
                parameters.add(new NamedValue(name, value));
            }
        }
        return new Parameters();
    }
}
