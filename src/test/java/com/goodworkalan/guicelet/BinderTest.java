package com.goodworkalan.guicelet;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.goodworkalan.deviate.Deviations;
import com.goodworkalan.guicelet.forward.Forward;

public class BinderTest
{
    @Test
    public void constructor()
    {
        new Binder();
    }

    @Test
    public void anyController()
    {
        Binder binder = new Binder();
        binder.view().controller().with(Forward.class);
        Deviations<ViewBinding> bindings = binder.getViewBindings();
        Object[] path = new Object[9];
        List<ViewBinding> found = bindings.get(path);
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getPriority(), 0);
        assertTrue(found.get(0).getModule() instanceof Forward);
    }
    
    public Object[] path(Object...objects)
    {     
        return objects;
    }

    @Test
    public void controllers()
    {
        Binder binder = new Binder();
        binder.view()
              .controller(Object.class).or(String.class)
              .with(Forward.class);
        Deviations<ViewBinding> bindings = binder.getViewBindings();
        Object[] path = path(null, Object.class, null, null, null, null, null, null, null);
        List<ViewBinding> found = bindings.get(path);
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getPriority(), 0);
        assertTrue(found.get(0).getModule() instanceof Forward);
        path = path(null, Integer.class, null, null, null, null, null, null, null);
        found = bindings.get(path);
        assertEquals(found.size(), 0);
    }
}
