package com.quantumtime.qc.vo.tls.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class TlsResponse {

    /**
     * 请求处理的结果，OK 表示处理成功，FAIL 表示失败
     */
    @JSONField(name="ActionStatus")
    private String ActionStatus;

    /**
     * 错误码，0表示成功，非0表示失败
     */
    @JSONField(name="ErrorCode")
    private Integer ErrorCode;

    /**
     * 错误信息
     */
    @JSONField(name="ErrorInfo")
    private String ErrorInfo;

    @Override
    public String toString(){
        return "ActionStatus: " + getActionStatus() + " ErrorCode: " + getErrorCode() + " ErrorInfo: " + getErrorInfo();
    }


}
