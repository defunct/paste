package com.goodworkalan.paste.connector;

import org.testng.annotations.Test;

import com.goodworkalan.paste.servlet.Cassette;

// TODO Document.
public class BinderTest {
    // TODO Document.
    @Test
    public void constructor() {
        new Connector(new Cassette());
    }

    // TODO Document.
    @Test
    public void anyController() {
//        CoreConnector connector = new CoreConnector();
//        connector.render().with(Forward.class);
//        RuleMap<Pair<Integer, RenderModule>> bindings = connector.getViewRules();
//        List<Pair<Integer, RenderModule>> found = bindings.test().get();
//        assertEquals(found.size(), 1);
//        assertEquals((int) Tuple.get1(found.get(0)), 0);
//        assertTrue(Tuple.get2(found.get(0)) instanceof Forward);
    }
    
    // TODO Document.
    public Object[] path(Object... objects) {
        return objects;
    }

    // TODO Document.
    @Test
    public void controllers() {
        // CoreConnector connector = new CoreConnector();
//        connector
//            .render()
//            .controller(Date.class).controller(String.class)
//            .with(Forward.class);
//        RuleMap<Pair<Integer, RenderModule>> bindings = connector.getViewRules();
//        List<Pair<Integer, RenderModule>> found = bindings.test().put(BindKey.CONTROLLER_CLASS, new Date()).get();
//        assertEquals(found.size(), 1);
//        assertEquals((int) Tuple.get1(found.get(0)), 0);
//        assertTrue(Tuple.get2(found.get(0)) instanceof Forward);
//        found =  bindings.test().put(BindKey.CONTROLLER_CLASS, 1).get();
//        assertEquals(found.size(), 0);
    }
}