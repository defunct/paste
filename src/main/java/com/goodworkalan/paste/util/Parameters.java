package com.goodworkalan.paste.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


public class Parameters extends NamedValueList
{
    public Parameters(List<NamedValue> namedValues)
    {
        super(namedValues);
    }
    
    public String toQueryString()
    {
        StringBuilder queryString = new StringBuilder();
        String separator = "";
        for (NamedValue namedValue : this)
        {
            queryString.append(separator);
            try
            {
                queryString.append(URLEncoder.encode(namedValue.getName(), "UTF-8"));
                queryString.append("=");
                if (namedValue.getValue() != null)
                {
                    queryString.append(URLEncoder.encode(namedValue.getValue(), "UTF-8"));
                }
            }
            catch (UnsupportedEncodingException e)
            {
            }
            separator = "&";
        }
        return queryString.toString();
    }
}
