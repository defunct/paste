package com.goodworkalan.guicelet.bean;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.goodworkalan.guicelet.Parameters;
import com.goodworkalan.guicelet.Request;

public class BeanActorTest
{
    @Test
    public void constructor()
    {
        Parameters parameters = new Parameters();
        parameters.add("string['bar']['baz']", "foo");
        parameters.add("string", "foo");
        parameters.add("-bar", "foo");
        parameters.put("bar", new ArrayList<String>());
        parameters.add("bar.baz", "foo");
        Map<Class<? extends Annotation>, Parameters> map = new HashMap<Class<? extends Annotation>, Parameters>();
        map.put(Request.class, parameters);
        BeanActor actor = new BeanActor(map);
        Widget widget = new Widget();
        actor.actUpon(widget);
    }
}
