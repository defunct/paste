package com.goodworkalan.sprocket.bean;

import java.util.ArrayList;

import org.testng.annotations.Test;

import com.goodworkalan.sprocket.Parameters;
import com.goodworkalan.sprocket.ParametersServer;
import com.goodworkalan.sprocket.bean.BeanActor;

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
        ParametersServer server = new ParametersServer();
        server.get(Parameters.REQUEST).putAll(parameters);
        BeanActor actor = new BeanActor(server);
        Widget widget = new Widget();
        actor.actUpon(widget);
    }
}
