package com.goodworkalan.guicelet;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.goodworkalan.diverge.RuleMap;
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
        binder.view().with(Forward.class);
        RuleMap<ViewBinding> bindings = binder.getMapOfRules();
        List<ViewBinding> found = bindings.test().get();
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
        RuleMap<ViewBinding> bindings = binder.getMapOfRules();
        List<ViewBinding> found = bindings.test().put(BindKey.CONTROLLER_CLASS, Object.class).get();
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getPriority(), 0);
        assertTrue(found.get(0).getModule() instanceof Forward);
        found =  bindings.test().put(BindKey.CONTROLLER_CLASS, Integer.class).get();
        assertEquals(found.size(), 0);
    }
}
