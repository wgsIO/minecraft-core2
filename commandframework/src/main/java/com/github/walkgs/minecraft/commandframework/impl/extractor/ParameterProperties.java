package com.github.walkgs.minecraft.commandframework.impl.extractor;

import lombok.Data;

@Data
public class ParameterProperties {

    private final Class<?>[] types;
    private final int argAt;

    private final boolean auto;
    private final boolean optional;
    private final boolean sender;
    private final boolean text;

}
