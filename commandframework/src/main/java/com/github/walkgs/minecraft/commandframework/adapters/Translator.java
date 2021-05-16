package com.github.walkgs.minecraft.commandframework.adapters;

import java.util.HashMap;
import java.util.Map;

public interface Translator {

    Map<Class<?>, Class<?>> PRIMITIVE_MAP = new HashMap<Class<?>, Class<?>>() {{
        put(boolean.class, Boolean.class);
        put(byte.class, Byte.class);
        put(char.class, Character.class);
        put(double.class, Double.class);
        put(float.class, Float.class);
        put(int.class, Integer.class);
        put(long.class, Long.class);
        put(short.class, Short.class);
    }};

    static Class<?> translate(Class<?> type) {
        final Class<?> conversion = PRIMITIVE_MAP.get(type);
        return conversion != null ? conversion : type;
    }

}
