package com.goodworkalan.paste.validate;


public @interface ValidatedBy
{
    public Class<? extends Validator> validator();
}
