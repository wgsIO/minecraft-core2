package com.github.walkgs.minecraft.commandframework.reflect;

import com.github.walkgs.cojt.cojut.properties.Properties;

import java.lang.reflect.Method;

public interface MethodInfo {

    Method getMethod();

    ParameterInfo[] getParameters();

    Properties getProperties();

}
