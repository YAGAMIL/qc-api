package com.quantumtime.qc.service.impl;

import com.alibaba.fastjson.JSON;
import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.constant.SmsConstant;
import com.quantumtime.qc.common.enums.UserStateEnum;
import com.quantumtime.qc.common.exception.BizException;
import com.quantumtime.qc.common.exception.BizVerifyException;
import com.quantumtime.qc.common.exception.ExpFunction;
import com.quantumtime.qc.common.exception.JpaCrudException;
import com.quantumtime.qc.common.properties.DefaultAvatarsProperties;
import com.quantumtime.qc.common.service.impl.BaseServiceImpl;
import com.quantumtime.qc.entity.StarFan;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.entity.UserBlacklist;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.entity.poi.AddressRecord;
import com.quantumtime.qc.entity.report.ReportUser;
import com.quantumtime.qc.entity.undo.SendManage;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.repository.AddressRecordRepository;
import com.quantumtime.qc.repository.ClickContentRepository;
import com.quantumtime.qc.repository.ReportUserRepository;
import com.quantumtime.qc.repository.SendManageRepository;
import com.quantumtime.qc.repository.StarFanRepository;
import com.quantumtime.qc.repository.UserBlacklistRepository;
import com.quantumtime.qc.repository.UserRepository;
import com.quantumtime.qc.repository.VideoRepository;
import com.quantumtime.qc.service.IAddressService;
import com.quantumtime.qc.service.IMQService;
import com.quantumtime.qc.service.IRedisService;
import com.quantumtime.qc.service.ISendManageService;
import com.quantumtime.qc.service.ISensorAnalytics;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.service.StarFanService;
import com.quantumtime.qc.utils.LocationUtils;
import com.quantumtime.qc.vo.AddressParam;
import com.quantumtime.qc.vo.FansOrStarListVo;
import com.quantumtime.qc.vo.QualifyVo;
import com.quantumtime.qc.vo.RegisterUser;
import com.quantumtime.qc.vo.ReportUserRequest;
import com.quantumtime.qc.vo.StarFanListVo;
import com.quantumtime.qc.vo.SysUser;
import com.quantumtime.qc.vo.UnverifiedVo;
import com.quantumtime.qc.vo.UserInfo;
import com.quantumtime.qc.vo.UserVo;
import com.quantumtime.qc.vo.login.LoginUser;
import com.quantumtime.qc.wrap.FansOrStarListWarp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.quantumtime.qc.common.constant.ErrorCodeConstant.GLOBAL_EXIST;
import static com.quantumtime.qc.common.constant.ErrorCodeConstant.SMS_VERIFICATION_ERROR;
import static com.quantumtime.qc.common.constant.ErrorCodeConstant.VERIFY_PHONE_FORMAT_ERROR;
import static com.quantumtime.qc.common.constant.ErrorCodeConstant.WECHAT_NOT_EXIST;
import static com.quantumtime.qc.common.constant.LoginConstant.isLegalPhone;
import static com.quantumtime.qc.common.constant.LoginConstant.isLegalUnionId;
import static com.quantumtime.qc.common.constant.SmsConstant.SMS_REGISTER_PREFIX;
import static com.quantumtime.qc.common.enums.UserStateEnum.ENABLE;
import static com.quantumtime.qc.common.exception.ExpFunction.true4Throw;
import static com.quantumtime.qc.common.exception.ExpFunction.true4ThrowBiz;
import static com.quantumtime.qc.entity.User.DEFAULT_SCORE;

