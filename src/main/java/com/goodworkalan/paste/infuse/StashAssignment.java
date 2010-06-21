package com.goodworkalan.paste.infuse;

import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.stash.Stash;

// TODO Document.
public class StashAssignment<T> {
    // TODO Document.
    private final Stash.Key stashKey;
    
    // TODO Document.
    private final Ilk<T> ilk;
    
    // TODO Document.
    public StashAssignment(Stash.Key stashKey, Ilk<T> ilk) {
        this.stashKey = stashKey;
        this.ilk = ilk;
    }
    
    // TODO Document.
    public void assign(Injector injector, Stash stash) {
        stash.put(stashKey, ilk, injector.instance(ilk, null));
    }
}
