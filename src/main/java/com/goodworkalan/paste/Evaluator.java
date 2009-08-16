package com.goodworkalan.paste;

import com.goodworkalan.infuse.Diffusion;
import com.goodworkalan.infuse.PathException;

/**
 * Extract values from an object using diffusion paths.
 * 
 * @author Alan Gutierrez
 */
public class Evaluator
{
    /** The object to diffuse. */
    private final Object object;

    /**
     * Create a new evaluator that extracts values from the given object.
     * 
     * @param object
     *            The object to diffuse.
     */
    public Evaluator(Object object)
    {
        this.object = object;
    }

    /**
     * Extract a value from the object property using the given object path
     * expression.
     * 
     * @param expression
     *            The object path expression.
     * @return The object value as a string or null if any part of the path does
     *         not exist.
     */
    public String get(String expression)
    {
        try
        {
            Diffusion diffusion = new Diffusion(object);
            Object object = diffusion.get(expression);
            return object == null ? null : object.toString();
        }
        catch (PathException e)
        {
            throw new PasteException(0, e);
        }
    }
}
