package com.goodworkalan.paste.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A list of named values that can be queried and converted into different maps.
 * 
 * @author Alan Gutierrez
 */
public class NamedValueList extends ArrayList<NamedValue> {
    /** The serial version id. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a named value list from the given list of named values.
     * 
     * @param namedValues
     *            The list of named values.
     */
    public NamedValueList(List<NamedValue> namedValues) {
        super(namedValues);
    }
    
    public NamedValueList(Map<String, String> map) {
        this(fromMap(map));
    }

    /**
     * Construct a name value list from the given query string.
     * 
     * @param queryString
     *            The query string.
     */
    public NamedValueList(String queryString) {
        this(fromQueryString(queryString, "UTF-8"));
    }
    
    protected static List<NamedValue> fromMap(Map<String, String> map) {
        List<NamedValue> namedValues = new ArrayList<NamedValue>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            namedValues.add(new NamedValue(entry.getKey(), entry.getValue()));
        }
        return namedValues;
    }
    
    protected static List<NamedValue> catenate(NamedValueList...namedValueLists) {
        List<NamedValue> namedValues = new ArrayList<NamedValue>();
        for (NamedValueList namedValueList : namedValueLists) {
            namedValues.addAll(namedValueList);
        }
        return namedValues;
    }
    
    /**
     * Create a parameter list from the given query string placing the named
     * values in the given context.
     * 
     * @param query
     *            The query string to parse.
     * @param context
     *            The context in which the parameter was specified.
     * @return A list of parameters contained in the query string.
     */
    static List<NamedValue> fromQueryString(String query, String encoding) {
        try {
            String[] parameters = query.split("&");
            List<NamedValue> namedValues = new ArrayList<NamedValue>(parameters.length);
            for (String parameter : parameters) {
                String[] pair = parameter.split("=", 2);
                String name = URLDecoder.decode(pair[0], encoding);
                String value = "";
                if (pair.length == 2) {
                    value = URLDecoder.decode(pair[1], encoding);
                }
                namedValues.add(new NamedValue(name, value));
            }
            return namedValues;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert the named value list into a map of names to of lists of values.
     * Named values that share the same name will have their values appended to
     * the string list indexed by the named value name.
     * 
     * @return A map of names to of lists of values.
     */
    public LinkedHashMap<String, List<String>> toStringListMap() {
        LinkedHashMap<String, List<String>> map = new LinkedHashMap<String, List<String>>();
        for (NamedValue namedValue : this) {
            List<String> values = map.get(namedValue.getName());
            if (values == null) {
                values = new ArrayList<String>();
                map.put(namedValue.getName(), values);
            }
            values.add(namedValue.getValue());
        }
        return map;
    }
    
    public LinkedHashMap<String, String> toStringMap(boolean spaceIsNull) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        for (NamedValue namedValue : this) {
            String value = namedValue.getValue();
            if (spaceIsNull && (namedValue.getValue() == null || namedValue.getValue().trim().length() == 0)) {
                value = null;
            }
            map.put(namedValue.getName(), value);
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
    public NamedValue get(String name) {
        for (NamedValue namedValue : this) {
            if (namedValue.getName().equals(name)) {
                return namedValue;
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
    public boolean contains(String name) {
        for (NamedValue namedValue : this) {
            if (namedValue.getName().equals(name)) {
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
    public Set<String> getNames() {
        Set<String> names = new LinkedHashSet<String>();
        for (NamedValue namedValue : this) {
            names.add(namedValue.getName());
        }
        return names;
    }
    
    /**
     * Create a URL encoded query string from the list of named values.
     * 
     * @return A URL encoded query string.
     */
    public String toQueryString() {
        StringBuilder queryString = new StringBuilder();
        String separator = "";
        for (NamedValue namedValue : this) {
            queryString.append(separator);
            try {
                queryString.append(URLEncoder.encode(namedValue.getName(),
                        "UTF-8"));
                queryString.append("=");
                if (namedValue.getValue() != null) {
                    queryString.append(URLEncoder.encode(namedValue.getValue(), "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
            }
            separator = "&";
        }
        return queryString.toString();
    }
}
