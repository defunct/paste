package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * A list of named values that can be queried and converted into different
 * maps.
 * 
 * @author Alan Gutierrez
 */
public class NamedValueList implements Iterable<NamedValue>
{
    /** The list of named values. */
    private final List<NamedValue> namedValues;
    
    /**
     * Create an empty named value list.
     */
    public NamedValueList()
    {
        this(new ArrayList<NamedValue>());
    }

    /**
     * Create a named value list from the given list of named values.
     * 
     * @param namedValues
     *            The list of named values.
     */
    public NamedValueList(List<NamedValue> namedValues)
    {
        this.namedValues = namedValues;
    }
    
    /**
     * Return an iterator over the named values.
     * 
     * @return An iterator over the named values.
     */
    public Iterator<NamedValue> iterator()
    {
        return namedValues.iterator();
    }

    /**
     * Returns the number of elements in this named value list.
     * 
     * @return The size of the named value list.
     */
    public int size()
    {
        return namedValues.size();
    }

    /**
     * Create a named value list reordered so that the named values given
     * contexts precede all other named values in the list. A new named value
     * list is created with the named values in the contexts given as the head
     * of list. The reordered named values are grouped by their context in the
     * order in which the contexts are specified by the given contexts list. If
     * a context is specified two more times in the given context lists, the
     * context is copied once and subsequent instances are ignored.
     * 
     * @param contexts
     *            A list of contexts in the order in which they are to be
     *            prepended to the a copy of the named value list.
     * @return A named value list with the named values in the given contexts at
     *         the head of the list.
     */
    public NamedValueList reorder(NamedValue.Context...contexts)
    {
        Set<NamedValue.Context> seen = new HashSet<NamedValue.Context>();
        List<NamedValue> reordered = new ArrayList<NamedValue>();
        for (NamedValue.Context context : contexts)
        {
            if (!seen.contains(context))
            {
                seen.add(context);
                for (NamedValue namedValue : namedValues)
                {
                    if (namedValue.getContext() == context)
                    {
                        reordered.add(namedValue);
                    }
                }
            }
        }
        for (NamedValue namedValue : namedValues)
        {
            if (!seen.contains(namedValue.getContext()))
            {
                reordered.add(namedValue);
            }
        }
        return new NamedValueList(reordered);
    }
    
    public NamedValueList include(NamedValue.Context...contexts)
    {
        List<NamedValue> included = new ArrayList<NamedValue>();
        Set<NamedValue.Context> include = new HashSet<NamedValue.Context>(Arrays.asList(contexts));
        for (NamedValue namedValue : namedValues)
        {
            if (include.contains(namedValue.getContext()))
            {
                included.add(namedValue);
            }
        }
        return new NamedValueList(included);
    }
    
    public NamedValueList exclude(NamedValue.Context...contexts)
    {
        List<NamedValue> excluded = new ArrayList<NamedValue>();
        Set<NamedValue.Context> exclude = new HashSet<NamedValue.Context>(Arrays.asList(contexts));
        for (NamedValue namedValue : namedValues)
        {
            if (!exclude.contains(namedValue.getContext()))
            {
                excluded.add(namedValue);
            }
        }
        return new NamedValueList(excluded);
    }

    /**
     * Convert the named value list into a map of names to of lists of values.
     * Named values that share the same name will have their values appended to
     * the string list indexed by the named value name.
     * 
     * @return A map of names to of lists of values.
     */
    public LinkedHashMap<String, List<String>> toMap()
    {
        LinkedHashMap<String, List<String>> map = new LinkedHashMap<String, List<String>>();
        for (NamedValue namedValue : namedValues)
        {
            List<String> values = map.get(namedValue.getName());
            if (values == null)
            {
                values = new ArrayList<String>();
                map.put(namedValue.getName(), values);
            }
            values.add(namedValue.getValue());
        }
        return map;
    }

    /**
     * Return the value of the first named value that matches the given name or
     * null if no named value matches the given name.
     * 
     * @param name
     *            The value name.
     * @return The first value found or null.
     */
    public String getFirst(String name)
    {
        for (NamedValue namedValue : namedValues)
        {
            if (namedValue.getName().equals(name))
            {
                return namedValue.getValue();
            }
        }
        return null;
    }

    /**
     * Return true if a named value with the given name exists in the named
     * value list.
     * 
     * @param name
     *            The value name to find.
     * @return True if a named value with the given name exists in the named
     *         value list.
     */
    public boolean hasName(String name)
    {
        for (NamedValue namedValue : namedValues)
        {
            if (namedValue.getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Return a set of the names that exist in the named value list.
     * 
     * @return The set of names in the named value set.
     */
    public Set<String> getNames()
    {
        Set<String> names = new HashSet<String>();
        for (NamedValue namedValue : namedValues)
        {
            names.add(namedValue.getName());
        }
        return names;
    }
    
    /**
     * Return a hash code that combines the hash code each named value in the
     * list.
     * 
     * @return The hash code.
     */
    public int hashCode()
    {
        return namedValues.hashCode();
    }

    /**
     * A named value list is equal to another named value object list whose of
     * the same length whose named values at each position in the list are equal
     * to the named values in this list.
     * 
     * @param object
     *            An object to which to compare this object.
     * @return True if the given object is equal to this named value list.
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof NamedValueList)
        {
            NamedValueList namedValueList = (NamedValueList) object;
            return namedValues.equals(namedValueList.namedValues);
        }
        return false;
    }
}
