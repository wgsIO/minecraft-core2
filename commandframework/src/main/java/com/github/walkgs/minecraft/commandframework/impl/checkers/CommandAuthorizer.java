package com.github.walkgs.minecraft.commandframework.impl.checkers;

import lombok.NonNull;
import org.bukkit.command.CommandSender;

public class CommandAuthorizer {

    public boolean authorize(@NonNull CommandSender sender, String[] permissions) {
        if (permissions == null || permissions.length < 1)
            return true;
        for (String permission : permissions)
            if (!sender.hasPermission(permission))
                return false;
        return true;
    }

}
