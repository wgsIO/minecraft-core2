package com.github.walkgs.minecraft.core;

import com.github.walkgs.cojt.codit.lifecycle.LifeCycleHandler;
import com.github.walkgs.cojt.codit.lifecycle.LifeCycleInstaller;
import com.github.walkgs.cojt.codit.lifecycle.LifeDescription;
import com.github.walkgs.cojt.codit.lifecycle.impl.LifeCycleHandlerImpl;
import com.github.walkgs.cojt.codit.lifecycle.notifiers.StateChangeNotification;
import com.github.walkgs.cojt.cojut.properties.LinkedProperties;
import com.github.walkgs.cojt.cojut.properties.Properties;
import com.github.walkgs.cojt.cojys.properties.Exchanger;
import com.github.walkgs.minecraft.core.annotation.PluginLifeEvents;
import com.github.walkgs.minecraft.core.annotation.PluginLifeStrategies;
import com.github.walkgs.minecraft.core.logger.Debug;
import com.github.walkgs.minecraft.core.logger.Level;
import com.github.walkgs.minecraft.core.logger.LogManager;
import com.github.walkgs.minecraft.core.logger.Logger;
import com.github.walkgs.minecraft.core.strategies.ServicesStrategy;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.instrument.IllegalClassFormatException;
import java.net.BindException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class AbstractPlugin extends JavaPlugin implements Logger {

    private static final String SEPARATOR = "--------------------------";
    private static final List<Class<?>> DEFAULT_STRATEGIES = new ArrayList<Class<?>>() {{
        add(ServicesStrategy.class);
    }};

    private static final LifeCycleHandler HANDLER = new LifeCycleHandlerImpl();
    @SuppressWarnings("unchecked")
    private static final LifeCycleInstaller INSTALLER = ((Exchanger<LifeCycleInstaller>) HANDLER).request("installer");
    private static final StateChangeNotification STATE_CHANGE_NOTIFICATION = new StateChangeNotification();

    //private static final ServicesStrategy SERVICES_STRATEGY = new ServicesStrategy();

    protected final Logger logger = LogManager.getLogger(getName());
    private final Properties properties = new LinkedProperties();
    private LifeDescription description;
    private PluginManager pluginManager = getServer().getPluginManager();

    @Override
    public void onEnable() {
        bySep($ -> {
            info("Loading life...");
            try {
                final Class<? extends AbstractPlugin> clazz = getClass();
                final ArrayList<Class<?>> strategies = new ArrayList<>(DEFAULT_STRATEGIES);
                strategies.addAll(Arrays.asList(findLifeStrategies(clazz)));
                description = INSTALLER.install(this, strategies.toArray(new Class[]{}));
                INSTALLER.installEvents(description, STATE_CHANGE_NOTIFICATION);
                final Class<?>[] events = findLifeEvents(clazz);
                if (events.length > 0)
                    INSTALLER.installEvents(description, events);
                debug("Lifecycle installed.");
                HANDLER.load(description.getLife(), load -> {
                    debug("Successfully loaded.");
                }, start -> success("Plugin enabled."));
            } catch (IllegalClassFormatException | BindException e) {
                error("Failed to start plugin; causes:" + e.getMessage());
                getPluginLoader().disablePlugin(this);
            }
        });
    }

    @Override
    public void onDisable() {
        bySep($ -> {
            info("Unloading life...");
            HANDLER.unload(description.getLife(), unload -> {
                debug("Successfully unloaded.");
            }, stop -> {
            });
            INSTALLER.uninstall(this);
            debug("Lifecycle uninstalling.");
            error("Plugin disabled.");
        });
    }

    private Class<?>[] findLifeEvents(Class<? extends AbstractPlugin> clazz) {
        final PluginLifeEvents events = clazz.getAnnotation(PluginLifeEvents.class);
        return events != null ? events.events() : new Class[0];
    }

    private Class<?>[] findLifeStrategies(Class<? extends AbstractPlugin> clazz) {
        final PluginLifeStrategies strategies = clazz.getAnnotation(PluginLifeStrategies.class);
        return strategies != null ? strategies.strategies() : new Class[0];
    }

    public void registerEvents(Class<? extends Listener>... listeners) throws IllegalAccessException, InstantiationException {
        for (Class<? extends Listener> listener : listeners)
            pluginManager.registerEvents(listener.newInstance(), this);
    }

    public void registerEvents(Listener... listeners) {
        for (Listener listener : listeners)
            pluginManager.registerEvents(listener, this);
    }

    private void bySep(Consumer<AbstractPlugin> consumer) {
        log(SEPARATOR);
        consumer.accept(this);
        log(SEPARATOR);
    }

    @Override
    public Debug getDebug() {
        return logger.getDebug();
    }

    @Override
    public String log(String message) {
        return logger.log(message);
    }

    @Override
    public String log(Level level, String message) {
        return logger.log(level, message);
    }

    @Override
    public String info(String message) {
        return logger.info(message);
    }

    @Override
    public String error(String message) {
        return logger.error(message);
    }

    @Override
    public String success(String message) {
        return logger.success(message);
    }

    @Override
    public String debug(Level level, String message) {
        return logger.debug(level, message);
    }

    @Override
    public String debug(String message) {
        return logger.debug(message);
    }

}
