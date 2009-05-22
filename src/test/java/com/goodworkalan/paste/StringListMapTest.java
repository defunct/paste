package com.goodworkalan.paste;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.goodworkalan.paste.StringListMap;

public class StringListMapTest
{
    @Test
    public void add()
    {
        StringListMap map = new StringListMap();
        map.add("hello", "world");
        assertEquals(map.entrySet().size(), 1);
        for (Map.Entry<String, List<String>> entry : map.entrySet())
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
        StringListMap map = new StringListMap();
        map.add("hello", "world");
        assertEquals(map.getFirst("hello"), "world");
        assertNull(map.getFirst("world"));
        map.get("hello").clear();
        assertNull(map.getFirst("hello"));
    }

    @Test
    public void put()
    {
        StringListMap map = new StringListMap();
        map.put("hello", Collections.singletonList("world"));
        assertEquals(map.getFirst("hello"), "world");
    }
    
    @Test
    public void putAll()
    {
        Map<String, List<String>> source = new HashMap<String, List<String>>();
        source.put("hello", Collections.singletonList("world"));
        StringListMap map = new StringListMap();
        map.putAll(source);
        assertEquals(map.getFirst("hello"), "world");
    }
    
    @Test
    public void containsKey()
    {
        StringListMap map = new StringListMap();
        map.put("hello", Collections.singletonList("world"));
        assertTrue(map.containsKey("hello"));
        assertFalse(map.containsKey("world"));
    }
    
    @Test
    public void containsValue()
    {
        StringListMap map = new StringListMap();
        map.put("hello", Collections.singletonList("world"));
        assertTrue(map.containsValue(Collections.singletonList("world")));
        assertFalse(map.containsValue(Collections.singletonList("hello")));
    }
    
    @Test
    public void entrySet()
    {
        StringListMap map = new StringListMap();
        map.put("hello", Collections.singletonList("world"));
        assertEquals(map.entrySet().size(), 1);
        for (Map.Entry<String, List<String>> entry : map.entrySet())
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
        StringListMap map = new StringListMap();
        map.put("hello", Collections.singletonList("world"));
        assertEquals(map.keySet().size(), 1);
        for (String key : map.keySet())
        {
            assertEquals(key, "hello");
        }   
    }
    
    @Test
    public void values()
    {
        StringListMap map = new StringListMap();
        map.put("hello", Collections.singletonList("world"));
        assertEquals(map.values().size(), 1);
        for (List<String> values : map.values())
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
        StringListMap map = new StringListMap();
        map.add("hello", "world");
        assertEquals(map.remove("hello"), Collections.singletonList("world"));
        assertNull(map.get("hello"));
    }
    
    @Test
    public void clear()
    {
        StringListMap map = new StringListMap();
        map.add("hello", "world");
        assertEquals(map.get("hello"), Collections.singletonList("world"));
        map.clear();
        assertNull(map.get("hello"));
    }
    
    @Test
    public void size()
    {
        StringListMap map = new StringListMap();
        map.add("hello", "world");
        map.add("hello", "nurse");
        assertEquals(map.size(), 1);
    }
    
    @Test
    public void isEmpty()
    {
        assertTrue(new StringListMap().isEmpty());
    }
    
    @Test
    public void equals()
    {
        StringListMap one = new StringListMap();
        assertTrue(one.equals(one));
        one.add("hello", "world");
        StringListMap two = new StringListMap();
        two.add("hello", "world");
        assertTrue(one.equals(two));
        assertFalse(one.equals(null));
    }
    
    @Test
    public void hash()
    {
        new StringListMap().hashCode();
    }
}