/**
 * Description: 用户业务接口实现 Created on 2019/09/18 11:58
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Slf4j
@Service(interfaceClass = IUserService.class, version = "1.0")
public class UserServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements IUserService {

    @Lazy @Resource private IRedisService redisService;
    @Resource private ISendManageService sendManageService;
    @Resource private SendManageRepository sendManageRepository;
    @Resource private AccountHelp accountHelp;
    @Resource private DefaultAvatarsProperties defaultAvatarsProperties;
    @Resource private IAddressService addressService;
    @Resource private StarFanService starFanService;
    @Resource private IMQService mqService;
    @Resource private ISensorAnalytics sensorAnalytics;
    @Resource private UserRepository userRepository;
    @Resource private VideoRepository videoRepository;
    @Resource private IUserService userService;
    @Resource private AddressRecordRepository recordRepository;
    @Resource private StarFanRepository starFanRepository;
    @Resource private ClickContentRepository clickContentRepository;
    @Resource private ReportUserRepository reportUserRepository;
    @Resource private UserBlacklistRepository userBlacklistRepository;

    @Cacheable(value = "cache-user", key = "'user'+#s", unless = "#result == null")
    @Override
    public User findById(String s) {
        return super.findById(s);
    }

    @Override
    public User findByAccount(String account) {
        return baseRepository.findByAccount(account);
    }

    @Override
    public User findByPhone(String phone) {
        return baseRepository.findPhone(phone);
    }

    @Override
    public UserInfo findInfoByPhone(String phone) {
        return buildUserInfo(baseRepository.findPhone(phone));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unverifiedFirst(UnverifiedVo vo) {
        String uid = accountHelp.getCurrentUser().getUid();
        User dbUser = this.findById(uid);
        if (!dbUser.getState().equals(UserStateEnum.UNVERIFIED_FIRST.getCode())) {
            throw new BizException(ErrorCodeConstant.ACCOUNT_STATUS_ERROR, new Throwable());
        }
        // 获取坐标距离
        Address userAddress = addressService.findById(dbUser.getAddressId());
        User newUser = new User();
        Address location = new Address();
        BeanUtils.copyProperties(vo, location);
        String poiId = vo.getPoiId();
        addressService.checkAndSetBusiness(vo.getBusinessList(), poiId, vo.getAdCode());
        addressService.checkAndSet(location);
        if (!userAddress.getPoiId().equals(poiId)) {
            newUser.setAddressId(addressService.findByPoiId(poiId).getId());
        }
        newUser.setState(UserStateEnum.UNVERIFIED_SECOND.getCode());
        this.update(newUser);
        removeCache(uid);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unverifiedSecond() {
        User currentUser = accountHelp.getCurrentUser();
        User dbUser = this.findById(currentUser.getUid());
        if (!dbUser.getState().equals(UserStateEnum.UNVERIFIED_SECOND.getCode())) {
            throw new BizException(ErrorCodeConstant.ACCOUNT_STATUS_ERROR, new Throwable());
        }
        SendManage sendManage = new SendManage();
        sendManage.setIdentificationCode(UUID.randomUUID().toString());
        sendManage.setIsSend(false);
        sendManage.setSendUser(dbUser.getUid());
        sendManageService.save(sendManage);

        User newUser = new User();
        newUser.setUid(dbUser.getUid());
        newUser.setState(UserStateEnum.UNVERIFIED_THIRD.getCode());
        this.update(newUser);
        removeCache(dbUser.getUid());
        return true;
    }

    @Override
    public Boolean rollbackToUnverifiedFirst() {
        User currentUser = accountHelp.getCurrentUser();
        User dbUser = this.findById(currentUser.getUid());
        if (!dbUser.getState().equals(UserStateEnum.UNVERIFIED_SECOND.getCode())) {
            throw new BizException(ErrorCodeConstant.ACCOUNT_STATUS_ERROR, new Throwable());
        }
        User newUser = new User();
        newUser.setUid(dbUser.getUid());
        newUser.setState(UserStateEnum.UNVERIFIED_FIRST.getCode());
        this.update(newUser);
        removeCache(dbUser.getUid());
        return true;
    }

    @Override
    public Boolean unverifiedThird(String identificationCode) {
        User currentUser = accountHelp.getCurrentUser();
        User dbUser = this.findById(currentUser.getUid());
        if (!dbUser.getState().equals(UserStateEnum.UNVERIFIED_THIRD.getCode())) {
            throw new BizException(ErrorCodeConstant.ACCOUNT_STATUS_ERROR, new Throwable());
        }
        if (sendManageRepository.verifyQrcode(identificationCode, dbUser.getUid()) > 0) {
            User newUser = new User();
            newUser.setUid(dbUser.getUid());
            newUser.setState(UserStateEnum.ENABLE.getCode());
            this.update(newUser);
            removeCache(dbUser.getUid());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfo registerEnd(RegisterUser registerUser) {
        log.debug("当前用户为-------" + registerUser.toString());
        String phone = registerUser.getPhone();
        Optional.ofNullable(phone)
                .ifPresent(phoneNumber -> phoneVerification(phoneNumber, registerUser.getVerification()));
        return register(registerUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean modifyAddress(UnverifiedVo vo) {
        if (LocationUtils.parseNullResult(vo)) {
            throw new BizException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "地址");
        }
        accountHelp.checkCurrentAccountStatusOfEnable();
        User dbUser = accountHelp.getCurrentUser();
        Address location = new Address();
        BeanUtils.copyProperties(vo, location);
        Long addressId = addressService.checkAndSet(location);
        User newUser = User.builder().house(vo.getHouse()).uid(dbUser.getUid()).build();
        newUser = addressId.equals(dbUser.getAddressId())
                ? newUser
                : newUser.setAddressId(addressId).setState(UserStateEnum.UNVERIFIED_FIRST.getCode());
        this.update(newUser);
        removeCache(dbUser.getUid());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean modifyUser(UserVo vo) {
        if (StringUtils.isBlank(vo.getNickname()) && vo.getGender() == null && StringUtils.isBlank(vo.getAvatar())) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "修改信息");
        }
        accountHelp.checkCurrentAccountStatusOfEnable();
        User dbUser = accountHelp.getCurrentUser();
        if (StringUtils.isNotEmpty(vo.getNickname())) {
            dbUser.setNickname(vo.getNickname());
        }
        if (vo.getGender() != null) {
            dbUser.setGender(vo.getGender());
        }
        if (StringUtils.isNotEmpty(vo.getAvatar())) {
            dbUser.setAvatar(vo.getAvatar());
        }
        try {
            this.update(dbUser);
            removeCache(dbUser.getUid());
        } catch (DataIntegrityViolationException e) {
            throw new BizException(ErrorCodeConstant.GLOBAL_EXIST, new Throwable(), "昵称");
        }
        return true;
    }

    @Override
    public List<User> findListLikeNickName(String nickName) {
        User currentUser = accountHelp.getCurrentUser();
        User dbUser = this.findById(currentUser.getUid());
        return this.baseRepository.findListLikeNickName(nickName, dbUser.getAddressId());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Boolean modifyPhoneSecond(LoginUser user) {
        User currentUser = accountHelp.getCurrentUser();
        String phone = currentUser.getPhone();
        Object obj1 = redisService.get(SmsConstant.SMS_MODIFY_PHONE_FIRST_PREFIX + phone);
        Object obj2 = redisService.get(SmsConstant.SMS_MODIFY_PHONE_SECOND_PREFIX + user.getPhoneNumber());
        ExpFunction.true4ThrowBiz(obj1 == null || obj2 == null, ErrorCodeConstant.SMS_VERIFICATION_MISS);
        String oldVerification = obj1.toString();
        String verification = obj2.toString();
        ExpFunction.true4ThrowBiz(
                !oldVerification.equals(user.getOldVerification()) || !verification.equals(user.getVerification()),
                ErrorCodeConstant.SMS_VERIFICATION_ERROR);
        User dbUser = baseRepository.findPhone(phone);
        User newUser = new User();
        newUser.setUid(dbUser.getUid());
        newUser.setPhone(user.getPhoneNumber());
        this.update(newUser);
        redisService.remove(
                SmsConstant.TOKEN_PREFIX + currentUser.getUid(),
                SmsConstant.SMS_MODIFY_PHONE_FIRST_PREFIX + phone,
                SmsConstant.SMS_MODIFY_PHONE_SECOND_PREFIX + user.getPhoneNumber());
        removeCache(dbUser.getUid());
        return true;
    }

    @Override
    public void updateUser(User newUser) {
        this.update(newUser);
        removeCache(newUser.getUid());
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void qualify(QualifyVo qualifyVo) throws BizException {
        updateQualifyUser(qualifyVo, false);
        if (!mqService.sendQualify(JSON.toJSONString(qualifyVo))) {
            throw new BizException(ErrorCodeConstant.MQ_REQUEST_FAIL, new Throwable());
        }
    }

    private void updateQualifyUser(QualifyVo qualifyVo, boolean qualified) {
        User user;
        if (qualified) {
            user = findById(qualifyVo.getUid());
        } else {
            user = accountHelp.getCurrentUser();
            qualifyVo.setUid(user.getUid());
            Date current = new Date();
            qualifyVo.setCreateTime(current.getTime());
            user.setQualifyTime(current);
        }
        Assert.notNull(user, "无法获取当前用户");
        if (StringUtils.isNotEmpty(qualifyVo.getWxNumber())) {
            user.setWxNumber(qualifyVo.getWxNumber());
        }
        if (StringUtils.isNotEmpty(qualifyVo.getMail())) {
            user.setMail(qualifyVo.getMail());
        }
        if (StringUtils.isNotEmpty(qualifyVo.getProjectUrl())) {
            user.setProjectUrl(qualifyVo.getProjectUrl());
        }
        if (qualifyVo.getAuthType() != -1) {
            user.setAuthType(qualifyVo.getAuthType());
        }
        if (StringUtils.isNotEmpty(qualifyVo.getMemo())) {
            user.setMemo(qualifyVo.getMemo());
        }
        if (StringUtils.isNotEmpty(qualifyVo.getHobbies())) {
            user.setHobbies(qualifyVo.getHobbies());
        }
        if (StringUtils.isNotEmpty(qualifyVo.getQualifyUrl1())) {
            user.setQualifyUrl1(qualifyVo.getQualifyUrl1());
        }
        if (StringUtils.isNotEmpty(qualifyVo.getQualifyUrl2())) {
            user.setQualifyUrl2(qualifyVo.getQualifyUrl2());
        }
        if (StringUtils.isNotEmpty(qualifyVo.getQualifyUrl3())) {
            user.setQualifyUrl3(qualifyVo.getQualifyUrl3());
        }
        if (qualified) {
            user.setAuthState(User.AUTH_STATE_SUCCESS);
        } else {
            user.setAuthState(User.AUTH_STATE_IN);
        }
        if (StringUtils.isNotEmpty(qualifyVo.getCharacterName())) {
            user.setCharacterName(qualifyVo.getCharacterName());
        }
        if (StringUtils.isNotEmpty(qualifyVo.getCharacterUrl())) {
            user.setCharacterUrl(qualifyVo.getCharacterUrl());
        }
        this.updateUser(user);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void qualifyCallback(QualifyVo qualifyVo) {
        updateQualifyUser(qualifyVo, true);
        sensorAnalytics.track(qualifyVo.getUid(), true, "identityAuth", null);
    }

    @Override
    public List<User> findAllByIds(Set<String> ids) {
        return baseRepository.findAllById(ids);
    }

    @Override
    public Integer getMaxId() {
        return baseRepository.maxId();
    }

    @Override
    public Integer getCurrentId(String uid) {
        return baseRepository.findIdByUid(uid);
    }

    @Override
    public List<User> findAllByIds(List<String> ids) {
        return baseRepository.findAllById(ids);
    }

    @Override
    public User findByOpenId(String openId) {
        return baseRepository.findByOpenId(openId);
    }

    @Override
    public UserInfo findByUnionId(String unionId) {
        User user = userRepository.findByUnionId(unionId);
        ExpFunction.true4ThrowBiz(user == null, WECHAT_NOT_EXIST);
        return buildUserInfo(user);
    }

    @Override
    public User findUnionId2User(String unionId) {
        return userRepository.findByUnionId(unionId);
    }

    @Override
    public Boolean isWeChatBind(String unionId) {
        return null != userRepository.findByUnionId(unionId);
    }

    /**
     * Author: Tablo
     *
     * <p>
     *
     * <p>Description:[根据账号查询用户信息] Created on 21:09 2019/09/16
     *
     * @param uid 用户主键
     * @return com.quantumtime.qc.vo.UserVo
     */
    @Override
    public UserVo findUserInfo(String uid) {
        User user = findById(uid);
        return UserVo.builder()
                .uid(user.getUid())
                .uid(user.getUid())
                .avatar(user.getAvatar())
                .signature(user.getSignature())
                .nickname(user.getNickname())
                .account(user.getAccount())
                .mail(user.getMail())
                .phone(user.getPhone())
                .addressId(user.getAddressId())
                .house(user.getHouse())
                .build();
    }

    @Override
    public List<SysUser> findSysUser() {
        List<SysUser> res = new ArrayList<>();
        List<Object[]> list = this.baseRepository.findSysUser();
        if (list != null && !list.isEmpty()) {
            list.forEach(item -> {
                SysUser sysUser = new SysUser();
                sysUser.setUid(item[0].toString());
                sysUser.setUserNickName(item[1].toString());
                sysUser.setUserPhone(item[2].toString());
                sysUser.setOpPhone(item[3].toString());
                sysUser.setOpName(item[4].toString());
                res.add(sysUser);
            });
        }
        return res;
    }

    @Override
    public int countByDept(Integer deptId) {
        return baseRepository.countByDept(deptId);
    }

    @Override
    @CachePut(value = "cache-user", key = "'user'+#entity.uid")
    public User save(User entity) throws JpaCrudException {
        return super.save(entity);
    }

    @CachePut(value = "cache-user", key = "'user'+#entity.uid")
    @Override
    public User update(User entity, Boolean... isUpdate) throws JpaCrudException {
        return super.update(entity);
    }

    @SuppressWarnings("unchecked")
    private void removeCache(String userId) {
        redisService.remove("cache-user::user" + userId);
    }

    /**
     * Author: Tablo
     *
     * <p>Description:[判断手机验证码] Created on 16:56 2019/10/08
     *
     * @param phone 手机号
     * @param verification 验证码
     */
    @SuppressWarnings("unchecked")
    private void phoneVerification(String phone, String verification) {
        true4Throw(!isLegalPhone(phone), new BizVerifyException(VERIFY_PHONE_FORMAT_ERROR, new Throwable()));
        true4ThrowBiz(
                !Objects.equals(redisService.get(SMS_REGISTER_PREFIX + phone).toString(), verification),
                SMS_VERIFICATION_ERROR);
    }

    /**
     * Author: Tablo
     *
     * <p>Description:[基本的注册功能] Created on 16:56 2019/10/08
     *
     * @param register 注册信息
     * @return com.quantumtime.qc.vo.login.LoginResult
     */
    private UserInfo register(RegisterUser register) {
        User user = new User();
        BeanUtils.copyProperties(register, user);
        AddressParam addressParam = register.getAddressParam();
        boolean build = addressParam != null && StringUtils.isNotBlank(addressParam.getAddress().getPoiId());
        // 构建用户默认的基本属性
        user = build
                ? buildDefaultPortrait(user).setAddressId(addressService.checkAndSetAddress(addressParam))
                : buildDefaultPortrait(user);
        //noinspection unchecked
        Optional.ofNullable(register.getPhone()).ifPresent(phone -> redisService.remove(SMS_REGISTER_PREFIX + phone));
        return buildUserInfo(
                save(user.setScore(DEFAULT_SCORE).setScoreFreeze(DEFAULT_SCORE).setScoreTotal(DEFAULT_SCORE)));
    }

    /**
     * Author: Tablo
     *
     * <p>Description:[构建用户头像属性] Created on 16:07 2019/10/08
     *
     * @param user 用户实体
     * @return com.quantumtime.qc.entity.User
     */
    private User buildAvatar(User user) {
        String avatar = user.getAvatar();
        return user.setAvatar(
                StringUtils.isNotBlank(avatar)
                        ? avatar
                        : defaultAvatarsProperties.getDefaultAvatars().get(new Random()
                                .nextInt(defaultAvatarsProperties.getDefaultAvatars().size() - 1)));
    }

    /**
     * Author: Tablo
     *
     * <p>Description:[配置用户默认属性] Created on 15:31 2019/10/08
     *
     * @param user 用户对象
     * @return com.quantumtime.qc.entity.User
     */
    private User buildDefaultPortrait(User user) {
        String wxNickName = user.getWxNickname();
        String phone = user.getPhone();
        String unionId = user.getUnionId();
        buildAvatar(user)
                .setPassword(UUID.randomUUID().toString())
                .setDept(5)
                .setState(ENABLE.getCode())
                .setStarSum(0)
                .setFanSum(0);
        return isLegalUnionId(unionId)
                ? checkUnionId(user)
                        .setNickname(wxNickName)
                        .setUnionId(unionId)
                        .setWxNumber(user.getWxNumber())
                        .setWxNickname(wxNickName)
                : user.setNickname(buildPhoneNickname(phone));
    }

    private String buildPhoneNickname(String phone) {
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    @Override
    public UserInfo personalDetail(String uid) {
        UserInfo userInfo = buildUserInfo(userRepository.findByUid(uid));
        int opusSum = 0;
        if (uid.equals(accountHelp.getCurrentUser().getUid())) {
            opusSum = videoRepository.coverSum(uid);
        } else {
            opusSum = videoRepository.coverOtherSum(uid);
        }
        userInfo.setOpusSum(opusSum);
        return userInfo;
    }

    private User checkUnionId(User user) {
        true4ThrowBiz(!ObjectUtils.isEmpty(baseRepository.findByUnionId(user.getUnionId())), GLOBAL_EXIST, "该微信");
        return user;
    }

    @SuppressWarnings("WeakerAccess")
    public UserInfo buildUserInfo(User user) {
        Long addressId = user.getAddressId();
        return user.convert2Info()
                .setAddress(addressId == null ? null : addressService.findAddressById(addressId))
                .setClickSum(clickContentRepository.userLikeSum(user.getUid()));
    }

    @Override
    public Boolean changeBirthday(String birthday) {
        String uid = accountHelp.getCurrentUser().getUid();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(birthday);
            User user = userRepository.getOne(uid);
            user.setBirthday(date);
            userService.save(user);
            return true;
        } catch (Exception e) {
            log.error("birthday" + birthday);
            throw new BizException(ErrorCodeConstant.DATE_TYPE_ERROR, new Throwable());
        }
    }

    /**
     * Author: wmw Description:[修改用户性别] Created on 2019/10/15
     *
     * @param gender 性别1
     * @return java.lang.Boolean
     */
    @Override
    public Boolean updateGender(Integer gender) {
        String uid = accountHelp.getCurrentUser().getUid();
        User user = userRepository.getOne(uid);
        user.setGender(gender);
        userService.save(user);
        return true;
    }

    @Override
    public List<Address> queryRecords(String currentUid, Long currentAddressId) {
        List<AddressRecord> recordList = recordRepository.findByUid(currentUid);
        return recordList.isEmpty() ? null : buildRecords(recordList, currentAddressId);
    }

    private List<Address> buildRecords(List<AddressRecord> recordList, Long currentAddressId) {
        Map<Long, List<AddressRecord>> records = recordList.stream()
                .filter(record -> record == null || !currentAddressId.equals(record.getAddressId()))
                .collect(Collectors.groupingBy(AddressRecord::getAddressId));
        TreeMap<Long, Long> recordMap = new TreeMap<>();
        // key为RecordID,v = addressId
        records.forEach(
                (key, value) -> recordMap.put(value.stream().mapToLong(AddressRecord::getId).max().orElse(0L), key));
        List<Address> unsorted = addressService.findAllByIds(records.keySet());
        TreeMap<Long, Address> result = new TreeMap<>();
        recordMap.forEach((key, value) ->
                result.put(
                        key,
                        unsorted.stream()
                                .filter(address -> address.getId().equals(value))
                                .findAny()
                                .orElse(null)));
        return result.descendingKeySet().stream().map(result::get).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRecords(String uid, Long addressId) {
        return recordRepository.deleteAddressRecordByUidAndAddressId(uid, addressId) >= 0;
    }

    @Override
    public Boolean updateAddress(AddressParam addressParam) {
        ExpFunction.true4ThrowBiz(
                addressParam.getAddress().getPoiId() == null, ErrorCodeConstant.VERIFY_NOT_NULL, "地址");
        String uid = accountHelp.getCurrentUser().getUid();
        userService.save(
                userRepository
                        .getOne(uid)
                        .setAddressId(
                                recordRepository
                                        .save(
                                                AddressRecord.builder()
                                                        .uid(uid)
                                                        .addressId(addressService.checkAndSetAddress(addressParam))
                                                        .createTime(LocalDateTime.now())
                                                        .build())
                                        .getAddressId()));
        return true;
    }

    @Override
    public List<FansOrStarListVo> fansList(FansOrStarListWarp fansOrStarListWarp) {
        List<FansOrStarListVo> warpList = starFansDetail(fansOrStarListWarp, 1);

        return warpList;
    }

    @Override
    public List<FansOrStarListVo> starList(FansOrStarListWarp fansOrStarListWarp) {
        List<FansOrStarListVo> warpList = starFansDetail(fansOrStarListWarp, 2);
        return warpList;
    }

    public List<FansOrStarListVo> starFansDetail(FansOrStarListWarp fansOrStarListWarp, int type) {
        Integer offset = fansOrStarListWarp.getPageSize() * (fansOrStarListWarp.getPageNum() - 1);
        List<StarFan> starFans = new ArrayList<>();
        List<FansOrStarListVo> warpList = new ArrayList<>();
        if (type == 1) {
            starFans =
                    starFanRepository.fansList(fansOrStarListWarp.getMyUid(), offset, fansOrStarListWarp.getPageSize());
        } else if (type == 2) {
            starFans = starFanRepository.starsList(
                    fansOrStarListWarp.getMyUid(), offset, fansOrStarListWarp.getPageSize());
        }

        if (starFans.size() == 0) {
            warpList.add(null);
        } else {
            List<String> uidList = starFans.stream().map(StarFan::getFanUid).collect(Collectors.toList());

            StarFanListVo starFanListVo = new StarFanListVo();
            starFanListVo.setThisUid(fansOrStarListWarp.getMyUid());
            starFanListVo.setUidList(uidList);
            List<Map<String, Integer>> relationMap = starFanService.starFanList(starFanListVo);
            List<User> userList = userRepository.findByUidList(uidList);
            userList.forEach(users ->
                    relationMap.stream()
                            .map(map -> new FansOrStarListVo()
                                    .setUid(users.getUid())
                                    .setAvatar(users.getAvatar())
                                    .setNickName(users.getNickname())
                                    .setId(users.getId()))
                            .forEach(warpList::add));
        }
        return warpList;
    }

    @Override
    public void reportUser(ReportUserRequest reportUserRequest) throws Exception {
        ReportUser reportUser =
                reportUserRepository.findExist(reportUserRequest.getUid(), reportUserRequest.getReportUid());
        if (reportUser != null) {
            throw new Exception("已经举报了");
        }
        reportUser = new ReportUser();
        reportUser.setUid(reportUserRequest.getUid());
        reportUser.setReportUid(reportUserRequest.getReportUid());
        reportUser.setReason(reportUserRequest.getReason());
        reportUser.setStatus(ReportUser.STATUS_NOT_PROCESSED);
        reportUser.setCreateTime(new Date());
        reportUserRepository.save(reportUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserBlacklist(String fromUid, String toUid) throws Exception {
        UserBlacklist userBlacklist = userBlacklistRepository.findExist(fromUid, toUid);
        if (userBlacklist != null) {
            throw new Exception("已经拉黑了");
        }
        userBlacklist = new UserBlacklist();
        userBlacklist.setFromUid(fromUid);
        userBlacklist.setToUid(toUid);
        userBlacklist.setCreateTime(new Date());
        starFanService.unsubscribe2Way(fromUid,toUid);
        userBlacklistRepository.save(userBlacklist);
    }

    @Override
    public void removeUserBlacklist(String fromUid, String toUid) {
        UserBlacklist userBlacklist = userBlacklistRepository.findExist(fromUid, toUid);
        if (userBlacklist != null) {
            userBlacklistRepository.delete(userBlacklist);
        }
    }

    @Override
    public List<User> getAllUserBlacklist(String uid) {
        return userRepository.getBlackListUsers(uid);
    }

    @Override
    public boolean checkInBlacklist(String fromUid, String toUid) {
        UserBlacklist userBlacklist = userBlacklistRepository.findExist(fromUid, toUid);
        if (userBlacklist != null) {
            return true;
        }
        return false;
    }
}
