package com.github.walkgs.minecraft.core.logger.impl;

import com.github.walkgs.minecraft.core.logger.Debug;
import com.github.walkgs.minecraft.core.logger.Level;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

@RequiredArgsConstructor
public class BasicDebug implements Debug {

    private final String name;
    private final Level defaultLevel;
    private final List<String> contents;

    private final String[] prefix;
    private final String[] suffix;

    @Override
    public void send() {
        for (String content : contents)
            CONSOLE.sendMessage(content);
        clear();
    }

    @Override
    public void clear() {
        contents.clear();
    }

    @Override
    public String log(String message) {
        return log(defaultLevel, message);
    }

    @Override
    public String log(Level level, String message) {
        final String prefix = String.join("", this.prefix);
        final String suffix = String.join("", this.prefix);
        final String msg = translateAlternateColorCodes(PREFIX, level.getColor() + name + prefix + message + suffix);
        contents.add(msg);
        return msg;
    }

    @Override
    public String info(String message) {
        return log(Level.INFO, message);
    }

    @Override
    public String error(String message) {
        return log(Level.ERROR, message);
    }

    @Override
    public String success(String message) {
        return log(Level.SUCCESS, message);
    }

    @Override
    public String debug(Level level, String message) {
        return log(Level.DEBUG, message);
    }

    @Override
    public String debug(String message) {
        return debug(Level.DEBUG, message);
    }

}
