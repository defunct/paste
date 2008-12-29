package com.goodworkalan.guicelet;

import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.diverge.RuleMap;
import com.goodworkalan.diverge.RuleMapBuilder;
import com.goodworkalan.dovetail.GlobCompiler;
import com.goodworkalan.dovetail.GlobTree;
import com.google.inject.Injector;

public class CoreBinder implements Binder
{
    private final List<ControllerBinder> listOfControllerBinders;
    
    private final RuleMapBuilder<ViewBinding> viewBindings;
    
    public CoreBinder()
    {
        this.listOfControllerBinders = new ArrayList<ControllerBinder>();
        this.viewBindings = new RuleMapBuilder<ViewBinding>();
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
    
    public RuleMap<ViewBinding> getViewBindings()
    {
        return viewBindings.newRuleMap();
    }
    
    public GuiceletGuicer newGuiceletGuicer(Injector injector)
    {
        return new GuiceletGuicer(injector, getBindingTrees(), getViewBindings());
    }
}
