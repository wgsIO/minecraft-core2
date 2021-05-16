package com.github.walkgs.minecraft.commandframework.reflect;

import com.github.walkgs.cojt.cojut.properties.LinkedProperties;
import com.github.walkgs.cojt.cojut.properties.Properties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MethodInfoImpl implements MethodInfo {

    private final Method method;
    private final ParameterInfo[] parameters;
    private final Properties properties;

    public MethodInfoImpl(Method method, ParameterInfo[] parameters) {
        this(method, parameters, new LinkedProperties());
    }

}
