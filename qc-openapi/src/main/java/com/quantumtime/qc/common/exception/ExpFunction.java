package com.quantumtime.qc.common.exception;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>
 * Description:
 * Program:qc-api
 * </p>
 * Created on 2019-10-13 11:36
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */

public final class ExpFunction {

    private final Boolean value;

    private ExpFunction(Boolean value) {
        this.value = value;
    }

    public static ExpFunction of(Boolean value) {
        return new ExpFunction(value);
    }

    public static void true4Throw(boolean result, RuntimeException exception) {
        if (result) {
            throw exception;
        }
    }

    @SuppressWarnings("unused")
    public static void true4ThrowBiz(boolean result, String errorCode, String... messages) {
        if (result) {
            throw new BizException(errorCode, new Throwable(), messages);
        }
    }

    @SuppressWarnings("unused")
    public static void true4ThrowBiz(boolean result, String errorCode, Function function, String... messages) {
        if (result) {
            throw new BizException(errorCode, new Throwable(), messages);
        }
    }
    /**
     * Author: Tablo
     * <p> Description:[根据boolean判断是否抛出异常]
     * Created on 12:04 2019/10/13
     *
     * @param exceptionSupplier 生产者
     **/
    @SuppressWarnings("unused")
    public <X extends Throwable> void true4Throw(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.value) {
            throw exceptionSupplier.get();
        }
    }
    public static void throwNoCodeBiz(boolean result, String... messages) {
        if (result) {
            throw new BizException(messages);
        }
    }
}
