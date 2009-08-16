package com.goodworkalan.paste.util;

/**
 * A name value pair with an attached context.
 * 
 * @author Alan Gutierrez
 */
public class NamedValue
{
    /** Context for named values created from the HTTP request. */
    public final static NamedValue.Context REQUEST = new NamedValue.Context();

    /**
     * Context for named values created by Dovetail matches against the URL
     * path.
     */
    public final static NamedValue.Context CONTROLLER = new NamedValue.Context();
    
    /** Context for named values created for HTTP response. */
    public final static NamedValue.Context RESPONSE = new NamedValue.Context();

    /** The context in which the named value was created. */
    private final Context context;
    
    /** The name of the value. */
    private final String name;
    
    /** The value. */
    private final String value;

    /**
     * Create a named value with the given name and given value that was created
     * in the given context.
     * 
     * @param context
     *            The context in which the named value was created.
     * @param name
     *            The name of the value.
     * @param value
     *            The value.
     */
    public NamedValue(Context context, String name, String value)
    {
        this.context = context;
        this.name = name;
        this.value = value;
    }

    /**
     * Get the context in which the named value was created.
     * 
     * @return The context in which the named value was created.
     */
    public Object getContext()
    {
        return context;
    }

    /**
     * Get the name of the value.
     * 
     * @return The name of the value.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the value.
     * 
     * @return The value.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * A named value is equal to another named value object with the same
     * context flag property, when the name properties and the value properties
     * are equal.
     * 
     * @param object
     *            An object to which to compare this object.
     * @return True if the given object is equal to this literal.
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof NamedValue)
        {
            NamedValue namedValue = (NamedValue) object;
            return context == namedValue.context
                && name.equals(namedValue.name)
                && value.equals(namedValue.value);
        }
        return false;
    }
    
    /**
     * Return a hash code that combines the hash code of the context property,
     * name property and value property.
     * 
     * @return The hash code.
     */
    @Override
    public int hashCode()
    {
        int hash = 1;
        hash = hash * 37 + context.hashCode();
        hash = hash * 37 + name.hashCode();
        hash = hash * 37 + value.hashCode();
        return hash;
    }
    
    /**
     * Return a string representation of the named value.
     * 
     * @return A string representation.
     */
    @Override
    public String toString()
    {
        return "{" + name + "=" + "\"" + value + "\"}";
    }

    /**
     * A flag to with identity equality used to identify named value contexts.
     * 
     * @author Alan Gutierrez
     */
    public final static class Context
    {
    }
}
