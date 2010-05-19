package com.goodworkalan.paste;

import com.goodworkalan.ilk.Ilk;

public interface Reactor {
    public <T> void react(Ilk<T> lik, T object);
}
