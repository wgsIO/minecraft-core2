package com.github.walkgs.minecraft.core.logger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Level {

    INFO("&7"),
    ERROR("&c"),
    SUCCESS("&a"),
    DEBUG("&e");

    private final String color;

}
