package com.github.walkgs.minecraft.commandframework.impl.adapters;

import com.github.walkgs.cojt.codit.handling.ClassHandling;
import com.github.walkgs.cojt.cojut.Applicable;
import com.github.walkgs.minecraft.commandframework.adapters.Adapter;
import com.github.walkgs.minecraft.commandframework.adapters.AdapterBinder;
import com.github.walkgs.minecraft.commandframework.adapters.AdapterBinding;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AdapterBinderImpl implements AdapterBinder, Applicable<AdapterBinderImpl> {

    private final List<AdapterBinding<?>> bindings;
    private final AdapterBinder parent;

    public AdapterBinderImpl(List<AdapterBinding<?>> bindings) {
        this.bindings = bindings;
        this.parent = null;
    }

    public AdapterBinderImpl(AdapterBinder parent) {
        this.bindings = new ArrayList<>();
        this.parent = parent;
    }

    public AdapterBinderImpl() {
        this.bindings = new ArrayList<>();
        this.parent = null;
    }

    @Override
    public <T> AdapterBinding<T> bind(Adapter<T> adapter) {
        final AdapterBindingImpl<T> binding = new AdapterBindingImpl<>(adapter);
        bindings.add(binding);
        return binding;
    }

    @Override
    public <T> AdapterBinding<T> bind(Class<? extends Adapter<T>> clazz, Object... args) throws Exception {
        final Adapter<T> adapter = ClassHandling.newInstance(clazz, args);
        return bind(adapter);
    }

    @Override
    public <T> Adapter<T> from(Class<?>... classes) {
        return from(false, classes);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Adapter<T> from(boolean test, Class<?>... classes) {
        for (AdapterBinding<?> binding : bindings) {
            final Adapter<?> adapter = binding.with(test, classes);
            if (adapter != null)
                return (Adapter<T>) adapter;
        }
        return parent != null ? parent.from(test, classes) : null;
    }

}
