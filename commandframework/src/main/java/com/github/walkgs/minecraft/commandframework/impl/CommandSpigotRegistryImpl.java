package com.github.walkgs.minecraft.commandframework.impl;

import com.github.walkgs.cojt.cojut.basic.accessor.Accessor;
import com.github.walkgs.cojt.cojut.basic.accessor.AccessorType;
import com.github.walkgs.cojt.cojut.basic.accessor.ClassAccessor;
import com.github.walkgs.cojt.cojut.basic.accessor.MemberAccessor;
import com.github.walkgs.minecraft.commandframework.CommandContainer;
import com.github.walkgs.minecraft.commandframework.CommandSpigotRegistry;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;

class CommandSpigotRegistryImpl implements CommandSpigotRegistry {

    private final CommandMap commandMap;

    @SneakyThrows
    CommandSpigotRegistryImpl() {
        final Server server = Bukkit.getServer();
        final Method methodCommandMap = server.getClass().getMethod("getCommandMap");
        this.commandMap = (CommandMap) methodCommandMap.invoke(server);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Command> getKnownCommands() {
        try (MemberAccessor accessor = ClassAccessor.getProvider().create(CommandMap.class)) {
            final Accessor<Member> knownCommands = accessor.filter(AccessorType.FIELDS, 0, ($member) -> $member.get().getName().equals("knownCommands"));
            return (Map<String, Command>) knownCommands.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean register(Plugin plugin, CommandContainer container) {
        return register(plugin, container, false);
    }

    @Override
    public boolean register(Plugin plugin, CommandContainer container, boolean removeExists) {
        if (removeExists)
            unregister(container);
        return commandMap.register(plugin.getName(), container);
    }

    @Override
    public boolean unregister(CommandContainer container) {
        final String[] names = container.getBase().getNames();
        final Map<String, Command> knownCommands = getKnownCommands();
        boolean removed = false;
        for (String name : names)
            if (knownCommands.containsKey(name))
                removed = knownCommands.remove(name).unregister(commandMap);
        return removed;
    }

}
