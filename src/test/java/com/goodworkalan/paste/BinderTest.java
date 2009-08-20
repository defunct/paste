package com.goodworkalan.paste;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.testng.annotations.Test;

import com.goodworkalan.deviate.RuleMap;
import com.goodworkalan.paste.forward.Forward;
import com.mallardsoft.tuple.Pair;
import com.mallardsoft.tuple.Tuple;

public class BinderTest
{
    @Test
    public void constructor()
    {
        new CoreConnector();
    }

    @Test
    public void anyController()
    {
        CoreConnector connector = new CoreConnector();
        connector.render().with(Forward.class);
        RuleMap<Pair<Integer, RenderModule>> bindings = connector.getViewRules();
        List<Pair<Integer, RenderModule>> found = bindings.test().get();
        assertEquals(found.size(), 1);
        assertEquals((int) Tuple.get1(found.get(0)), 0);
        assertTrue(Tuple.get2(found.get(0)) instanceof Forward);
    }
    
    public Object[] path(Object...objects)
    {     
        return objects;
    }

    @Test
    public void controllers()
    {
        CoreConnector connector = new CoreConnector();
        connector
            .render()
            .controller(Date.class).controller(String.class)
            .with(Forward.class);
        RuleMap<Pair<Integer, RenderModule>> bindings = connector.getViewRules();
        List<Pair<Integer, RenderModule>> found = bindings.test().put(BindKey.CONTROLLER_CLASS, new Date()).get();
        assertEquals(found.size(), 1);
        assertEquals((int) Tuple.get1(found.get(0)), 0);
        assertTrue(Tuple.get2(found.get(0)) instanceof Forward);
        found =  bindings.test().put(BindKey.CONTROLLER_CLASS, (Integer) 1).get();
        assertEquals(found.size(), 0);
    }
}
