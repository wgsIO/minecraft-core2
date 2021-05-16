package com.github.walkgs.minecraft.commandframework.config;

public interface LanguageMap {

    String UNDEFINED_NAME = "$ud";
    String NON_PERMISSION = "$np";
    String SUB_COMMAND_USAGE_FORM = "$suf";
    String SUB_COMMAND_PARAMETER_NAME = "$sug";
    String SUB_COMMAND_NOT_FOUND = "$snf";

    //Adapters
    String ADAPTER_VALUE_NAME = "$avn";
    String ADAPTER_TEXT_NAME = "$atn";
    String ADAPTER_PLAYER_NAME = "$apn";

    String findFor(String key);

    String getLanguage();

}
