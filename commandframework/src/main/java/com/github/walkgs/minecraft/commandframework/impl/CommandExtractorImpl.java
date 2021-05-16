package com.github.walkgs.minecraft.commandframework.impl;

import com.github.walkgs.cojt.codit.handling.ClassHandling;
import com.github.walkgs.cojt.cojex.HandlerNotFoundException;
import com.github.walkgs.cojt.cojut.properties.LinkedProperties;
import com.github.walkgs.minecraft.commandframework.*;
import com.github.walkgs.minecraft.commandframework.annotation.*;
import com.github.walkgs.minecraft.commandframework.config.LanguageMap;
import com.github.walkgs.minecraft.commandframework.impl.extractor.MethodExtractorImpl;
import com.github.walkgs.minecraft.commandframework.reflect.MethodExtractor;
import com.github.walkgs.minecraft.commandframework.reflect.MethodInfo;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@RequiredArgsConstructor
class CommandExtractorImpl implements CommandExtractor {

    //private static final CommandConfiguration DEFAULT_CONFIGURATION = new CommandConfigurationImpl(false);
    private static final MethodExtractor METHOD_EXTRACTOR = new MethodExtractorImpl();

    //public static final String COMMAND_PROPERTIES_NAME = "$prop@command-info";

    private final CommandInstaller installer;

    @Override
    public CommandContainer extract(Plugin plugin, Object instance) {
        return extract(plugin, instance, true);
    }

    @Override
    public CommandContainer extract(Plugin plugin, Class<?> clazz, Object... args) throws Exception {
        return extract(plugin, true, clazz, args);
    }

    @Override
    public CommandContainer extract(Plugin plugin, boolean ignoreErrors, Class<?> clazz, Object... args) throws Exception {
        return extract(plugin, ClassHandling.newInstance(clazz, args), ignoreErrors);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CommandContainer extract(Plugin plugin, Object instance, boolean ignoreErrors) {
        final Class<?> clazz = instance.getClass();
        final CommandBase base = findBase(clazz, ignoreErrors);
        if (base == null)
            return null;
        final List<CommandInfo> subs = new ArrayList<>();
        try {
            final Iterator<Method> methods = ClassHandling.getMethodsByAnnotation(clazz, ClassHandling.DECLARED_METHODS, Command.class);
            while (methods.hasNext()) {
                final Method method = methods.next();
                final Command command = method.getAnnotation(Command.class);
                final Description description = method.getAnnotation(Description.class);
                final Permissions permissions = method.getAnnotation(Permissions.class);
                final Configuration configuration = method.getAnnotation(Configuration.class);
                final Usage usage = method.getAnnotation(Usage.class);

                if (command == null || command.names().length < 1) {
                    if (ignoreErrors)
                        return null;
                    throw new IllegalStateException("The inserted method was not defined as a command");
                }

                final String[] names = command.names();
                final String[] perms = permissions != null ? permissions.permissions() : new String[]{};
                final String desc = description != null ? description.description() : installer.getLanguage().findFor(LanguageMap.UNDEFINED_NAME);

                final boolean alwaysConsole = configuration != null && configuration.alwaysConsole();

                subs.add(new CommandInfoImpl(names, perms, METHOD_EXTRACTOR.extract(method), desc, new CommandConfigurationImpl(alwaysConsole), new LinkedProperties()).apply(it -> {
                    if (usage != null)
                        it.setUsage(usage.usage());
                }));
            }
        } catch (HandlerNotFoundException e) {
            if (!ignoreErrors)
                throw new IllegalStateException("Failed to load sub commands");
        }
        return new CommandContainerImpl(plugin, installer.getLanguage(), base, installer.getAdapterBinder(), subs, instance).apply(CommandContainerImpl::generateUsages);
    }

    private CommandBase findBase(Class<?> clazz, boolean ignoreErrors) {
        final Command command = clazz.getAnnotation(Command.class);
        final Description description = clazz.getAnnotation(Description.class);
        final Permissions permissions = clazz.getAnnotation(Permissions.class);
        final Configuration configuration = clazz.getAnnotation(Configuration.class);
        final Usage usage = clazz.getAnnotation(Usage.class);

        if (command == null || command.names().length < 1) {
            if (ignoreErrors)
                return null;
            throw new IllegalStateException("The inserted class was not defined as a command");
        }

        final String[] names = command.names();
        final String[] perms = permissions != null ? permissions.permissions() : new String[]{};
        final String desc = description != null ? description.description() : installer.getLanguage().findFor(LanguageMap.UNDEFINED_NAME);

        final boolean alwaysConsole = configuration != null && configuration.alwaysConsole();

        final List<MethodInfo> worns = new ArrayList<>();
        final List<MethodInfo> normals = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Worn.class))
                worns.add(METHOD_EXTRACTOR.extract(method, ignoreErrors));
            else if (method.isAnnotationPresent(Normal.class))
                normals.add(METHOD_EXTRACTOR.extract(method, ignoreErrors));
        }

        return new CommandBaseImpl(names, perms, worns.toArray(new MethodInfo[]{}), normals.toArray(new MethodInfo[]{}), desc, new CommandConfigurationImpl(alwaysConsole), new LinkedProperties()).apply(it -> {
            if (usage != null)
                it.setUsage(usage.usage());
        });
    }


}
