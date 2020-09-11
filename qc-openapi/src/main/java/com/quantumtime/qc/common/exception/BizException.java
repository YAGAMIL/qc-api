package com.quantumtime.qc.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description: 业务异常 Created on 2019/09/18 13:35
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException {

  private String errorCode;

  private String message;

  private String[] args;

  /**
   * 业务异常 所有业务都应该使用此种方式抛出异常
   *
   * @param errorCode 错误码
   * @param e 异常
   * @param args message 对应参数
   */
  public BizException(String errorCode, Throwable e, String... args) {
    super(errorCode, e);
    this.setErrorCode(errorCode);
    this.setArgs(args);
  }
  public BizException(String... args) {
    this.setArgs(args);
  }


  /**
   * 业务异常 所有业务都应该使用此种方式抛出异常
   *
   * @param errorCode 错误码
   * @param e 异常
   * @param args message 对应参数
   */
  public BizException(String errorCode, String message, Throwable e, String... args) {
    super(errorCode, e);
    this.setErrorCode(errorCode);
    this.setArgs(args);
    this.setMessage(message);
  }

  public static BizException throwException(String errorCode, String... messages){
      return new BizException(errorCode, new Throwable(), messages);
  }
}
