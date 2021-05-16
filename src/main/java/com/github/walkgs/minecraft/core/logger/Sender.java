package com.github.walkgs.minecraft.core.logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public interface Sender {

    char PREFIX = '&';
    CommandSender CONSOLE = Bukkit.getConsoleSender();

    String log(String message);

    String log(Level level, String message);

    String info(String message);

    String error(String message);

    String success(String message);

    String debug(Level level, String message);

    String debug(String message);

}
