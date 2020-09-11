//package com.quantumtime.qc.controller.wechat;
//
//import com.quantumtime.qc.common.constant.ErrorCodeConstant;
//import com.quantumtime.qc.common.exception.BizVerifyException;
//import com.quantumtime.qc.service.IWeChatService;
//import com.quantumtime.qc.vo.login.LoginUser;
//import com.quantumtime.qc.vo.Result;
//import com.quantumtime.qc.vo.wechat.WeChatLoginVo;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@Api(tags = "微信相关接口")
//@RestController
//@RequestMapping("/wechat")
//public class WeChatController {
//
//    @Autowired
//    private IWeChatService weChatService;
//
//
//    @ApiOperation(value = "第三方微信登录", notes = "token有效期一个月")
//    @PostMapping(value = "/login")
//    public Result<LoginUser> login(@RequestBody WeChatLoginVo loginVo) {
//        if(StringUtils.isEmpty(loginVo.getPhoneNumber()) ||
//            StringUtils.isEmpty(loginVo.getPhoneNumberIv()) ||
//            StringUtils.isEmpty(loginVo.getJsCode()) ||
//            StringUtils.isEmpty(loginVo.getUserInfo()) ||
//            StringUtils.isEmpty(loginVo.getUserInfoIv())){
//            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "参数");
//        }
//        return Result.success("登录成功", weChatService.login(loginVo));
//    }
//
//
//}
