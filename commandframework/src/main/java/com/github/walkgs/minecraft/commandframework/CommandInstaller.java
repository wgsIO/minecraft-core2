package com.github.walkgs.minecraft.commandframework;

import com.github.walkgs.minecraft.commandframework.adapters.AdapterBinder;
import com.github.walkgs.minecraft.commandframework.config.LanguageMap;
import org.bukkit.plugin.Plugin;

public interface CommandInstaller {

    LanguageMap getLanguage();

    AdapterBinder getAdapterBinder();

    CommandContainer install(Plugin plugin, Class<?> clazz, Object... args) throws Exception;

    CommandContainer install(Plugin plugin, Object instance);

    CommandContainer install(Plugin plugin, boolean ignoreErrors, Class<?> clazz, Object... args) throws Exception;

    CommandContainer install(Plugin plugin, Object instance, boolean ignoreErrors);

    void unistall(CommandContainer container);

}
