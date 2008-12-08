package com.goodworkalan.guicelet;

import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.deviate.Deviations;
import com.goodworkalan.dovetail.GlobCompiler;
import com.goodworkalan.dovetail.GlobTree;

public class Binder
{
    private final List<ControllerBinder> listOfControllerBinders;
    
    private final Deviations<ViewBinding> viewBindings;
    
    public Binder()
    {
        this.listOfControllerBinders = new ArrayList<ControllerBinder>();
        this.viewBindings = new Deviations<ViewBinding>(PatternKey.values().length);
    }

    public ControllerBinder controllers(Class<?> conditional)
    {
        ControllerBinder globBuilder = new ControllerBinder(new GlobCompiler(conditional));
        listOfControllerBinders.add(globBuilder);
        return globBuilder;
    }
    
    public ViewBinder view()
    {
        return new ViewBinder(viewBindings);
    }
    
    public List<GlobTree<ControllerBinding>> getBindingTrees()
    {
        List<GlobTree<ControllerBinding>> controllerBindings = new ArrayList<GlobTree<ControllerBinding>>();
        for (ControllerBinder binder : listOfControllerBinders)
        {
            controllerBindings.add(binder.getGlobTree());
        }
        return controllerBindings;
    }
    
    public Deviations<ViewBinding> getViewBindings()
    {
        return viewBindings;
    }
}
