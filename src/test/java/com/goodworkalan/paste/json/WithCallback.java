package com.goodworkalan.paste.json;

/**
 * It was simpiler to derive from {@link WithoutCallback} and set the callback
 * value than to try to set the callback value in the test.
 * 
 * @author Alan Gutierrez
 */
public class WithCallback extends WithoutCallback {
    /**
     * Construct a controller that has a callback method name assigned.
     */
    public WithCallback() {
        callback = "invoke";
    }
}
