package com.goodworkalan.paste.infuse;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.testng.annotations.Test;

import com.goodworkalan.infuse.CollectionFactory;
import com.goodworkalan.infuse.DefaultConstructorFactory;
import com.goodworkalan.infuse.ObjectFactory;
import com.goodworkalan.paste.NamedValue;
import com.goodworkalan.paste.Parameters;

public class InfusionActorTest
{
    @Test
    public void constructor()
    {
        List<NamedValue> parameters = new ArrayList<NamedValue>();
        parameters.add(new NamedValue(NamedValue.REQUEST, "string['bar']['baz']", "foo"));
        parameters.add(new NamedValue(NamedValue.REQUEST, "string", "foo"));
        parameters.add(new NamedValue(NamedValue.REQUEST, "-bar", "foo"));
//        parameters.put("bar", new ArrayList<String>());
        parameters.add(new NamedValue(NamedValue.REQUEST, "bar.baz", "foo"));
        Set<ObjectFactory> factories = new LinkedHashSet<ObjectFactory>();
        factories.add(new CollectionFactory());
        factories.add(new DefaultConstructorFactory());
        InfusionActor actor = new InfusionActor(new Parameters(parameters), factories);
        Widget widget = new Widget();
        actor.actUpon(widget);
    }
}
