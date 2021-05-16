package com.github.walkgs.minecraft.commandframework.adapters.java;

import com.github.walkgs.cojt.cojut.properties.Properties;
import com.github.walkgs.minecraft.commandframework.Defaults;
import com.github.walkgs.minecraft.commandframework.adapters.Adapter;
import com.github.walkgs.minecraft.commandframework.config.LanguageMap;
import com.github.walkgs.minecraft.commandframework.impl.extractor.ParameterExtractorImpl;
import com.github.walkgs.minecraft.commandframework.impl.extractor.ParameterProperties;
import com.github.walkgs.minecraft.commandframework.reflect.ParameterInfo;
import org.bukkit.command.CommandSender;

public class StringAdapter implements Adapter<String> {

    @Override
    public String getType(ParameterInfo info) {
        final Properties properties = info.getProperties();
        final Properties.DataSet<ParameterProperties> dataSet = properties.get(ParameterExtractorImpl.PROPERTIES_NAME);
        final ParameterProperties props = dataSet.get(ParameterExtractorImpl.PROPERTIES_INDEX);
        return props.isText() ? Defaults.DEFAULT_LANGUAGE.findFor(LanguageMap.ADAPTER_TEXT_NAME) : Defaults.DEFAULT_LANGUAGE.findFor(LanguageMap.ADAPTER_VALUE_NAME);
    }

    @Override
    public String adapt(CommandSender sender, ParameterInfo parameterInfo, String value) {
        return value;
    }

}
