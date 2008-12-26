package com.goodworkalan.guicelet.faults;

import static com.goodworkalan.dspl.Patterns.glob;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goodworkalan.dspl.PathException;
import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.guicelet.GuiceletException;

public class FaultMessages
{
    private final Object controller;
    
    private final ResourceBundle bundle;
    
    public FaultMessages(Object controller)
    {
        this.controller = controller;
        this.bundle = findResourceBundle(controller);
    }
    
    private static ResourceBundle findResourceBundle(Object controller)
    {
        String packageName = controller.getClass().getPackage().getName();
        String baseName = packageName + ".faults";
        return ResourceBundle.getBundle(baseName, 
                Locale.getDefault(), controller.getClass().getClassLoader());
    }
    
    public String getMessage(String propertyPath, String key) 
    {
        String className = controller.getClass().getCanonicalName();
        try
        {
            return bundle.getString(className + "." + propertyPath + "." + key);
        }
        catch (MissingResourceException e)
        {
        }
        try
        {
            return bundle.getString(className + "." + key);
        }
        catch (MissingResourceException e)
        {
        }
        try
        {
            return bundle.getString(key);
        }
        catch (MissingResourceException e)
        {
            return "No error message provided.";
        }
    }
    
    private final Pattern MESSAGE = Pattern.compile(
            "\\s*\\(\\s*" + glob() + "(?:\\s*,\\s*" + glob() + ")\\s*)\\s*(.*)"
            );

    private final Pattern GLOB = Pattern.compile(
        "\\s*(" + glob() + ")\\s*,?"
        );

    public String format(String stem, String key, Map<Object, Object> map)
    {
        String format = getMessage(stem, key);

        List<Object> arguments = new ArrayList<Object>();

        Matcher message = MESSAGE.matcher(format);
        if (message.matches())
        {
            Matcher glob = GLOB.matcher(message.group(1));
            while (glob.find())
            {
                try
                {
                    PropertyPath path = new PropertyPath(glob.group(1));
                    arguments.add(path.get(map));
                }
                catch (PathException e)
                {
                    throw new GuiceletException(e);
                }
            }
            return String.format(message.group(2), arguments.toArray());
        }
        else
        {
            return format;
        }
    }
}
