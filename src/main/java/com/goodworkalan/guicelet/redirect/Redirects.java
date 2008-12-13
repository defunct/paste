package com.goodworkalan.guicelet.redirect;

public class Redirects
{
    public static boolean isRedirectStatus(int status)
    {
        return (status >= 300 && status <= 303) || status == 307;
    }
}
