package com.quantumtime.qc.controller;

import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.service.ITlsService;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.zip.DataFormatException;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * .Description:腾讯IM相关接口 & Created on 2019/11/12 11:06
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */

@ApiIgnore
@Api(tags = "腾讯IM相关接口")
@RestController
@RequestMapping("/tlsSig")
public class TlsSigController {

    @Resource private ITlsService tlsSigService;

    @Resource private IUserService userService;

    @GetMapping("/genTLSSignatureEx")
    @ApiOperation(value = "签名", notes = "返回签名结果 如果出错 urlSig为空，errMsg 为出错信息，成功返回有效的票据")
    public Result genTlsSignatureEx() {
        return Result.success(tlsSigService.genTLSSignatureEx());
    }

    @GetMapping("/checkTLSSignatureEx")
    @ApiOperation(value = "验签", notes = "返回验签结果 verifyResult返回校验结果", consumes = "application/x-www-form-urlencoded")
    public Result checkTlsSignatureEx(@ApiParam(value = "签名", required = true) String urlSig)
            throws DataFormatException {
        return Result.success(tlsSigService.checkTLSSignatureEx(urlSig));
    }

    @PostMapping("/findUserListLikeNickName")
    @ApiOperation(
            value = "根据昵称模糊查询用户列表",
            notes = "返回同一小区的用户列表 根据uid建立会话",
            consumes = "application/x-www-form-urlencoded")
    public Result findUserListLikeNickName(@ApiParam(value = "昵称", required = true) String nickName) {
        List<User> listLikeNickName = userService.findListLikeNickName(nickName);
        return Result.success(listLikeNickName);
    }
}
