package com.quantumtime.qc.common.exception;

public class BizVerifyException extends BizException {
    /**
     * 业务异常 所有业务都应该使用此种方式抛出异常
     *
     * @param errorCode
     * @param e
     * @param arges     errorcode message 对应参数
     */
    public BizVerifyException(String errorCode, Throwable e, String... arges) {
        super(errorCode, e, arges);
    }
}
