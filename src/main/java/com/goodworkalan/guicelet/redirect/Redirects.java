package com.goodworkalan.guicelet.redirect;

public class Redirects
{
    public static boolean isRedirectStatus(int status)
    {
        switch (status)
        {
        case 300:
        case 301:
        case 302:
        case 303:
        case 307:
            return true;
        default:
            return false;
        }
    }
}
