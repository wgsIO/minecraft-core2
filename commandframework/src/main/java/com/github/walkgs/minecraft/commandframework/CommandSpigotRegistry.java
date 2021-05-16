package com.github.walkgs.minecraft.commandframework;

import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public interface CommandSpigotRegistry {

    Map<String, Command> getKnownCommands();

    boolean register(Plugin plugin, CommandContainer container);

    boolean register(Plugin plugin, CommandContainer container, boolean removeExists);

    boolean unregister(CommandContainer container);

}
