package com.github.walkgs.minecraft.commandframework.adapters;

public interface AdapterBinding<T> {

    AdapterBinding<T> to(Class<?> clazz);

    Adapter<T> with(Class<?>... classes);

    Adapter<T> with(boolean test, Class<?>... classes);

}
