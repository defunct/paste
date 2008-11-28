package com.goodworkalan.guicelet;

import com.goodworkalan.dovetail.GlobCompiler;
import com.goodworkalan.dovetail.GlobTree;

public class ControllerBinder
{
    private final GlobTree<ControllerBinding> treeOfBindings;
    
    private final GlobCompiler compiler;
    
    public ControllerBinder(GlobCompiler compiler)
    {
        this.treeOfBindings = new GlobTree<ControllerBinding>();
        this.compiler = compiler;
    }
    
    public ControllerPathBinder bind(String pattern)
    {
        return new ControllerPathBinder(this, treeOfBindings, compiler.compile(pattern));
    }
    
    public GlobTree<ControllerBinding> getGlobTree()
    {
        return treeOfBindings;
    }
}