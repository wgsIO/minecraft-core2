package com.github.walkgs.minecraft.commandframework.impl.extractor;

import com.github.walkgs.minecraft.commandframework.annotation.Command;
import com.github.walkgs.minecraft.commandframework.annotation.Normal;
import com.github.walkgs.minecraft.commandframework.annotation.Worn;
import com.github.walkgs.minecraft.commandframework.reflect.*;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodExtractorImpl implements MethodExtractor {

    private static final ParameterExtractor PARAMETER_EXTRACTOR = new ParameterExtractorImpl();

    @Override
    public MethodInfo[] extract(Class<?> clazz) {
        return extract(clazz, true);
    }

    @Override
    public MethodInfo[] extract(Object instance) {
        return extract(instance, true);
    }

    @Override
    public MethodInfo[] extract(Class<?> clazz, boolean ignoreErrors) {
        MethodInfo[] infoArray = new MethodInfo[0];
        final Method[] methods = collectQualifiedMethods(clazz.getMethods());
        for (Method method : methods) {
            final int index = infoArray.length;
            final MethodInfo info = extract(method, ignoreErrors);
            if (info == null)
                continue;
            infoArray = Arrays.copyOf(infoArray, index + 1);
            infoArray[index] = info;
        }
        return infoArray;
    }

    @Override
    public MethodInfo[] extract(Object instance, boolean ignoreErrors) {
        return extract(instance.getClass(), ignoreErrors);
    }

    @Override
    public MethodInfo extract(Method method) {
        return extract(method, true);
    }

    @Override
    public MethodInfo extract(Method method, boolean ignoreErrors) {
        final ParameterInfo[] parameters = PARAMETER_EXTRACTOR.extract(method, ignoreErrors);
        if (parameters.length < method.getParameters().length) {
            if (ignoreErrors)
                return null;
            throw new IllegalStateException("There is something wrong with the method parameters");
        }
        method.setAccessible(true);
        return new MethodInfoImpl(method, parameters);
    }

    private Method[] collectQualifiedMethods(Method[] methods) {
        Method[] methodsArray = new Method[0];
        for (Method method : methods) {
            final int index = methodsArray.length;
            if (!checkMethod(method))
                continue;
            methodsArray = Arrays.copyOf(methodsArray, index + 1);
            methodsArray[index] = method;
        }
        return methodsArray;
    }

    private boolean checkMethod(Method method) {
        return method.isAnnotationPresent(Worn.class) || method.isAnnotationPresent(Normal.class) || method.isAnnotationPresent(Command.class);
    }

}
