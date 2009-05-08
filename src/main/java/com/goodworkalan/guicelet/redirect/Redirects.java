package com.goodworkalan.guicelet.redirect;

// TODO Document.
public class Redirects
{
    // TODO Document.
    public static boolean isRedirectStatus(int status)
    {
        return (status >= 300 && status <= 303) || status == 307;
    }
}
