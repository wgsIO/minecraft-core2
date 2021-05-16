package com.github.walkgs.minecraft.commandframework;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface CommandTabCompleter {

    List<String> onTabComplete(CommandSender sender, CommandContainer container, String[] args);

}
