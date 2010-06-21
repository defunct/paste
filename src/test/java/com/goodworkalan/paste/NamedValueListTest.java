package com.goodworkalan.paste;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.goodworkalan.paste.controller.NamedValue;
import com.goodworkalan.paste.controller.NamedValueList;

// TODO Document.
public class NamedValueListTest {
    // TODO Document.
    @Test
    public void add() {
        List<NamedValue> list = new ArrayList<NamedValue>();
        list.add(new NamedValue("hello", "world"));
        NamedValueList namedValues = new NamedValueList(list);
        assertEquals(namedValues.size(), 1);
        for (Map.Entry<String, List<String>> entry : namedValues
                .toStringListMap().entrySet()) {
            assertEquals(entry.getKey(), "hello");
            for (String value : entry.getValue()) {
                assertEquals(value, "world");
            }
        }
    }

    // TODO Document.
    @Test
    public void getFirst() {
        List<NamedValue> list = new ArrayList<NamedValue>();
        list.add(new NamedValue("hello", "world"));
        NamedValueList map = new NamedValueList(list);
        assertEquals(map.get("hello"), "world");
        assertNull(map.get("world"));
        list.clear();
        assertNull(map.get("hello"));
    }

    // TODO Document.
    @Test
    public void hasName() {
        List<NamedValue> list = new ArrayList<NamedValue>();
        list.add(new NamedValue("hello", "world"));
        NamedValueList namedValue = new NamedValueList(list);
        assertTrue(namedValue.contains("hello"));
        assertFalse(namedValue.contains("world"));
    }

    // TODO Document.
    @Test
    public void iterable() {
        List<NamedValue> list = new ArrayList<NamedValue>();
        list.add(new NamedValue("hello", "world"));
        NamedValueList namedValues = new NamedValueList(list);
        for (NamedValue namedValue : namedValues) {
            assertEquals(namedValue.getName(), "hello");
            assertEquals(namedValue.getValue(), "world");
        }
    }

    // TODO Document.
    @Test
    public void getNames() {
        List<NamedValue> list = new ArrayList<NamedValue>();
        list.add(new NamedValue("hello", "world"));
        NamedValueList namedValues = new NamedValueList(list);
        for (String name : namedValues.getNames()) {
            assertEquals(name, "hello");
        }
    }

    // TODO Document.
    @Test
    public void size() {
        List<NamedValue> list = new ArrayList<NamedValue>();
        list.add(new NamedValue("hello", "world"));
        list.add(new NamedValue("hello", "nurse"));
        NamedValueList namedValues = new NamedValueList(list);
        assertEquals(namedValues.size(), 2);
    }

    // TODO Document.
    @Test
    public void equals() {
        List<NamedValue> listOne = new ArrayList<NamedValue>();
        NamedValueList one = new NamedValueList(listOne);
        assertTrue(one.equals(one));
        listOne.add(new NamedValue("hello", "world"));
        List<NamedValue> listTwo = new ArrayList<NamedValue>();
        NamedValueList two = new NamedValueList(listTwo);
        listTwo.add(new NamedValue("hello", "world"));
        assertTrue(one.equals(two));
        assertFalse(one.equals(null));
    }

    // TODO Document.
    @Test
    public void hash() {
        new NamedValueList(new ArrayList<NamedValue>()).hashCode();
    }
}
