/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */

package org.truelicense.core.misc;

import java.util.Locale;

/**
 * Provides static functions for nullable strings.
 * This class is trivially immutable.
 *
 * @author Christian Schlichtherle
 */
public class Strings {

    public static boolean equalsIgnoreCase(String a, String b) {
        return null == a ? null == b : a.equalsIgnoreCase(b);
    }

    public static String toLowerCase(String s, Locale l) {
        return null == s ? null : s.toLowerCase(l);
    }

    public static String toUpperCase(String s, Locale l) {
        return null == s ? null : s.toUpperCase(l);
    }

    private Strings() { }
}
