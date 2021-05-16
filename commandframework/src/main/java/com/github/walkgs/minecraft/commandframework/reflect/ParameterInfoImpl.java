package com.github.walkgs.minecraft.commandframework.reflect;

import com.github.walkgs.cojt.cojut.properties.LinkedProperties;
import com.github.walkgs.cojt.cojut.properties.Properties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Parameter;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ParameterInfoImpl implements ParameterInfo {

    private final Parameter parameter;
    private final Properties properties;

    public ParameterInfoImpl(Parameter parameter) {
        this(parameter, new LinkedProperties());
    }

}
