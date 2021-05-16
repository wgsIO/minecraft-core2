package com.github.walkgs.minecraft.commandframework.adapters;

public interface AdapterBinder {

    <T> AdapterBinding<T> bind(Adapter<T> adapter);

    <T> AdapterBinding<T> bind(Class<? extends Adapter<T>> clazz, Object... args) throws Exception;

    <T> Adapter<T> from(Class<?>... classes);

    <T> Adapter<T> from(boolean test, Class<?>... classes);

}
