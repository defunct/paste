package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;

// TODO Document.
public class ControllerPathMapping
{
    // TODO Document.
    public final List<Glob> listOfGlobs = new ArrayList<Glob>();
    
    // TODO Document.
    public final RuleMapBuilder<ControllerBinding> rules = new RuleMapBuilder<ControllerBinding>();
}
