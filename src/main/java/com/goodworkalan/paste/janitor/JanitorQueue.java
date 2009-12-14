package com.goodworkalan.paste.janitor;

import java.util.Collection;

// TODO Document.
public class JanitorQueue {
    // TODO Document.
    private final Collection<Janitor> janitors;

    // TODO Document.
    public JanitorQueue(Collection<Janitor> janitors) {
        this.janitors = janitors;
    }

    // TODO Document.
    public void add(Janitor janitor) {
        janitors.add(janitor);
    }
}
