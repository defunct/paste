package com.goodworkalan.paste.paths;

import com.goodworkalan.paste.Controller;
import com.goodworkalan.paste.Criteria;
import com.goodworkalan.paste.Filter;
import com.google.inject.Injector;
import com.google.inject.Key;


// TODO Document.
public class FormatArguments
{
    // TODO Document.
    public final static FormatArgument REQUEST_PATH = new FormatArgument()
    {
        public Object getArgument(Injector injector)
        {
            return injector.getInstance(Key.get(Criteria.class, Filter.class)).getPath();
        }
    };
    
    // TODO Document.
    public final static FormatArgument REQUEST_DIRECTORY_NAME = new FormatArgument()
    {
        public Object getArgument(Injector injector)
        {
            String path = injector.getInstance(Key.get(Criteria.class, Filter.class)).getPath();
            if (path.length() == 0)
            {
                return "";
            }
            return path.substring(0, path.lastIndexOf('/'));
        }
    };

    // TODO Document.
    public final static FormatArgument REQUEST_FILE_NAME = new FormatArgument()
    {
        public Object getArgument(Injector injector)
        {
            String path = injector.getInstance(Key.get(Criteria.class, Filter.class)).getPath();
            if (path.length() < 2)
            {
//                return injector.getInstance(Key.get(String.class, WelcomeFile.class));
            }
            int toothpick = path.lastIndexOf('/');
            if (toothpick != -1 && toothpick + 1 < path.length())
            {
                return path.substring(toothpick + 1);
            }
            return "index";
        }
    };
    
    // TODO Document.
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

    // TODO Document.
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

    // TODO Document.
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
