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

/**
 * Unit tests for the {@link NamedValueList} class.
 *
 * @author Alan Gutierrez
 */
public class NamedValueListTest {
    /** Test add. */
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

    /** Test get first. */
    @Test
    public void getFirst() {
        List<NamedValue> list = new ArrayList<NamedValue>();
        list.add(new NamedValue("hello", "world"));
        NamedValueList map = new NamedValueList(list);
        assertEquals(map.get("hello"), "world");
        assertNull(map.get("world"));
        map.clear();
        assertNull(map.get("hello"));
    }

    /** Test has name. */
    @Test
    public void hasName() {
        List<NamedValue> list = new ArrayList<NamedValue>();
        list.add(new NamedValue("hello", "world"));
        NamedValueList namedValue = new NamedValueList(list);
        assertTrue(namedValue.contains("hello"));
        assertFalse(namedValue.contains("world"));
    }

    /** Test iterable. */
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

    /** Test get names. */
    @Test
    public void getNames() {
        List<NamedValue> list = new ArrayList<NamedValue>();
        list.add(new NamedValue("hello", "world"));
        NamedValueList namedValues = new NamedValueList(list);
        for (String name : namedValues.getNames()) {
            assertEquals(name, "hello");
        }
    }

    /** Test size. */
    @Test
    public void size() {
        List<NamedValue> list = new ArrayList<NamedValue>();
        list.add(new NamedValue("hello", "world"));
        list.add(new NamedValue("hello", "nurse"));
        NamedValueList namedValues = new NamedValueList(list);
        assertEquals(namedValues.size(), 2);
    }

    /** Test equals. */
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

    /** Test hash. */
    @Test
    public void hash() {
        new NamedValueList(new ArrayList<NamedValue>()).hashCode();
    }
}
