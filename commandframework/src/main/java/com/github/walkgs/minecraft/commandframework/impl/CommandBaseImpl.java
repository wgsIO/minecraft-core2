package com.github.walkgs.minecraft.commandframework.impl;

import com.github.walkgs.cojt.cojut.Applicable;
import com.github.walkgs.cojt.cojut.properties.Properties;
import com.github.walkgs.minecraft.commandframework.CommandBase;
import com.github.walkgs.minecraft.commandframework.CommandConfiguration;
import com.github.walkgs.minecraft.commandframework.reflect.MethodInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
class CommandBaseImpl implements CommandBase, Applicable<CommandBaseImpl> {

    private final String[] names;
    private final String[] permissions;

    private final MethodInfo[] worns;
    private final MethodInfo[] normals;

    private final String description;

    private final CommandConfiguration configuration;

    private final Properties properties;

    @Setter(AccessLevel.PROTECTED)
    private String usage;

}
