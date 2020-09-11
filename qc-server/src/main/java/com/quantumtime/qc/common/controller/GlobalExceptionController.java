package com.quantumtime.qc.common.controller;

import com.quantumtime.qc.common.entity.ErrorCodeEntity;
import com.quantumtime.qc.common.exception.BizException;
import com.quantumtime.qc.common.exception.BizVerifyException;
import com.quantumtime.qc.service.IErrorCodeService;
import com.quantumtime.qc.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.IntStream;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @Autowired
    private IErrorCodeService errorCodeService;


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        log.error("could_not_read_json...", e.getMessage());
        return Result.error500("请求参数不正确", e.getMessage());
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result handleValidationException(MethodArgumentNotValidException e) {
        log.error("parameter_validation_exception...", e);
        return Result.error500("请求类型不支持！", e.getMessage());
    }

    /**
     * 405 - Method Not Allowed。HttpRequestMethodNotSupportedException
     * 是ServletException的子类,需要Servlet API支持
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        log.error("request_method_not_supported...", e);
        return Result.error500("不被支持的访问方法！", e.getMessage());
    }

    /**
     * 415 - Unsupported Media Type。HttpMediaTypeNotSupportedException
     * 是ServletException的子类,需要Servlet API支持
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public Result handleHttpMediaTypeNotSupportedException(Exception e) {
        log.error("content_type_not_supported...", e);
        return Result.error500("不支持的媒体类型！", e.getMessage());
    }

    /**
     * Author: Tablo
     * <p> Description:[捕捉业务校验参数异常]
     * Created on 16:32 2019/12/20

     * @param request 请求
     * @param e 异常
     * @return com.quantumtime.qc.vo.Result<java.lang.String>
     **/

    @ExceptionHandler(BizVerifyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result bizVerifyException(HttpServletRequest request, BizVerifyException e) {
        ExceptionHandling exceptionHandling = new ExceptionHandling(e).invoke();
        ErrorCodeEntity errorCode = exceptionHandling.getErrorCode();
        String message = exceptionHandling.getMessage();
        return Result.error400(message, errorCode.getCode());
    }


    /**
     * Author: Tablo
     * <p> Description:[捕捉业务校验参数异常]
     * Created on 16:32 2019/12/20

     * @param request 请求
     * @param e 异常
     * @return com.quantumtime.qc.vo.Result<java.lang.String>
     **/
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result bizException(HttpServletRequest request, BizException e) {
        ExceptionHandling exceptionHandling = new ExceptionHandling(e).invoke();
        ErrorCodeEntity errorCode = exceptionHandling.getErrorCode();
        String message = exceptionHandling.getMessage();
        return Result.error500(message, errorCode.getCode());
    }


    /**
     * Author: Tablo
     * <p> Description:[捕捉其他异常]
     * Created on 16:32 2019/12/20

     * @param request 请求
     * @param e 异常
     * @return com.quantumtime.qc.vo.Result<java.lang.String>
     **/
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result globalException(HttpServletRequest request, Exception e) {
        log.error(e.getMessage(), e);
//        ErrorCodeEntity errorCode = errorCodeService.getErrorCodeEntityByErrorCode(ErrorCodeConstant.GLOBAL_UNKNOWN_EXCEPTION);
        return Result.error500("未知异常", "global_unknown_exception");
    }

    private class ExceptionHandling {
        private BizException e;
        private ErrorCodeEntity errorCode;
        private String message;

        public ExceptionHandling(BizException e) {
            this.e = e;
        }

        public ErrorCodeEntity getErrorCode() {
            return errorCode;
        }

        public String getMessage() {
            return message;
        }

        public ExceptionHandling invoke() {
            String[] args = e.getArgs();
            String code = e.getErrorCode();
            if (code == null){
                if(args != null) {
                    IntStream.range(0, args.length).forEach(i -> message = message.replaceAll("\\{" + i + "}", args[i]));
                }
                log.error(message, e);
                return this;
            }
            this.errorCode = errorCodeService.getErrorCodeEntityByErrorCode(e.getErrorCode());
            message = this.errorCode.getMessage();
            if(args != null) {
                IntStream.range(0, args.length).forEach(i -> message = message.replaceAll("\\{" + i + "}", args[i]));
            }
            log.error(message, e);
            return this;
        }
    }
}
