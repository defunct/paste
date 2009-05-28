package com.goodworkalan.paste.faults;

import static com.goodworkalan.infuse.Patterns.glob;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goodworkalan.infuse.Diffusion;
import com.goodworkalan.infuse.PathException;
import com.goodworkalan.paste.PasteException;

// TODO Document.
public class FaultMessages
{
    // TODO Document.
    private final Object controller;
    
    // TODO Document.
    private final ResourceBundle bundle;
    
    // TODO Document.
    public FaultMessages(Object controller)
    {
        this.controller = controller;
        this.bundle = findResourceBundle(controller);
    }
    
    // TODO Document.
    private static ResourceBundle findResourceBundle(Object controller)
    {
        String packageName = controller.getClass().getPackage().getName();
        String baseName = packageName + ".faults";
        return ResourceBundle.getBundle(baseName, 
                Locale.getDefault(), controller.getClass().getClassLoader());
    }
    
    // TODO Document.
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
    
    // TODO Document.
    private final Pattern MESSAGE = Pattern.compile(
            "\\s*\\(\\s*" + glob() + "(?:\\s*,\\s*" + glob() + "\\s*)\\s*(.*)"
            );
    
    // TODO Document.
    private final Pattern GLOB = Pattern.compile(
        "\\s*(" + glob() + ")\\s*,?"
        );

    // TODO Document.
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
                    Diffusion path = new Diffusion(glob.group(1));
                    arguments.add(path.get(map));
                }
                catch (PathException e)
                {
                    throw new PasteException(e);
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
