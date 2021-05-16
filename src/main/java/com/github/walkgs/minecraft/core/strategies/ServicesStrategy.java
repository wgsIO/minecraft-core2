package com.github.walkgs.minecraft.core.strategies;

import com.github.walkgs.cojt.codit.handling.ClassHandling;
import com.github.walkgs.cojt.codit.lifecycle.LifeCycle;
import com.github.walkgs.cojt.codit.lifecycle.LifeCycleInstaller;
import com.github.walkgs.cojt.codit.lifecycle.LifeDescription;
import com.github.walkgs.cojt.codit.lifecycle.impl.LifeCycleHandlerImpl;
import com.github.walkgs.cojt.codit.lifecycle.notifiers.StateChangeNotification;
import com.github.walkgs.cojt.cojys.executors.Context;
import com.github.walkgs.cojt.cojys.invokers.post.Post;
import com.github.walkgs.cojt.cojys.invokers.strategies.StrategyHandler;
import com.github.walkgs.cojt.cojys.invokers.strategies.setup.Strategy;
import com.github.walkgs.cojt.cojys.services.Service;
import com.github.walkgs.minecraft.commandframework.impl.CommandInstallerImpl;
import com.github.walkgs.minecraft.core.AbstractPlugin;
import com.github.walkgs.minecraft.core.annotation.PluginLifeEvents;
import com.github.walkgs.minecraft.core.annotation.PluginLifeStrategies;
import com.github.walkgs.minecraft.core.annotation.PluginService;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Strategy(name = "ServicesStrategy")
public class ServicesStrategy {

    private static final Object[] EMPTY_ARRAY = new Object[0];
    private static final StateChangeNotification STATE_CHANGE_NOTIFICATION = new StateChangeNotification();

    private final LifeCycleHandlerImpl handler = new LifeCycleHandlerImpl();
    private final Map<Plugin, Queue<Object>> services = new ConcurrentHashMap<>();

    private final LifeCycleInstaller lifeCycleInstaller = handler.request("installer");
    private final CommandInstallerImpl commandInstaller = new CommandInstallerImpl();

    @Post(type = StrategyHandler.SETUP)
    private void setup(Context<LifeDescription, LifeCycleInstaller, StrategyHandler> context) {
        final LifeDescription pluginDescription = context.getProvider();
        final AbstractPlugin plugin = (AbstractPlugin) pluginDescription.getLivableObject();

        final String packageName = plugin.getClass().getPackage().getName();
        final ClassLoader classLoader = plugin.getClass().getClassLoader();

        final Map<Integer, List<Class<?>>> services = findServicesAndCollect(plugin, classLoader, packageName);
        final int major = findMajor(services);

        final Queue<Object> registry = this.services.computeIfAbsent(plugin, it -> new ConcurrentLinkedQueue<>());

        int failed = 0;
        for (int order = 0; order <= major; order++) {
            for (Class<?> clazz : services.get(order)) {
                try {
                    final LifeCycle lifeCycle = clazz.getAnnotation(LifeCycle.class);
                    final Object service = ClassHandling.newInstance(clazz);
                    if (lifeCycle == null) {
                        install(plugin, service);
                        registry.add(service);
                        continue;
                    }
                    final PluginLifeEvents events = clazz.getAnnotation(PluginLifeEvents.class);
                    final PluginLifeStrategies strategies = clazz.getAnnotation(PluginLifeStrategies.class);

                    final LifeDescription description = lifeCycleInstaller.install(service, strategies != null ? strategies.strategies() : EMPTY_ARRAY);
                    lifeCycleInstaller.installEvents(description, STATE_CHANGE_NOTIFICATION);
                    if (events != null)
                        lifeCycleInstaller.installEvents(description, events.events());
                    handler.load(description.getLife(), load -> install(plugin, service), start -> registry.add(service));

                } catch (Exception e) {
                    plugin.error(e.toString());
                    failed++;
                }
            }
        }

        plugin.info("&7Loaded &a" + services.size() + " &7and failed &4" + failed + " &7services");
    }

    private void install(AbstractPlugin plugin, Object service) {
        final Class<?> clazz = service.getClass();
        if (clazz.isAssignableFrom(Listener.class)) {
            plugin.registerEvents((Listener) service);
            return;
        }
        commandInstaller.install(plugin, service, false);
    }

    private Map<Integer, List<Class<?>>> findServicesAndCollect(AbstractPlugin plugin, ClassLoader loader, String packageName) {
        final Map<Integer, List<Class<?>>> services = new HashMap<>();
        try {
            final ClassPath path = ClassPath.from(loader);
            final ImmutableSet<ClassPath.ClassInfo> infoSet = path.getTopLevelClassesRecursive(packageName);
            for (ClassPath.ClassInfo info : infoSet) {
                try {
                    final Class<?> clazz = info.load();
                    final PluginService service = clazz.getAnnotation(PluginService.class);
                    if (service == null)
                        continue;
                    final int order = service.order();
                    services.computeIfAbsent(order < 0 ? 0 : order, it -> new ArrayList<>()).add(clazz);
                } catch (Exception e) {
                    plugin.debug("Failed to load " + info.getSimpleName() + " class.");
                }
            }
        } catch (IOException e) {
            plugin.error("Failed to find classes of service.");
        }
        return services;
    }

    private int findMajor(Map<Integer, List<Class<?>>> services) {
        int major = 0;
        for (Integer order : services.keySet())
            major = order > major ? order : major;
        return major;
    }

}
