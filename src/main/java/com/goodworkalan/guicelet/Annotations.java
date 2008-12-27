package com.goodworkalan.guicelet;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

public class Annotations
{
    private final HttpServletRequest request;
    
    private final Map<Class<? extends Annotation>, Parameters> parameters;
    
    @Inject
    public Annotations(@RequestScoped Map<Class<? extends Annotation>, Parameters> parameters,
                       HttpServletRequest request)
    {
        this.parameters = parameters;
        this.request = request;
    }

    public boolean invoke(String[] on, String param, String[] methods)
    {
        Parameters merged = Parameters.merge(parameters);
        boolean audit = on.length == 0;
        if (!audit)
        {
            if (!"".equals(param))
            {
                String value = merged.getFirst(param);
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
                    audit = parameters.containsKey(on[i]);
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
