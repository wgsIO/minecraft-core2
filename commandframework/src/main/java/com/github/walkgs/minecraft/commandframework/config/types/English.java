package com.github.walkgs.minecraft.commandframework.config.types;

import com.github.walkgs.minecraft.commandframework.config.LanguageMap;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class English implements LanguageMap {

    @Getter(lazy = true)
    private static final LanguageMap instance = new English();

    private final String language = "EN";

    private final Map<String, String> map = new HashMap<String, String>() {{
        put(UNDEFINED_NAME, "Undefined");
        put(NON_PERMISSION, "§cYou do not have permission to perform this command.");
        put(SUB_COMMAND_USAGE_FORM, "§cUse: ");
        put(SUB_COMMAND_PARAMETER_NAME, "<command>");
        put(SUB_COMMAND_NOT_FOUND, "§cThis sub command could not be found.");

        //Adapters
        put(ADAPTER_VALUE_NAME, "value");
        put(ADAPTER_TEXT_NAME, "text");
        put(ADAPTER_PLAYER_NAME, "player");
    }};

    @Override
    public String findFor(String key) {
        return map.get(key);
    }

}
