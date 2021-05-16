package com.github.walkgs.minecraft.commandframework.impl.extractor;

import com.github.walkgs.cojt.cojut.properties.Properties;
import com.github.walkgs.minecraft.commandframework.annotation.*;
import com.github.walkgs.minecraft.commandframework.reflect.ParameterExtractor;
import com.github.walkgs.minecraft.commandframework.reflect.ParameterInfo;
import com.github.walkgs.minecraft.commandframework.reflect.ParameterInfoImpl;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ParameterExtractorImpl implements ParameterExtractor {

    public static final String PROPERTIES_NAME = "$prop@command";
    public static final int PROPERTIES_INDEX = 0;

    @Override
    public ParameterInfo[] extract(Method method) {
        return extract(method, true);
    }

    @Override
    public ParameterInfo[] extract(Method method, boolean ignoreErrors) {
        if (!checkMethod(method))
            throw new IllegalStateException("The method is not a valid function for the command");
        final Parameter[] parameters = method.getParameters();
        final ParameterInfo[] infoArray = new ParameterInfo[parameters.length];
        for (int index = 0, last = 0; index < parameters.length; index++) {
            final Parameter parameter = parameters[index];
            final Temp<ParameterProperties, Boolean> temp = collectProperties(parameter, last, ignoreErrors);
            if (temp == null)
                continue;
            final ParameterProperties properties = temp.one;
            final ParameterInfoImpl parameterInfo = new ParameterInfoImpl(parameter);
            final Properties props = parameterInfo.getProperties();
            final Properties.DataSet<Object> dataSet = props.get(PROPERTIES_NAME);
            dataSet.set(PROPERTIES_INDEX, properties);
            props.save(dataSet);
            infoArray[index] = parameterInfo;
            if (temp.two)
                last++;
        }
        return infoArray;
    }

    private Temp<ParameterProperties, Boolean> collectProperties(Parameter parameter, int index, boolean ignoreErrors) {
        //Collect all qualifiers
        final AnyArgs anyArgs = parameter.getAnnotation(AnyArgs.class);
        final ArgAt argAt = parameter.getAnnotation(ArgAt.class);
        final Auto auto = parameter.getAnnotation(Auto.class);
        final Optional optional = parameter.getAnnotation(Optional.class);
        final Sender sender = parameter.getAnnotation(Sender.class);
        final Text text = parameter.getAnnotation(Text.class);

        //Creating properties
        final Class<?>[] types = anyArgs != null ? anyArgs.types() : new Class[]{parameter.getType()};
        final int argIndex = argAt != null ? argAt.at() : index;

        final boolean isAuto = auto != null;
        final boolean isOptional = optional != null;
        final boolean isSender = sender != null;
        final boolean isText = text != null;

        if (isAuto && isMultipleCombination(isAuto, isOptional, isSender, isText)) {
            if (ignoreErrors)
                return null;
            throw new IllegalStateException("The parameter starts automatically and duplicate identifiers were found");
        }
        if (isMultipleCombination(isText, isSender)) {
            if (ignoreErrors)
                return null;
            throw new IllegalStateException("In the parameter, duplicate identifiers were found");
        }

        return new Temp<>(new ParameterProperties(types, argIndex, isAuto, isOptional, isSender, isText), !(isAuto || isSender));
    }

    private boolean isMultipleCombination(boolean... props) {
        boolean result = false;
        for (boolean prop : props) {
            if (result && prop)
                return true;
            result = prop || result;
        }
        return false;
    }

    private boolean checkMethod(Method method) {
        return method.isAnnotationPresent(Worn.class) || method.isAnnotationPresent(Normal.class) || method.isAnnotationPresent(Command.class);
    }

    @Data
    class Temp<A, B> {

        private final A one;
        private final B two;

    }

}
