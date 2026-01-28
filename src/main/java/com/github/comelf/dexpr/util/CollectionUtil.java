package com.github.comelf.dexpr.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class CollectionUtil {
    public static double sum(List p) {
        double d = 0;
        for (Object o : p) {
            if (o instanceof Number) {
                d += ((Number) o).doubleValue();
            }
        }
        return d;
    }

    public static double sum(Map p) {
        double d = 0;
        for (Object o : p.values()) {
            if (o instanceof Number) {
                d += ((Number) o).doubleValue();
            }
        }
        return d;
    }

    public static double sumArr(Object p) {
        double d = 0;
        int size = Array.getLength(p);
        for (int i = 0; i < size; i++) {
            Object o = Array.get(p, i);
            if (o instanceof Number) {
                d += ((Number) o).doubleValue();
            }
        }
        return d;
    }
}
