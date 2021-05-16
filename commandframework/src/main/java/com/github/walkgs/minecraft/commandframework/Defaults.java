package com.github.walkgs.minecraft.commandframework;

import com.github.walkgs.minecraft.commandframework.adapters.AdapterBinder;
import com.github.walkgs.minecraft.commandframework.adapters.java.*;
import com.github.walkgs.minecraft.commandframework.config.LanguageMap;
import com.github.walkgs.minecraft.commandframework.config.types.English;
import com.github.walkgs.minecraft.commandframework.impl.adapters.AdapterBinderImpl;
import com.github.walkgs.minecraft.commandframework.impl.adapters.PlayerAdapter;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Defaults {

    public static final AdapterBinder DEFAULT_ADAPTER_BINDER_PARENT = new AdapterBinderImpl().apply(binder -> {
        //Java
        binder.bind(new StringAdapter()).to(String.class);
        binder.bind(new BooleanAdapter()).to(Boolean.class);
        binder.bind(new ByteAdapter()).to(Byte.class);
        binder.bind(new CharacterAdapter()).to(Character.class);
        binder.bind(new DoubleAdapter()).to(Double.class);
        binder.bind(new FloatAdapter()).to(Float.class);
        binder.bind(new IntegerAdapter()).to(Integer.class);
        binder.bind(new LongAdapter()).to(Long.class);
        binder.bind(new ShortAdapter()).to(Short.class);
        //Custom
        binder.bind(new PlayerAdapter()).to(Player.class).to(CommandSender.class).to(OfflinePlayer.class);
    });
    public static LanguageMap DEFAULT_LANGUAGE = English.getInstance();

}
