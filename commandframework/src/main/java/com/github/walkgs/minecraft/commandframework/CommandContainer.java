package com.github.walkgs.minecraft.commandframework;

import com.github.walkgs.minecraft.commandframework.config.LanguageMap;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

@Getter
public abstract class CommandContainer extends Command implements PluginIdentifiableCommand {

    private final Plugin plugin;
    private final CommandBase base;
    private final Object instance;
    private final List<CommandInfo> subs;
    private final LanguageMap language;

    protected CommandContainer(Plugin plugin, LanguageMap language, CommandBase base, List<CommandInfo> subs, Object instance) {
        super(base.getNames()[0], base.getDescription(), "/" + base.getNames()[0], Arrays.asList(Arrays.copyOfRange(base.getNames(), 1, base.getNames().length)));
        this.plugin = plugin;
        this.base = base;
        this.subs = subs;
        this.language = language;
        this.instance = instance;
    }

}
