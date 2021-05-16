package com.github.walkgs.minecraft.commandframework.reflect;

import com.github.walkgs.cojt.cojut.properties.Properties;

import java.lang.reflect.Parameter;

public interface ParameterInfo {

    Parameter getParameter();

    Properties getProperties();

}
