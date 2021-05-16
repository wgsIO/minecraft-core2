package com.github.walkgs.minecraft.commandframework.adapters.java;

import com.github.walkgs.minecraft.commandframework.Defaults;
import com.github.walkgs.minecraft.commandframework.adapters.Adapter;
import com.github.walkgs.minecraft.commandframework.config.LanguageMap;
import com.github.walkgs.minecraft.commandframework.reflect.ParameterInfo;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class BooleanAdapter implements Adapter<Boolean> {

    @Override
    public String getType(ParameterInfo info) {
        return Defaults.DEFAULT_LANGUAGE.findFor(LanguageMap.ADAPTER_VALUE_NAME);
    }

    @Override
    public Boolean adapt(CommandSender sender, ParameterInfo parameterInfo, String value) {
        return Optional.ofNullable(value).map(Boolean::parseBoolean).orElse(null);
    }

}