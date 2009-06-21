package com.goodworkalan.paste;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.util.NamedValue;
import com.google.inject.Inject;

/**
 * Must be controller scoped so that the correct servlet response is used, one
 * reset for a subsequent servlet response.
 * 
 * @author Alan Gutierrez
 */
@ControllerScoped
public class Responder
{
    private final HttpServletResponse servletResponse;
    
    @Inject
    public Responder(HttpServletResponse servletResponse)
    {
        this.servletResponse = servletResponse;
    }
    
    public void send(Response response)
    {
        servletResponse.setStatus(response.getStatus());
        for (NamedValue namedValue : response.getHeaders())
        {
            if (namedValue.getName().toLowerCase().equals("content-type"))
            {
                servletResponse.setContentType(namedValue.getValue());
            }
            else if (namedValue.getName().toLowerCase().equals("content-length"))
            {
                servletResponse.setContentLength(Integer.parseInt(namedValue.getValue()));
            }
            else
            {
                servletResponse.addHeader(namedValue.getName(), namedValue.getValue());
            }
        }
    }
    
    public Writer getWriter() throws IOException
    {
        return servletResponse.getWriter();
    }
    
    public OutputStream getOutputStream() throws IOException
    {
        return servletResponse.getOutputStream();
    }
}
