package com.quantumtime.qc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description: 返回类型封装 Created on 2019/09/18 13:37
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回状态码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回内容
     */
    private T data;

    /**
     * 返回异常信息
     */
    private String error;

    /**
     * 时间搓
     */
    private Long timestamp;

    public static <T> Result result(Integer code, String msg, T data, String error) {
        return Result.builder().code(code).message(msg).data(data).error(error).timestamp(System.currentTimeMillis()).build();
    }

    // 快速返回成功
    public static <T> Result success() {
        return success("success", null);
    }

    public static <T> Result success(String msg) {
        return success(msg, null);
    }

    public static <T> Result success(T data) {
        return success("success", data);
    }

    public static <T> Result success(String msg, T data) {
        return result(0, msg, data, null);
    }

    public static <T> Result error(int code, String msg, String error) {
        return result(code, msg, null, error);
    }

    public static <T> Result error401(String msg, String error) {
        return result(401, msg, null, error);
    }

    public static <T> Result error400(String msg, String error) {
        return result(400, msg, null, error);
    }

    public static <T> Result error404(String msg, String error) {
        return result(404, msg, null, error);
    }

    public static <T> Result error408(String msg, String error) {
        return result(408, msg, null, error);
    }

    public static <T> Result error415(String msg, String error) {
        return result(415, msg, null, error);
    }

    public static <T> Result error500(String msg, String error) {
        return result(500, msg, null, error);
    }

    public static <T> Result error502(String msg, String error) {
        return result(502, msg, null, error);
    }
}
