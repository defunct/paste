package com.goodworkalan.paste.bean;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.goodworkalan.paste.NamedValue;
import com.goodworkalan.paste.Parameters;

public class BeanActorTest
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
        BeanActor actor = new BeanActor(new Parameters(parameters));
        Widget widget = new Widget();
        actor.actUpon(widget);
    }
}
