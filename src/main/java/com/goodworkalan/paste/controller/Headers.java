package com.goodworkalan.paste.controller;

// TODO Document.
public class Headers extends NamedValueList {
    /** The default serial version id. */
    private static final long serialVersionUID = 1L;

    // TODO Document.
    public Headers(NamedValueList...namedValueLists) {
        super(catenate(namedValueLists));
    }
}
