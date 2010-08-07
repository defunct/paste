package com.goodworkalan.paste.json;

import java.util.HashMap;
import java.util.Map;

import com.goodworkalan.paste.stream.Output;

/**
 * A dummy controler for testing.
 *
 * @author Alan Gutierrez
 */
@SuppressWarnings("serial")
public class WithoutCallback {
    /** The jSON callback method name. */
    public String callback;
    
    @Output
    public Map<Object, Object> map = new HashMap<Object, Object>() {{
        put("a", 1);
    }};
}
