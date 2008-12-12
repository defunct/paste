package com.goodworkalan.guicelet;

import static org.testng.Assert.assertEquals; 
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNull; 
import static org.testng.Assert.assertFalse; 

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

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

    @Test
    public void add()
    {
        Parameters parameters = new Parameters();
        parameters.add("hello", "world");
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
    public void getFirst()
    {
        Parameters parameters = new Parameters();
        parameters.add("hello", "world");
        assertEquals(parameters.getFirst("hello"), "world");
        assertNull(parameters.getFirst("world"));
        parameters.get("hello").clear();
        assertNull(parameters.getFirst("hello"));
    }

    @Test
    public void put()
    {
        Parameters parameters = new Parameters();
        parameters.put("hello", Collections.singletonList("world"));
        assertEquals(parameters.getFirst("hello"), "world");
    }
    
    @Test
    public void putAll()
    {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("hello", Collections.singletonList("world"));
        Parameters parameters = new Parameters();
        parameters.putAll(map);
        assertEquals(parameters.getFirst("hello"), "world");
    }
    
    @Test
    public void containsKey()
    {
        Parameters parameters = new Parameters();
        parameters.put("hello", Collections.singletonList("world"));
        assertTrue(parameters.containsKey("hello"));
        assertFalse(parameters.containsKey("world"));
    }
    
    @Test
    public void containsValue()
    {
        Parameters parameters = new Parameters();
        parameters.put("hello", Collections.singletonList("world"));
        assertTrue(parameters.containsValue(Collections.singletonList("world")));
        assertFalse(parameters.containsValue(Collections.singletonList("hello")));
    }
    
    @Test
    public void entrySet()
    {
        Parameters parameters = new Parameters();
        parameters.put("hello", Collections.singletonList("world"));
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
    public void keySet()
    {
        Parameters parameters = new Parameters();
        parameters.put("hello", Collections.singletonList("world"));
        assertEquals(parameters.keySet().size(), 1);
        for (String key : parameters.keySet())
        {
            assertEquals(key, "hello");
        }   
    }
    
    @Test
    public void values()
    {
        Parameters parameters = new Parameters();
        parameters.put("hello", Collections.singletonList("world"));
        assertEquals(parameters.values().size(), 1);
        for (List<String> values : parameters.values())
        {
            for (String value : values)
            {
                assertEquals(value, "world");
            }
        }   
    }

    @Test
    public void remove()
    {
        Parameters parameters = new Parameters();
        parameters.add("hello", "world");
        assertEquals(parameters.remove("hello"), Collections.singletonList("world"));
        assertNull(parameters.get("hello"));
    }
    
    @Test
    public void clear()
    {
        Parameters parameters = new Parameters();
        parameters.add("hello", "world");
        assertEquals(parameters.get("hello"), Collections.singletonList("world"));
        parameters.clear();
        assertNull(parameters.get("hello"));
    }
    
    @Test
    public void size()
    {
        Parameters parameters = new Parameters();
        parameters.add("hello", "world");
        parameters.add("hello", "nurse");
        assertEquals(parameters.size(), 1);
    }
    
    @Test
    public void isEmpty()
    {
        assertTrue(new Parameters().isEmpty());
    }
    
    @Test
    public void equals()
    {
        Parameters one = new Parameters();
        assertTrue(one.equals(one));
        one.add("hello", "world");
        Parameters two = new Parameters();
        two.add("hello", "world");
        assertTrue(one.equals(two));
        assertFalse(one.equals(null));
    }
    
    @Test
    public void hash()
    {
        new Parameters().hashCode();
    }
}
