package com.goodworkalan.sprocket;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.goodworkalan.sprocket.Parameters;

public class ParametersTest
{
    @Test
    public void constructor()
    {
        new Parameters();
    }
    
    @Test
    public void fromStringArrayMap()
    {
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("hello", new String[] { "world" });
        Parameters parameters = Parameters.fromStringArrayMap(map);
        assertEquals(parameters.entrySet().size(), 1);
        for (Map.Entry<String, List<String>> entry : parameters.entrySet())
        {
            assertEquals(entry.getKey(), "hello");
            for (String value : entry.getValue())
            {
                assertEquals(value, "world");
            }
        }
    }
    
    @Test
    public void fromStringMap()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("hello", "world");
        Parameters parameters = Parameters.fromStringMap(map);
        assertEquals(parameters.entrySet().size(), 1);
        for (Map.Entry<String, List<String>> entry : parameters.entrySet())
        {
            assertEquals(entry.getKey(), "hello");
            for (String value : entry.getValue())
            {
                assertEquals(value, "world");
            }
        }
    }
    
    @Test
    public void merge()
    {
        Parameters one = new Parameters();
        one.add("a", "one");
        one.add("a", "one");
        one.add("b", "one");
        one.add("c", "one");
        Parameters two = new Parameters();
        two.add("a", "two");
        two.add("b", "two");
        two.add("d", "two");
        Parameters three = new Parameters();
        three.add("a", "three");
        Parameters merged = Parameters.merge(one, two, three);
        assertEquals(merged.size(), 4);
        assertEquals(merged.get("a").size(), 4);
        Iterator<String> values = merged.get("a").iterator();
        assertEquals(values.next(), "one");
        assertEquals(values.next(), "one");
        assertEquals(values.next(), "two");
        assertEquals(values.next(), "three");
        assertEquals(merged.get("b").size(), 2);
        values = merged.get("b").iterator();
        assertEquals(values.next(), "one");
        assertEquals(values.next(), "two");
        assertEquals(merged.get("c").size(), 1);
        values = merged.get("c").iterator();
        assertEquals(values.next(), "one");
    }
    
    @Test(expectedExceptions=UnsupportedOperationException.class)
    public void unmodifiable()
    {
        Parameters parameters = new Parameters();
        parameters.add("hello", "world");
        parameters.unmodifiable().get("hello").add("failure");
    }
}
