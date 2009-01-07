package com.goodworkalan.guicelet.paths;

import com.goodworkalan.guicelet.Controller;
import com.goodworkalan.guicelet.Path;
import com.goodworkalan.guicelet.WelcomeFile;
import com.google.inject.Injector;
import com.google.inject.Key;


public class FormatArguments
{
    public final static FormatArgument REQUEST_PATH = new FormatArgument()
    {
        public Object getArgument(Injector injector)
        {
            return injector.getInstance(Key.get(String.class, Path.class));
        }
    };
    
    public final static FormatArgument REQUEST_DIRECTORY_NAME = new FormatArgument()
    {
        public Object getArgument(Injector injector)
        {
            String path = injector.getInstance(Key.get(String.class, Path.class));
            if (path.length() == 0)
            {
                return "";
            }
            return path.substring(0, path.lastIndexOf('/'));
        }
    };

    public final static FormatArgument REQUEST_FILE_NAME = new FormatArgument()
    {
        public Object getArgument(Injector injector)
        {
            String path = injector.getInstance(Key.get(String.class, Path.class));
            if (path.length() < 2)
            {
                return injector.getInstance(Key.get(String.class, WelcomeFile.class));
            }
            int toothpick = path.lastIndexOf('/');
            if (toothpick != -1 && toothpick + 1 < path.length())
            {
                return path.substring(toothpick + 1);
            }
            return "index";
        }
    };
    
    public final static FormatArgument CONTROLLER_CLASS_AS_PATH = new FormatArgument()
    {
        public Object getArgument(Injector injector)
        {
            return injector.getInstance(Key.get(Object.class, Controller.class))
                           .getClass()
                           .getCanonicalName()
                           .replace('.', '/');
        }
    };

    public final static FormatArgument CONTROLLER_PACKAGE_AS_PATH = new FormatArgument()
    {
        public Object getArgument(Injector injector)
        {
            return injector.getInstance(Key.get(Object.class, Controller.class))
                           .getClass()
                           .getPackage()
                           .getName()
                           .replace('.', '/');
        }
    };

    public final static FormatArgument CONTROLLER_CLASS_NAME = new FormatArgument()
    {
        public Object getArgument(Injector injector)
        {
            Object controller = injector.getInstance(Key.get(Object.class, Controller.class));
            String name = controller.getClass().getCanonicalName();
            String pkg = controller.getClass().getPackage().getName();
            return name.substring(pkg.length() + 1).replace('.', '/');
        }
    };
}
