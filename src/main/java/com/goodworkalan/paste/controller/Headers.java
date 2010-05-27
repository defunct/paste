package com.goodworkalan.paste.controller;


public class Headers extends NamedValueList {
    /** The default serial version id. */
    private static final long serialVersionUID = 1L;

    public Headers(NamedValueList...namedValueLists) {
        super(catenate(namedValueLists));
    }
}
