package com.github.walkgs.minecraft.commandframework.impl;

import com.github.walkgs.cojt.cojut.Applicable;
import com.github.walkgs.cojt.cojut.properties.Properties;
import com.github.walkgs.minecraft.commandframework.*;
import com.github.walkgs.minecraft.commandframework.adapters.Adapter;
import com.github.walkgs.minecraft.commandframework.adapters.AdapterBinder;
import com.github.walkgs.minecraft.commandframework.adapters.Translator;
import com.github.walkgs.minecraft.commandframework.config.LanguageMap;
import com.github.walkgs.minecraft.commandframework.impl.checkers.CommandAuthorizer;
import com.github.walkgs.minecraft.commandframework.impl.extractor.ParameterExtractorImpl;
import com.github.walkgs.minecraft.commandframework.impl.extractor.ParameterProperties;
import com.github.walkgs.minecraft.commandframework.reflect.MethodInfo;
import com.github.walkgs.minecraft.commandframework.reflect.ParameterInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

class CommandContainerImpl extends CommandContainer implements Applicable<CommandContainerImpl> {

    private static final CommandTabCompleter DEFAULT_TAB_COMPLETER = new CommandTabCompleterImpl();
    private static final CommandAuthorizer AUTHORIZER = new CommandAuthorizer();
    private final AdapterBinder binder;
    protected CommandTabCompleter tabCompleter = DEFAULT_TAB_COMPLETER;

    public CommandContainerImpl(Plugin plugin, LanguageMap language, CommandBase base, AdapterBinder binder, List<CommandInfo> subs, Object instance) {
        super(plugin, language, base, subs, instance);
        this.binder = binder;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        try {
            final CommandBase base = getBase();

            if (!AUTHORIZER.authorize(sender, base.getPermissions())) {
                sender.sendMessage(getLanguage().findFor(LanguageMap.NON_PERMISSION));
                return true;
            }

            final MethodInfo[] worns = base.getWorns();
            if (worns.length > 0)
                execute(sender, worns[0], base, args);

            final List<CommandInfo> subs = getSubs();
            if (subs.size() < 1) {
                final MethodInfo[] normals = base.getNormals();
                if (normals.length > 0)
                    execute(sender, normals[0], base, args);
                return true;
            }

            if (args.length < 1) {
                sender.sendMessage(getUsage());
                return true;
            }

            for (CommandInfo info : subs) {
                for (String name : info.getNames())
                    if (name.equalsIgnoreCase(args[0])) {
                        execute(sender, info.getMethod(), info, Arrays.copyOfRange(args, 1, args.length));
                        return true;
                    }
            }

            sender.sendMessage(getLanguage().findFor(LanguageMap.SUB_COMMAND_NOT_FOUND));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private void execute(CommandSender sender, MethodInfo method, CommandIdentifier identifier, String[] args) {
        final ParameterInfo[] parameters = method.getParameters();
        final Object[] arguments = new Object[parameters.length];
        for (int index = 0, last = 0; index < parameters.length; index++) {
            final ParameterInfo parameter = parameters[index];
            final Properties properties = parameter.getProperties();
            final Properties.DataSet<ParameterProperties> dataSet = properties.get(ParameterExtractorImpl.PROPERTIES_NAME);
            final ParameterProperties props = dataSet.get(ParameterExtractorImpl.PROPERTIES_INDEX);

            final String arg = args.length >= last + 1 ? args[props.getArgAt()] : null;

            if (!(props.isSender() || props.isAuto()))
                last++;

            if (arg == null && !(props.isSender() || props.isAuto())) {
                if (props.isOptional())
                    continue;
                sender.sendMessage(identifier.getUsage());
                return;
            }

            if (props.isText()) {
                arguments[index] = String.join(" ", Arrays.copyOfRange(args, props.getArgAt(), args.length));
                continue;
            }

            final Adapter<?> adapter = binder.from(parameter.getParameter().getType());
            arguments[index] = adapter.adapt(sender, parameter, arg);
        }
        try {
            method.getMethod().invoke(getInstance(), arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return tabCompleter.onTabComplete(sender, this, args);
    }

    protected void generateUsages() {
        final List<CommandInfo> subs = getSubs();
        final CommandBaseImpl base = (CommandBaseImpl) getBase();
        if (base.getUsage() == null)
            base.setUsage(getLanguage().findFor(LanguageMap.SUB_COMMAND_USAGE_FORM) + "/" + getName() + " " + (subs.size() > 0 ? getLanguage().findFor(LanguageMap.SUB_COMMAND_PARAMETER_NAME) : generateUsageParameters(getBase().getNormals()[0])));
        setUsage(base.getUsage());
        for (CommandInfo info : subs) {
            final CommandInfoImpl baseInfo = (CommandInfoImpl) info;
            if (baseInfo.getUsage() == null)
                baseInfo.setUsage(getLanguage().findFor(LanguageMap.SUB_COMMAND_USAGE_FORM) + "/" + getName() + " " + generateUsageParameters(info.getMethod()));
        }
    }

    private String generateUsageParameters(MethodInfo info) {
        final StringBuilder usages = new StringBuilder();
        int last = 0;
        for (ParameterInfo parameter : info.getParameters()) {
            final Properties properties = parameter.getProperties();
            final Properties.DataSet<ParameterProperties> dataSet = properties.get(ParameterExtractorImpl.PROPERTIES_NAME);
            final ParameterProperties props = dataSet.get(ParameterExtractorImpl.PROPERTIES_INDEX);
            if (props.isAuto() || last > props.getArgAt() || props.isSender())
                continue;
            final boolean text = props.isText();
            final boolean optional = props.isOptional();

            final Class<?> type = Translator.translate(parameter.getParameter().getType());
            final Adapter<?> adapter = binder.from(type);

            usages.append(optional ? "(" : text ? "[" : "<")
                    .append(text ? "..." : adapter.getType(parameter))
                    .append(optional ? ")" : text ? "]" : ">")
                    .append(" ");
            last++;
        }
        return usages.toString();
    }

}
