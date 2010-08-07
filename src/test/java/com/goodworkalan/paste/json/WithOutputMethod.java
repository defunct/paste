package com.goodworkalan.paste.json;

import java.util.HashMap;
import java.util.Map;

import com.goodworkalan.paste.stream.Output;

public class WithOutputMethod {
    @SuppressWarnings("serial")
    @Output
    public Map<Object, Object> map() {
        return new HashMap<Object, Object>() {{
            put("a", 1);
        }};
    }
}
