package com.goodworkalan.paste.servlet;

import java.util.Enumeration;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.paste.controller.NamedValue;
import com.goodworkalan.paste.controller.Parameters;

// TODO Document.
public class EnumeratedParametersProvider implements Provider<Parameters> {
    // TODO Document.
    private final HttpServletRequest request;
    
    // TODO Document.
    @Inject
    public EnumeratedParametersProvider(HttpServletRequest request) {
        this.request = request;
    }

    // TODO Document.
    public Parameters get() {
        Parameters parameters = new Parameters();
        Enumeration<?> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement().toString();
            for (String value : request.getParameterValues(name)) {
                parameters.add(new NamedValue(name, value));
            }
        }
        return parameters;
    }
}
