package com.github.walkgs.minecraft.commandframework.adapters;

import com.github.walkgs.minecraft.commandframework.reflect.ParameterInfo;
import org.bukkit.command.CommandSender;

public interface Adapter<T> {

    String getType(ParameterInfo info);

    T adapt(CommandSender sender, ParameterInfo parameterInfo, String value);

}
