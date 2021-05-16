package com.github.walkgs.minecraft.core.logger;

import com.github.walkgs.cojt.cojut.Strings;
import com.github.walkgs.minecraft.core.logger.impl.BasicDebug;
import com.github.walkgs.minecraft.core.logger.impl.BasicLogger;

import java.util.ArrayList;

public class LogManager {

    private static final String[] BLANK = new String[]{" "};

    private LogManager() {
    }

    public static Logger getLogger(String name, Level defaultLevel, Debug debug, String[] prefix, String[] suffix) {
        return new BasicLogger(Strings.isNullOrBlank(name) ? "" : "[" + name + "]", defaultLevel, debug, prefix, suffix);
    }

    public static Logger getLogger(String name, Level defaultLevel, String[] prefix, String[] suffix) {
        return getLogger(name, defaultLevel, new BasicDebug(Strings.isNullOrBlank(name) ? "" : "[" + name + "]", Level.DEBUG, new ArrayList<>(), prefix, suffix), prefix, suffix);
    }

    public static Logger getLogger(String name, String[] prefix, String[] suffix) {
        return getLogger(name, Level.INFO, prefix, suffix);
    }

    public static Logger getLogger(String name, Level defaultLevel, Debug debug) {
        return getLogger(name, defaultLevel, debug, BLANK, BLANK);
    }

    public static Logger getLogger(String name, Level defaultLevel) {
        return getLogger(name, defaultLevel, BLANK, BLANK);
    }

    public static Logger getLogger(String name) {
        return getLogger(name, BLANK, BLANK);
    }

}
