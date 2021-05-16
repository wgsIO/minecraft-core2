package com.github.walkgs.minecraft.commandframework;

import com.github.walkgs.cojt.cojut.properties.Properties;

public interface CommandIdentifier {

    String[] getNames();

    String[] getPermissions();

    String getUsage();

    String getDescription();

    CommandConfiguration getConfiguration();

    Properties getProperties();

}
