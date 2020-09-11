package com.quantumtime.qc.common.utils;

import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Collection;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * .Description:对象工具类 Program:qc-api.Created on 2019-10-21 15:04
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
public class GlobalUtils<T> {
    private final T value;

    /**
     * Convert.
     *
     * @param source the source
     * @param result the result
     */
    public static void convert(Object source, Object result) {
        BeanCopier beanCopier = BeanCopier.create(source.getClass(), result.getClass(), false);
        beanCopier.copy(source, result, null);
    }

    public static <T> GlobalUtils<T> of(T value) {
        return new GlobalUtils<>(value);
    }

    public static int getNullNumElse(Integer num) {
        return Optional.ofNullable(num).orElse(0);
    }

    public static int getNullSumElse(Collection<Integer> nums) {
        return nums.stream().mapToInt(GlobalUtils::getNullNumElse).sum();
    }

    public T proper(boolean result, UnaryOperator<T> function) {
        return function.apply(value);
    }
}
