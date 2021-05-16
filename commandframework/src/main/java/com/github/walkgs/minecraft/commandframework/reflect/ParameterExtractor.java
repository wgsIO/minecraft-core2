package com.github.walkgs.minecraft.commandframework.reflect;

import java.lang.reflect.Method;

public interface ParameterExtractor {

    ParameterInfo[] extract(Method method);

    ParameterInfo[] extract(Method method, boolean ignoreErrors);

}
