package com.goodworkalan.paste;

import org.testng.annotations.Test;

public class NullProviderTest {
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void get() {
        new NullProvider<Object>().get();
    }
}
