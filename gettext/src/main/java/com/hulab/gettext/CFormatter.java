package com.hulab.gettext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nebneb on 11/04/2018.
 */

public class CFormatter {

    private static final HashMap<String, String> injections = new HashMap<String, String>(){{put("%i", "%d");}};

    static String format(String string) {
        if (string == null) return null;
        for (Map.Entry<String, String> entry : injections.entrySet())
            string = string.replaceAll(entry.getKey(), entry.getValue());
        return string;
    }
}
