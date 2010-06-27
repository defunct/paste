package com.goodworkalan.paste;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.goodworkalan.paste.controller.JanitorQueue;

/**
 * Unit tests for the {@link JanitorQueue} class.
 *
 * @author Alan Gutierrez
 */
public class JanitorQueueTest {
    /** Test add. */
    @Test
    public void add() {
        List<Runnable> janitors = new ArrayList<Runnable>();
        JanitorQueue queue = new JanitorQueue(janitors);
        Runnable janitor = new Runnable() {
            public void run() {
            }
        };
        queue.add(janitor);
        assertEquals(janitors.size(), 1);
        assertSame(janitors.get(0), janitor);
    }
}
