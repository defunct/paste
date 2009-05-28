package com.goodworkalan.paste;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

//TODO Document.
@ControllerScoped
public class Annotations
{
    // TODO Document.
    private final HttpServletRequest request;
    
    // TODO Document.
    private final NamedValueList parameters;
    
    // TODO Document.
    @Inject
    public Annotations(@Controller Parameters parameters, HttpServletRequest request)
    {
        this.parameters = parameters;
        this.request = request;
    }

    // TODO Document.
    public boolean invoke(String[] on, String param, String[] methods)
    {
        boolean audit = on.length == 0;
        if (!audit)
        {
            if (!"".equals(param))
            {
                String value = parameters.getFirst(param);
                if (value != null)
                {
                    for (int i = 0; !audit && i < on.length; i++)
                    {
                        audit = on[i].equals(value);
                    }
                }
            }
            else
            {
                for (int i = 0; !audit &&  i < on.length; i++)
                {
                    audit = parameters.hasName(on[i]);
                }
            }
        }
        if (audit)
        {
            if (methods.length != 0)
            {
                audit = false;
                for (int i = 0; !audit && i < methods.length; i++)
                {
                    audit = methods[i].equals(request.getMethod());
                }
            }
        }
        return audit;
    }
}
