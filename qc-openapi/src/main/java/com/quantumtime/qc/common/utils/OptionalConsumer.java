package com.quantumtime.qc.common.utils;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * .Description:Optional处理类 Program:qc-api.Created on 2019-10-19 18:12
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OptionalConsumer<T> {

    private Optional<T> optional;

    private T value;
    private OptionalConsumer(Optional<T> optional) {

        this.optional = optional;
    }

    public static <T> OptionalConsumer<T> of(Optional<T> optional) {

        return new OptionalConsumer<>(optional);
    }

    public OptionalConsumer<T> ifPresent(Consumer<T> c) {
        optional.ifPresent(c);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public OptionalConsumer<T> ifNotPresent(Runnable r) {
        if (!optional.isPresent()) {
            r.run();
        }
        return this;
    }

    public void ifNotPresentEnd(Runnable runnable) {
        if (!optional.isPresent()) {
            runnable.run();
        }
    }

    public T get() {
        return optional.isPresent() ? this.optional.get():value;
    }

    public OptionalConsumer<T> value(T value) {
        this.value = value;
        return this;
    }
}
