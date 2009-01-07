package com.goodworkalan.guicelet.paths;

import static com.goodworkalan.guicelet.paths.FormatArguments.CONTROLLER_CLASS_AS_PATH;
import static com.goodworkalan.guicelet.paths.FormatArguments.CONTROLLER_CLASS_NAME;
import static com.goodworkalan.guicelet.paths.FormatArguments.CONTROLLER_PACKAGE_AS_PATH;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.goodworkalan.guicelet.Controller;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class FormatArgumentsTest
{
    private FormatArgument[] args(FormatArgument...formatArguments)
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
        assertEquals(formatter.format("/%s.ftl", args(CONTROLLER_CLASS_AS_PATH)), "/java/lang/Object.ftl");
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
        assertEquals(formatter.format("/%s.ftl", args(CONTROLLER_PACKAGE_AS_PATH)), "/java/lang.ftl");
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
        assertEquals(formatter.format("/%s.ftl", args(CONTROLLER_CLASS_NAME)), "/Object.ftl");
    }
}
