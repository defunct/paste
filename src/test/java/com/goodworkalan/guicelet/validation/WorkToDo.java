package com.goodworkalan.guicelet.validation;

import com.goodworkalan.guicelet.Actors;
import com.sun.tools.javac.util.List;

@Actors(ValidationActor.class)
public class WorkToDo
{
    public WorkToDo()
    {
    }
    
    @Required(property={
        @Property(foreach="this[*]", path="widget.string"),
        @Property(foreach="this[*].stringMapMap[*][*]", path="class")
    })
    public void setWidgets(List<Widget> widgets)
    {
    }
    
    @Required(property=@Property(path="widget.string"))
    public void setWidget(Widget widget)
    {
    }

    @Required(property=@Property(foreach="widget", path="string"))
    public void setAltWidget(Widget widget)
    {
    }
    
    @Required(property=@Property(foreach="this", path="string"))
    public void setDefaultWidget(Widget widget)
    {
    }

    @Required
    public void setString(String string)
    {
    }
}
