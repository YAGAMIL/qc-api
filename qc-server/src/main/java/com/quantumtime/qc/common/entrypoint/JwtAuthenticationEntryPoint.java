//package com.quantumtime.qc.common.entrypoint;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.quantumtime.qc.common.constant.ErrorCodeConstant;
//import com.quantumtime.qc.common.entity.ErrorCodeEntity;
//import com.quantumtime.qc.service.IErrorCodeService;
//import com.quantumtime.qc.vo.Result;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.Serializable;
//
//
//@Slf4j
//@Component
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
//
//    @Autowired
//    private ObjectMapper mapper;
//
//    @Autowired
//    private IErrorCodeService errorCodeService;
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json; charset=utf-8");
//        Result result = null;
//        /**身份认证未通过*/
//        if (authException instanceof BadCredentialsException) {
//            ErrorCodeEntity errorCode = errorCodeService.getErrorCodeEntityByErrorCode(ErrorCodeConstant.ACCOUNT_USERNAMEORPASSWD_ERROR);
//            result = Result.error500(errorCode.getMessage(), errorCode.getCode());
//        } else {
//            ErrorCodeEntity errorCode = errorCodeService.getErrorCodeEntityByErrorCode(ErrorCodeConstant.ACCOUNT_TOKEN_ERROR);
//            result = Result.error500(errorCode.getMessage(), errorCode.getCode());
//        }
//        response.getWriter().write(mapper.writeValueAsString(result));
//    }
//}
