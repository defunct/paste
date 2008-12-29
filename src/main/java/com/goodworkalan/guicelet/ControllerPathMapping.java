package com.goodworkalan.guicelet;

import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.diverge.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;

public class ControllerPathMapping
{
    public final List<Glob> listOfGlobs = new ArrayList<Glob>();
    
    public final RuleMapBuilder<ControllerBinding> rules = new RuleMapBuilder<ControllerBinding>();
}
