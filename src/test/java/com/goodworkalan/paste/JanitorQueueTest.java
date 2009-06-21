package com.goodworkalan.paste;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.goodworkalan.paste.janitor.Janitor;
import com.goodworkalan.paste.janitor.JanitorQueue;

public class JanitorQueueTest
{
    @Test
    public void add()
    {
        List<Janitor> janitors = new ArrayList<Janitor>();
        JanitorQueue queue = new JanitorQueue(janitors);
        Janitor janitor = new Janitor()
        {
            public void cleanUp()
            {
            }
        };
        queue.add(janitor);
        assertEquals(janitors.size(), 1);
        assertSame(janitors.get(0), janitor);
    }
}
