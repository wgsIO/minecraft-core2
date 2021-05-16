package com.github.walkgs.minecraft.commandframework.impl;

import com.github.walkgs.cojt.cojut.Strings;
import com.github.walkgs.minecraft.commandframework.CommandContainer;
import com.github.walkgs.minecraft.commandframework.CommandIdentifier;
import com.github.walkgs.minecraft.commandframework.CommandInfo;
import com.github.walkgs.minecraft.commandframework.CommandTabCompleter;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

class CommandTabCompleterImpl implements CommandTabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandContainer container, String[] args) {
        final List<CommandInfo> subs = container.getSubs();
        if (subs.size() > 0) {
            return getSuggestions(args.length == 0 ? "" : args[0], subs);
        }
        return null;
    }

    private List<String> getSuggestions(String prefix, List<? extends CommandIdentifier> identifiers) {
        List<String> suggestions = new ArrayList<>();
        for (CommandIdentifier identifier : identifiers) {
            for (String name : identifier.getNames())
                if (!Strings.isNullOrBlank(name) && (prefix.length() == 0 || name.toLowerCase().startsWith(prefix)))
                    suggestions.add(name);
        }
        return suggestions;
    }

}
