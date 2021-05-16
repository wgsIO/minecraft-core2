package com.github.walkgs.minecraft.commandframework;

import org.bukkit.plugin.Plugin;

public interface CommandExtractor {

    CommandContainer extract(Plugin plugin, Class<?> clazz, Object... args) throws Exception;

    CommandContainer extract(Plugin plugin, Object instance);

    CommandContainer extract(Plugin plugin, boolean ignoreErrors, Class<?> clazz, Object... args) throws Exception;

    CommandContainer extract(Plugin plugin, Object instance, boolean ignoreErrors);

}
