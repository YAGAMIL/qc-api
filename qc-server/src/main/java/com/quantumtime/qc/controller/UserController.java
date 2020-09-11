package com.quantumtime.qc.controller;

import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.constant.SmsConstant;
import com.quantumtime.qc.common.controller.BaseController;
import com.quantumtime.qc.common.exception.BizException;
import com.quantumtime.qc.common.exception.BizVerifyException;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.entity.undo.SendManage;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.service.IAddressService;
import com.quantumtime.qc.service.IBusinessService;
import com.quantumtime.qc.service.IRedisService;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.service.StarFanService;
import com.quantumtime.qc.utils.LocationUtils;
import com.quantumtime.qc.utils.PoiUtil;
import com.quantumtime.qc.vo.*;
import com.quantumtime.qc.vo.login.LoginUser;
import com.quantumtime.qc.wrap.FansOrStarListWarp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * .Description:用户业务接口 & Created on 2019/11/08 20:42
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController extends BaseController<User, String, IUserService> {

    private final IRedisService redisService;

    private final AccountHelp accountHelp;
    @Resource private IBusinessService businessService;
    @Resource private IAddressService addressService;
    @Resource private IUserService userService;
    @Resource private StarFanService starFanService;

    @ApiIgnore
    @ApiOperation(value = "获取用户信息")
    @GetMapping("/info")
    public Result findUserInfo() {
        User currentUser = accountHelp.getCurrentUser();
        User userInfo = baseService.findById(currentUser.getUid());
        UserPoiVo userPoiVo = new UserPoiVo(userInfo);
        Address address = addressService.findAddressById(userInfo.getAddressId());
        userPoiVo.setPoiName(address.getPoiName());
        return Result.success(userPoiVo);
    }

    @ApiOperation(value = "给后台提供用户信息")
    @GetMapping("/userInfo")
    public Result findUserInfoWithPoi(@RequestParam("uid") String uid) {
        User currentUser = baseService.findById(uid);
        Assert.notNull(currentUser, "无法获取当前用户信息");
        Address address = addressService.findAddressById(currentUser.getAddressId());
        Assert.notNull(address, "无法获取当前用户的地址信息");
        Map<String, Object> res = new HashMap<>();
        res.put("user", currentUser);
        res.put("poiList", PoiUtil.createPoiList(address, businessService));
        return Result.success(res);
    }

    @ApiOperation(value = "给后台提供马甲用户信息")
    @GetMapping("/sysUserInfo")
    public Result findSysUserInfo() {
        return Result.success(baseService.findSysUser());
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "更换手机号第一步", notes = "仅校验验证码是否正确 只需传入verification")
    @PostMapping(value = "/modifyPhoneFirst")
    public Result<Boolean> modifyPhoneFirst(@RequestBody LoginUser user) {
        if (StringUtils.isEmpty(user.getVerification())) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "验证码");
        }
        String phone = accountHelp.getCurrentUser().getPhone();
        Object obj1 = redisService.get(SmsConstant.SMS_MODIFY_PHONE_FIRST_PREFIX + phone);
        if (obj1 == null) {
            throw new BizException(ErrorCodeConstant.SMS_VERIFICATION_MISS, new Throwable());
        } else {
            String verification = obj1.toString();
            if (verification.equals(user.getVerification())) {
                return Result.success(true);
            } else {
                throw new BizException(ErrorCodeConstant.SMS_VERIFICATION_ERROR, new Throwable());
            }
        }
    }

    @ApiOperation(
            value = "更换手机号第二步",
            notes = "更换手机号后需要重新登录 可以程序代码登录 也可以跳转用户登录 phoneNumber为新手机号 OldVerification为第一步的验证码 Verification为第二步验证码")
    @PostMapping(value = "/modifyPhoneSecond")
    public Result modifyPhoneSecond(@RequestBody LoginUser user) {
        if (StringUtils.isEmpty(user.getPhoneNumber())
                || StringUtils.isEmpty(user.getVerification())
                || StringUtils.isEmpty(user.getOldVerification())) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "新手机号和验证码");
        }
        Boolean result = this.baseService.modifyPhoneSecond(user);
        return Result.success(result);
    }

    @ApiOperation(value = "修改密码", notes = "传password")
    @GetMapping("/modifyPassword")
    public Result modifyPassword(User user) {
        User currentUser = accountHelp.getCurrentUser();
        User dbUser = new User();
        dbUser.setUid(currentUser.getUid());
        dbUser.setPassword(user.getPassword());
        baseService.update(dbUser);
        return Result.success(true);
    }

    @ApiOperation(value = "真实性认证第一步", notes = "所有参数都需要")
    @PostMapping(value = "unverifiedFirst")
    public Result unverifiedFirst(@RequestBody UnverifiedVo vo) {
        if (LocationUtils.parseNullResult(vo)) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "参数");
        }
        Boolean aBoolean = baseService.unverifiedFirst(vo);
        return Result.success(aBoolean);
    }

    @ApiOperation(value = "真实性认证第二步", notes = "确认寄送")
    @PostMapping(value = "unverifiedSecond")
    public Result unverifiedSecond() {
        Boolean aBoolean = baseService.unverifiedSecond();
        return Result.success(aBoolean);
    }

    @ApiOperation(value = "回退真实性认证第一步", notes = "修改地址回退")
    @PostMapping(value = "rollbackToUnverifiedFirst")
    public Result rollbackToUnverifiedFirst() {
        Boolean aBoolean = baseService.rollbackToUnverifiedFirst();
        return Result.success(aBoolean);
    }

    @ApiOperation(value = "真实性认证第三步", notes = "扫码完成验证 传入identificationCode")
    @PostMapping(value = "unverifiedThird")
    public Result unverifiedThird(@RequestBody SendManage message) {
        Boolean aBoolean = baseService.unverifiedThird(message.getIdentificationCode());
        return Result.success(aBoolean);
    }

    @ApiOperation(value = "更新住址", notes = "所有参数不能为空")
    @PostMapping(value = "modifyAddress")
    public Result modifyAddress(@RequestBody UnverifiedVo vo) {
        Boolean aBoolean = baseService.modifyAddress(vo);
        return Result.success(aBoolean);
    }

    @ApiOperation(value = "更新用户信息", notes = "可更新Nickname、Gender（性别[ 0.女  1.男  2.未知]）、Avatar")
    @PostMapping(value = "modifyUser")
    public Result modifyUser(@RequestBody UserVo vo) {
        Boolean aBoolean = baseService.modifyUser(vo);
        return Result.success(aBoolean);
    }

    @ApiOperation(value = "我的主页信息")
    @GetMapping("/personalDetail/{uid}")
    public Result personalDetail(@PathVariable("uid") String uid) {
        User user = accountHelp.getCurrentUser();
        if (userService.checkInBlacklist(user.getUid(), uid)) {
            return Result.result(10633, "已经拉黑了", null, "已经拉黑了");
        }
        UserInfo userInfo = userService.personalDetail(uid);
        Optional.ofNullable(user).ifPresent(currentUser ->
                userInfo.setRelationCode(starFanService.checkRelation(currentUser.getUid(), uid)));
        return Result.success(userInfo);
    }

    @ApiOperation(value = "修改出生日期")
    @GetMapping("/updateBirthday/{datetime}")
    public Result updateBirthday(@PathVariable("datetime") String datetime) {
        return Result.success(userService.changeBirthday(datetime));
    }

    @ApiOperation(value = "新增或者修改住址信息", notes = "所有参数不能为空")
    @PostMapping(value = "updateAddress")
    public Result updateAddress(@RequestBody AddressParam addressParam) {
        return Result.success(userService.updateAddress(addressParam));
    }

    @ApiOperation(value = "更改用户性别")
    @GetMapping("/updateGender/{gender}")
    public Result updateGender(@PathVariable("gender") Integer gender) {
        return Result.success(userService.updateGender(gender));
    }

    @ApiOperation(value = "获取用户生活圈历史记录", notes = "用户生活圈")
    @GetMapping("lifeCircle/history")
    public Result getAddressRecord() {
        User currentUser = userService.findById(accountHelp.getCurrentUser().getUid());
        return Result.success(userService.queryRecords(currentUser.getUid(), currentUser.getAddressId()));
    }

    @ApiOperation(value = "删除用户生活圈历史记录", notes = "删除用户生活圈记录")
    @DeleteMapping("lifeCircle/history/{addressId}")
    public Result removeAddressRecord(@PathVariable("addressId") Long addressId) {
        return Result.success(userService.removeRecords(accountHelp.getCurrentUser().getUid(), addressId));
    }

    @ApiOperation(value = "关注列表", notes = "返回粉丝列表list（头像，昵称，id，uid等）")
    @PostMapping("/starList")
    public Result starList(@RequestBody FansOrStarListWarp fansOrStarListWarp) {
        Integer pageNum = fansOrStarListWarp.getPageNum();
        Integer pageSize = fansOrStarListWarp.getPageSize();
        Assert.state(pageNum > 0, "pageNum必须大于0");
        Assert.state(pageSize > 0, "pageSize必须大于0");
        return Result.success(userService.starList(fansOrStarListWarp));
    }

    @ApiOperation(value = "粉丝列表", notes = "返回关注列表list（头像，昵称，id，uid等）")
    @PostMapping("/fensList")
    public Result fensList(@RequestBody FansOrStarListWarp fansOrStarListWarp) {
        Integer pageNum = fansOrStarListWarp.getPageNum();
        Integer pageSize = fansOrStarListWarp.getPageSize();
        Assert.state(pageNum > 0, "pageNum必须大于0");
        Assert.state(pageSize > 0, "pageSize必须大于0");
        return Result.success(userService.fansList(fansOrStarListWarp));
    }

    @ApiOperation(value = "举报用户")
    @PostMapping("/reportUser")
    public Result reportUser(@RequestBody ReportUserRequest reportUserRequest) {
        if (log.isDebugEnabled()) {
            log.debug("reportUser:" + reportUserRequest.toString());
        }
        try {
            Assert.notNull(reportUserRequest.getUid(), "被举报用户uid为空");
            Assert.notNull(reportUserRequest.getReason(), "reason为空");
            User user = accountHelp.getCurrentUser();
            reportUserRequest.setReportUid(user.getUid());
            userService.reportUser(reportUserRequest);
            return Result.success();
        } catch (Exception e) {
            log.error("reportUser:", e);
            return Result.result(10620, e.getMessage(), null, e.getMessage());
        }
    }

    @ApiOperation(value = "拉黑用户")
    @GetMapping("/addUserBlacklist")
    public Result addUserBlacklist(@RequestParam String toUid) {
        if (log.isDebugEnabled()) {
            log.debug("addUserBlacklist:" + toUid);
        }
        try {
            User user = accountHelp.getCurrentUser();
            userService.addUserBlacklist(user.getUid(), toUid);
            return Result.success();
        } catch (Exception e) {
            log.error("addUserBlacklist:", e);
            return Result.result(10621, e.getMessage(), null, e.getMessage());
        }
    }

    @ApiOperation(value = "取消拉黑用户")
    @GetMapping("/removeUserBlacklist")
    public Result removeUserBlacklist(@RequestParam String toUid) {
        if (log.isDebugEnabled()) {
            log.debug("removeUserBlacklist:" + toUid);
        }
        try {
            User user = accountHelp.getCurrentUser();
            userService.removeUserBlacklist(user.getUid(), toUid);
            return Result.success();
        } catch (Exception e) {
            log.error("removeUserBlacklist:", e);
            return Result.result(10622, e.getMessage(), null, e.getMessage());
        }
    }

    @ApiOperation(value = "获得拉黑用户列表")
    @GetMapping("/allUserBlacklist")
    public Result allUserBlacklist() {
        try {
            User user = accountHelp.getCurrentUser();
            return Result.success(userService.getAllUserBlacklist(user.getUid()));
        } catch (Exception e) {
            log.error("allUserBlacklist:", e);
            return Result.result(10623, e.getMessage(), null, e.getMessage());
        }
    }
}
