package com.goodworkalan.guicelet.paths;

import com.goodworkalan.guicelet.Transfer;

public class FormatArguments
{
    public final static FormatArgument REQUEST_PATH = new FormatArgument()
    {
        public Object getArgument(Transfer transfer)
        {
            return transfer.getPath();
        }
    };
    
    public final static FormatArgument REQUEST_DIRECTORY_NAME = new FormatArgument()
    {
        public Object getArgument(Transfer transfer)
        {
            String path = transfer.getPath();
            if (path.length() == 0)
            {
                return "";
            }
            return path.substring(0, path.lastIndexOf('/'));
        }
    };

    public final static FormatArgument REQUEST_FILE_NAME = new FormatArgument()
    {
        public Object getArgument(Transfer transfer)
        {
            String path = transfer.getPath();
            if (path.length() < 2)
            {
                return transfer.getWelcomeFile();
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
        public Object getArgument(Transfer transfer)
        {
            return transfer.getController()
                           .getClass()
                           .getCanonicalName()
                           .replace('.', '/');
        }
    };

    public final static FormatArgument CONTROLLER_PACKAGE_AS_PATH = new FormatArgument()
    {
        public Object getArgument(Transfer transfer)
        {
            return transfer.getController()
                           .getClass()
                           .getPackage()
                           .getName()
                           .replace('.', '/');
        }
    };

    public final static FormatArgument CONTROLLER_CLASS_NAME = new FormatArgument()
    {
        public Object getArgument(Transfer transfer)
        {
            Object controller = transfer.getController();
            String name = controller.getClass().getCanonicalName();
            String pkg = controller.getClass().getPackage().getName();
            return name.substring(pkg.length() + 1).replace('.', '/');
        }
    };
}
