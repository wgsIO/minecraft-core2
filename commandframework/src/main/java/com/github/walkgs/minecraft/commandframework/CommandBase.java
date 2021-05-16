package com.github.walkgs.minecraft.commandframework;

import com.github.walkgs.minecraft.commandframework.reflect.MethodInfo;

public interface CommandBase extends CommandIdentifier {

    MethodInfo[] getWorns();

    MethodInfo[] getNormals();

}
