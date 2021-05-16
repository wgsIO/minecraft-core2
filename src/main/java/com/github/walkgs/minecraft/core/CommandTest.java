package com.github.walkgs.minecraft.core;

import com.github.walkgs.cojt.codit.lifecycle.LifeCycle;
import com.github.walkgs.cojt.cojut.Applicable;
import com.github.walkgs.minecraft.commandframework.annotation.Command;
import com.github.walkgs.minecraft.commandframework.annotation.Normal;
import com.github.walkgs.minecraft.commandframework.annotation.Sender;
import com.github.walkgs.minecraft.commandframework.annotation.Worn;
import com.github.walkgs.minecraft.core.annotation.PluginService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@LifeCycle
@PluginService
@Command(names = "PEDRO")
public class CommandTest implements Applicable<CommandTest> {

    @Worn
    private void worn() {

    }

    @Normal
    private void normal(@Sender CommandSender sender, String nome) {
        Bukkit.getConsoleSender().sendMessage("MODO NORMAL EXECUTADO");
    }

    @Command(names = "teste")
    private void test(@Sender CommandSender sender, Integer value) {
        Bukkit.getConsoleSender().sendMessage("NUMERO: " + value);
    }

}
