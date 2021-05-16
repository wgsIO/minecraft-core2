package com.github.walkgs.minecraft.commandframework.impl.adapters;

import com.github.walkgs.cojt.cojut.properties.Properties;
import com.github.walkgs.minecraft.commandframework.Defaults;
import com.github.walkgs.minecraft.commandframework.adapters.Adapter;
import com.github.walkgs.minecraft.commandframework.config.LanguageMap;
import com.github.walkgs.minecraft.commandframework.impl.extractor.ParameterExtractorImpl;
import com.github.walkgs.minecraft.commandframework.impl.extractor.ParameterProperties;
import com.github.walkgs.minecraft.commandframework.reflect.ParameterInfo;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;

import java.lang.reflect.Type;
import java.util.UUID;

public class PlayerAdapter implements Adapter<ServerOperator> {

    private static final Server SERVER = Bukkit.getServer();

    @Override
    public String getType(ParameterInfo info) {
        return Defaults.DEFAULT_LANGUAGE.findFor(LanguageMap.ADAPTER_PLAYER_NAME);
    }

    @Override
    public ServerOperator adapt(CommandSender sender, ParameterInfo info, String value) {
        final Properties properties = info.getProperties();
        final Properties.DataSet<ParameterProperties> dataSet = properties.get(ParameterExtractorImpl.PROPERTIES_NAME);
        final ParameterProperties props = dataSet.get(ParameterExtractorImpl.PROPERTIES_INDEX);
        if (props.isSender())
            return sender;
        return findPlayer(info.getParameter().getParameterizedType(), UUID.fromString(value), value);
    }

    private ServerOperator findPlayer(Type type, UUID uuid, String value) {
        if (type == OfflinePlayer.class) {
            final OfflinePlayer player = SERVER.getOfflinePlayer(uuid);
            return player == null ? findOfflinePlayer(value) : player;
        }
        final Player player = SERVER.getPlayer(uuid);
        return player == null ? SERVER.getPlayer(value) : player;
    }

    private OfflinePlayer findOfflinePlayer(String value) {
        final OfflinePlayer player = SERVER.getPlayer(value);
        if (player != null)
            return player;
        final OfflinePlayer[] players = SERVER.getOfflinePlayers();
        for (OfflinePlayer find : players)
            if (find.getName().equalsIgnoreCase(value))
                return find;
        return null;
    }

}
