package com.github.walkgs.minecraft.commandframework.impl;

import com.github.walkgs.minecraft.commandframework.*;
import com.github.walkgs.minecraft.commandframework.adapters.AdapterBinder;
import com.github.walkgs.minecraft.commandframework.config.LanguageMap;
import com.github.walkgs.minecraft.commandframework.impl.adapters.AdapterBinderImpl;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;

@Getter
@Setter
public class CommandInstallerImpl implements CommandInstaller {

    private static final CommandSpigotRegistry DEFAULT_REGISTRY = new CommandSpigotRegistryImpl();
    private final CommandExtractor commandExtractor = new CommandExtractorImpl(this);
    private LanguageMap language = Defaults.DEFAULT_LANGUAGE;
    private AdapterBinder adapterBinder = new AdapterBinderImpl(Defaults.DEFAULT_ADAPTER_BINDER_PARENT);
    private CommandSpigotRegistry registry = DEFAULT_REGISTRY;

    @Override
    public CommandContainer install(Plugin plugin, Class<?> clazz, Object... args) throws Exception {
        return install(plugin, true, clazz, args);
    }

    @Override
    public CommandContainer install(Plugin plugin, Object instance) {
        return install(plugin, instance, true);
    }

    @Override
    public CommandContainer install(Plugin plugin, boolean ignoreErrors, Class<?> clazz, Object... args) throws Exception {
        final CommandContainer container = commandExtractor.extract(plugin, ignoreErrors, clazz, args);
        registry.register(plugin, container);
        return container;
    }

    @Override
    public CommandContainer install(Plugin plugin, Object instance, boolean ignoreErrors) {
        final CommandContainer container = commandExtractor.extract(plugin, instance, ignoreErrors);
        registry.register(plugin, container);
        return container;
    }

    @Override
    public void unistall(CommandContainer container) {
        registry.unregister(container);
    }

}
