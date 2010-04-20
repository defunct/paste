package com.goodworkalan.paste.infuse;

import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.stash.Stash;
import com.google.inject.Injector;
import com.google.inject.Key;

public class StashAssignment<T> {
    private final Stash.Key stashKey;
    
    private final Key<T> guiceKey;
    
    private final Ilk<T> ilk;
    
    public StashAssignment(Stash.Key stashKey, Class<T> keyClass) {
        this(stashKey, keyClass, Key.get(keyClass));
    }

    public StashAssignment(Stash.Key stashKey, Class<T> keyClass, Key<T> guiceKey) {
        this.stashKey = stashKey;
        this.ilk = new Ilk<T>(keyClass);
        this.guiceKey = guiceKey;
    }
    
    public void assign(Injector injector, Stash stash) {
        stash.put(stashKey, ilk, injector.getInstance(guiceKey));
    }
}
