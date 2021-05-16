package com.github.walkgs.minecraft.commandframework.impl.adapters;

import com.github.walkgs.minecraft.commandframework.adapters.Adapter;
import com.github.walkgs.minecraft.commandframework.adapters.AdapterBinding;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class AdapterBindingImpl<T> implements AdapterBinding<T> {

    private final Adapter<T> adapter;
    private final List<Class<?>> types = new ArrayList<>();

    @Override
    public AdapterBinding<T> to(Class<?> clazz) {
        types.add(clazz);
        return this;
    }

    @Override
    public Adapter<T> with(Class<?>... classes) {
        return with(false, classes);
    }

    @Override
    public Adapter<T> with(boolean test, Class<?>... classes) {
        synchronized (types) {
            if (test) {
                for (Class<?> clazz : classes)
                    if (types.contains(clazz))
                        return adapter;
                return null;
            }
            if (types.containsAll(Arrays.asList(classes)))
                return adapter;
            return null;
        }
    }
}
