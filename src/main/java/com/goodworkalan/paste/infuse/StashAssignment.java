package com.goodworkalan.paste.infuse;

import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.stash.Stash;

public class StashAssignment<T> {
    private final Stash.Key stashKey;
    
    private final Ilk<T> ilk;
    
    public StashAssignment(Stash.Key stashKey, Ilk<T> ilk) {
        this.stashKey = stashKey;
        this.ilk = ilk;
    }
    
    public void assign(Injector injector, Stash stash) {
        stash.put(stashKey, ilk, injector.instance(ilk, null));
    }
}
