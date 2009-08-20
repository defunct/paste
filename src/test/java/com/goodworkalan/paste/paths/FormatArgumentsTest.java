package com.goodworkalan.paste.paths;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.goodworkalan.paste.Controller;
import com.goodworkalan.paste.paths.PathFormatter;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class FormatArgumentsTest
{
    private Class<?>[] args(Class<?>...formatArguments)
    {
        return formatArguments;
    }

    @Test
    public void ControllerClassAsPath()
    {
        Injector injector = Guice.createInjector(new AbstractModule()
        {
            @Override
            protected void configure()
            {
                bind(Object.class).annotatedWith(Controller.class).toInstance(new Object());
            }
        });

        PathFormatter formatter = new PathFormatter(injector);
        assertEquals(formatter.format("/%s.ftl", args(ControllerClassAsPath.class)), "/java/lang/Object.ftl");
    }

    @Test
    public void ControllerPackageAsPath()
    {
        Injector injector = Guice.createInjector(new AbstractModule()
        {
            @Override
            protected void configure()
            {
                bind(Object.class).annotatedWith(Controller.class).toInstance(new Object());
            }
        });

        PathFormatter formatter = new PathFormatter(injector);
        assertEquals(formatter.format("/%s.ftl", args(ControllerPackageAsPath.class)), "/java/lang.ftl");
    }

    @Test
    public void ControllerClassName()
    {
        Injector injector = Guice.createInjector(new AbstractModule()
        {
            @Override
            protected void configure()
            {
                bind(Object.class).annotatedWith(Controller.class).toInstance(new Object());
            }
        });

        PathFormatter formatter = new PathFormatter(injector);
        assertEquals(formatter.format("/%s.ftl", args(ControllerClassName.class)), "/Object.ftl");
    }
}
