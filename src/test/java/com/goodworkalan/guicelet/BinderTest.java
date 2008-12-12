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
        new CoreBinder();
    }

    @Test
    public void anyController()
    {
        CoreBinder binder = new CoreBinder();
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
        CoreBinder binder = new CoreBinder();
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
