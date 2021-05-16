package com.github.walkgs.minecraft.commandframework;

import com.github.walkgs.minecraft.commandframework.reflect.MethodInfo;

public interface CommandInfo extends CommandIdentifier {

    MethodInfo getMethod();

}
