package com.goodworkalan.guicelet;

import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.dovetail.GlobCompiler;
import com.goodworkalan.dovetail.GlobTree;

public class Binder
{
    private final List<ControllerBinder> listOfControllerBinders;
    
    private final List<ViewBinder> listOfViewBinders;
    
    public Binder()
    {
        this.listOfControllerBinders = new ArrayList<ControllerBinder>();
        this.listOfViewBinders = new ArrayList<ViewBinder>();
    }

    public ControllerBinder controllers(Class<?> conditional)
    {
        ControllerBinder globBuilder = new ControllerBinder(new GlobCompiler(conditional));
        listOfControllerBinders.add(globBuilder);
        return globBuilder;
    }
    
    public ViewBinder views(Class<?> conditional)
    {
        ViewBinder views = new ViewBinder(new GlobCompiler(conditional));
        listOfViewBinders.add(views);
        return views;
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
    
    public List<GlobTree<ViewBinding>> getViewBindings()
    {
        List<GlobTree<ViewBinding>> viewBindings = new ArrayList<GlobTree<ViewBinding>>();
        for (ViewBinder binder : listOfViewBinders)
        {
            viewBindings.add(binder.getGlobTree());
        }
        return viewBindings;
    }
}
