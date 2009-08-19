package com.goodworkalan.paste;

import java.util.Enumeration;

/**
 * A collection of package specific casts to generic types for older Servlet
 * interfaces.
 * 
 * @author Alan Gutierrez
 */
class Objects {
    /**
     * Cast an enumeration to a string enumeration.
     * 
     * @param enumeration
     *            An enumeration.
     * @return A string enumeration.
     */
    @SuppressWarnings("unchecked")
    public static Enumeration<String> toStringEnumeration(Enumeration enumeration) {
        return enumeration;
    }
}
