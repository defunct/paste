package com.goodworkalan.paste.infuse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import com.goodworkalan.infuse.BasicObjectFactory;
import com.goodworkalan.infuse.ObjectFactory;
import com.goodworkalan.paste.NamedValue;
import com.goodworkalan.paste.Parameters;
import com.goodworkalan.paste.infuse.InfusionActor;

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
        InfusionActor actor = new InfusionActor(new Parameters(parameters), Collections.<ObjectFactory>singleton(new BasicObjectFactory()));
        Widget widget = new Widget();
        actor.actUpon(widget);
    }
}
