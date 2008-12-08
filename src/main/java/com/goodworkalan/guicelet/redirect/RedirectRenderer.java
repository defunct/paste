package com.goodworkalan.guicelet.redirect;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.ServletException;

import com.goodworkalan.guicelet.Headers;
import com.goodworkalan.guicelet.Renderer;
import com.goodworkalan.guicelet.Transfer;

public class RedirectRenderer implements Renderer
{
    private final Transfer transfer;
    
    private final Configuration configuration;
    
    public RedirectRenderer(
            Transfer transfer,
            Configuration configuration)
    {
        this.transfer = transfer;
        this.configuration = configuration;
    }

    public void render() throws ServletException, IOException
    {
        Redirector redirector = transfer.getRedirector();

        if (configuration.getFormat() != null)
        {
            Object[] args = new String[configuration.getFormatArguments().length];
            for (int i = 0; i < args.length; i++){
                args[i] = configuration.getFormatArguments()[i].getArgument(transfer);
            }
            String where = String.format(configuration.getFormat(), args);
            redirector.redirect(where);
        }

        Headers headers = transfer.getRequestHeaders();
        if (!headers.contains("Location"))
        {
            Object controller = transfer.getController();
            SuggestedRedirection suggestedRedirection = controller.getClass().getAnnotation(SuggestedRedirection.class);
            if (suggestedRedirection != null)
            {
                redirector.redirect(suggestedRedirection.value());
            }
        }
        
        if (headers.getStatus() == 0)
        {
            headers.setStatus(configuration.getStatus());
        }
        headers.send(transfer.getHttpServletResponse());
 
        String page = 
            String.format(getPageFormat(),
                          headers.getStatus(), headers.get("Location"));
        transfer.getHttpServletResponse().getWriter().append(page);
    }
    
    private String getPageFormat() throws IOException
    {
        Reader reader = new InputStreamReader(RedirectRenderer.class.getResourceAsStream("redirect.html"));
        StringBuilder newString = new StringBuilder();
        char[] buffer = new char[2048];
        int read;
        while ((read = reader.read(buffer)) != -1)
        {
            newString.append(buffer, 0, read);
        }
        return newString.toString();
    }
}
