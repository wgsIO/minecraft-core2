package com.github.walkgs.minecraft.core.logger.impl;

import com.github.walkgs.minecraft.core.logger.Debug;
import com.github.walkgs.minecraft.core.logger.Level;
import com.github.walkgs.minecraft.core.logger.Logger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

@Getter
@RequiredArgsConstructor
public class BasicLogger implements Logger {

    private final String name;
    private final Level defaultLevel;
    private final Debug debug;

    private final String[] prefix;
    private final String[] suffix;

    @Override
    public String log(String message) {
        return log(defaultLevel, message);
    }

    @Override
    public String log(Level level, String message) {
        if (level == Level.DEBUG)
            return debug.log(level, message);
        final String prefix = String.join("", this.prefix);
        final String suffix = String.join("", this.prefix);
        final String msg = translateAlternateColorCodes(PREFIX, level.getColor() + name + prefix + message + suffix);
        CONSOLE.sendMessage(msg);
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
        return debug.log(level, message);
    }

    @Override
    public String debug(String message) {
        return debug.log(message);
    }

}
