package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.controller.Parameters;

public class Including {
    @Inject
    public Including(Parameters parameters, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "/included";
        if ("true".equals(parameters.get("qs"))) {
            path += "?a=1&b=2";
        }
        request.getRequestDispatcher(path).include(request, response);
    }
}
