package com.goodworkalan.guicelet;

import com.goodworkalan.dovetail.GlobCompiler;
import com.goodworkalan.dovetail.GlobTree;

public class ViewBinder
{
    private final GlobTree<ViewBinding> treeOfBindings;
    
    private final GlobCompiler compiler;
    
    public ViewBinder(GlobCompiler compiler)
    {
        this.treeOfBindings = new GlobTree<ViewBinding>();
        this.compiler = compiler;
    }

    public ViewPathBinder bind(String pattern)
    {
        return new ViewPathBinder(this, treeOfBindings, compiler.compile(pattern));
    }
    
    public GlobTree<ViewBinding> getGlobTree()
    {
        return treeOfBindings;
    }
}
