package com.github.walkgs.minecraft.commandframework.impl;

import com.github.walkgs.minecraft.commandframework.CommandConfiguration;
import lombok.Data;

@Data
class CommandConfigurationImpl implements CommandConfiguration {

    private final boolean alwaysConsole;

}
