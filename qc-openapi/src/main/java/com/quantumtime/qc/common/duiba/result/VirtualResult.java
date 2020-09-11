package com.quantumtime.qc.common.duiba.result;

import lombok.Data;

@Data
public class VirtualResult {

    public static final String SUCCESS = "success";
    public static final String PROCESS = "process";
    public static final String SUCCESS_PREFIX = "{'status':'success','credits':'";
    public static final String FAIL_PREFIX = "{'status':'fail','errorMessage':'";
    public static final String SUPPLIER_BIZ_ID = "','supplierBizId':'";
    public static final String END_SUFFIX = "'}";
    /** 充值是否成功的状态 */
    private String status;
    /** 失败信息 */
    private String errorMessage = "";
    /** 虚拟商品充值流水号 */
    private String supplierBizId = "";

    private Long credits = -1L;

    public VirtualResult(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return SUCCESS.equals(status) || PROCESS.equals(status)
                ? SUCCESS_PREFIX + credits + SUPPLIER_BIZ_ID + supplierBizId + END_SUFFIX
                : FAIL_PREFIX + errorMessage + SUPPLIER_BIZ_ID + supplierBizId + END_SUFFIX;
    }
}
