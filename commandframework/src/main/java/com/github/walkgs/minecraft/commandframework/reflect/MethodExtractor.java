package com.github.walkgs.minecraft.commandframework.reflect;

import java.lang.reflect.Method;

public interface MethodExtractor {

    MethodInfo[] extract(Class<?> clazz);

    MethodInfo[] extract(Object instance);

    MethodInfo[] extract(Class<?> clazz, boolean ignoreErrors);

    MethodInfo[] extract(Object instance, boolean ignoreErrors);

    MethodInfo extract(Method method);

    MethodInfo extract(Method method, boolean ignoreErrors);

}
