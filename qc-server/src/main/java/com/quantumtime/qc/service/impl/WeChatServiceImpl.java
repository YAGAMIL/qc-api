//package com.quantumtime.qc.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.quantumtime.qc.common.constant.ErrorCodeConstant;
//import com.quantumtime.qc.common.constant.SmsConstant;
//import com.quantumtime.qc.common.exception.BizException;
//import com.quantumtime.qc.common.exception.BizVerifyException;
//import com.quantumtime.qc.constant.OpenIdConstant;
//import com.quantumtime.qc.entity.User;
//import com.quantumtime.qc.common.enums.UserStateEnum;
//import com.quantumtime.qc.service.IAddressService;
//import com.quantumtime.qc.service.IRedisService;
//import com.quantumtime.qc.service.IUserService;
//import com.quantumtime.qc.service.IWeChatService;
//import com.quantumtime.qc.utils.AesCbcUtil;
//import com.quantumtime.qc.common.utils.VerifyUtil;
//import com.quantumtime.qc.vo.Location;
//import com.quantumtime.qc.vo.amap.response.CoordinateConvertResponse;
//import com.quantumtime.qc.vo.amap.response.PlaceAroundPOIDetailResponse;
//import com.quantumtime.qc.vo.amap.response.PlaceAroundResponse;
//import com.quantumtime.qc.vo.wechat.OpenIdDetail;
//import com.quantumtime.qc.vo.wechat.WeChatLoginVo;
//import com.quantumtime.qc.vo.wechat.decode.UserInfoDecode;
//import com.quantumtime.qc.vo.wechat.response.WeChatLoginResponse;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class WeChatServiceImpl implements IWeChatService {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Value("${WeChat.loginUrl}")
//    private String loginUrl;
//
//    @Value("${WeChat.appid}")
//    private String appid;
//
//    @Value("${WeChat.secret}")
//    private String secret;
//
//    @Value("${WeChat.grant_type}")
//    private String grantType;
//
//    @Value("${Amap.amapKey}")
//    private String amapKey;
//
//    @Value("${Amap.coordinateConvertUrl}")
//    private String coordinateConvertUrl;
//
//    @Value("${Amap.placeAroundUrl}")
//    private String placeAroundUrl;
//
//    @Autowired
//    private IUserService userService;
//
//    @Autowired
//    private IAddressService addressService;
//
//    @Autowired
//    private IRedisService redisService;
//
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String login(WeChatLoginVo loginVo) {
//        WeChatLoginResponse response = null;
//        String phoneNumber = null;
//        try {
//            StringBuilder requestUrl = new StringBuilder();
//            requestUrl.append(loginUrl);
//            requestUrl.append("?");
//            requestUrl.append("appid=").append(appid);
//            requestUrl.append("&secret=").append(secret);
//            requestUrl.append("&js_code=").append(loginVo.getJsCode());
//            requestUrl.append("&grant_type=").append(grantType);
//            String respString = restTemplate.getForObject(requestUrl.toString(), String.class);
//            response = JSON.parseObject(respString, WeChatLoginResponse.class);
//        }catch (Exception e){
//            throw new BizException(ErrorCodeConstant.ACCOUNT_WECHAT_LOGIN_ERROR, e);
//        }
//        if(response != null && response.getErrcode() == null && StringUtils.isEmpty(response.getErrmsg())){
//            String openid = response.getOpenid();
//            String session_key = response.getSession_key();
//            //获取解密后的userInfo
//            String decrypt = null;
//            decrypt = AesCbcUtil.decrypt(loginVo.getUserInfo(), session_key, loginVo.getUserInfoIv(), "UTF-8");
//            if(StringUtils.isEmpty(decrypt)){
//                throw new BizException(ErrorCodeConstant.ACCOUNT_WECHAT_LOGIN_ERROR, new Throwable());
//            }
//            UserInfoDecode userInfoDecode = JSON.parseObject(decrypt, UserInfoDecode.class);
//            if(StringUtils.isEmpty(userInfoDecode.getUnionId()) || StringUtils.isEmpty(userInfoDecode.getOpenId())){
//                throw new BizException(ErrorCodeConstant.ACCOUNT_WECHAT_LOGIN_ERROR, new Throwable());
//            }
//            //获取解密后的手机号
//            decrypt = AesCbcUtil.decrypt(loginVo.getPhoneNumber(), session_key, loginVo.getPhoneNumberIv(), "UTF-8");
//            if(StringUtils.isEmpty(decrypt)){
//                throw new BizException(ErrorCodeConstant.ACCOUNT_WECHAT_LOGIN_ERROR, new Throwable());
//            }
//            Object purePhoneNumber = JSON.parseObject(decrypt).get("purePhoneNumber");
//            if(purePhoneNumber != null){
//                phoneNumber = purePhoneNumber.toString();
//                if(!VerifyUtil.isPhone(phoneNumber)){
//                    throw new BizVerifyException(ErrorCodeConstant.VERIFY_PHONE_FORMAT_ERROR, new Throwable());
//                }
//            }else{
//                throw new BizException(ErrorCodeConstant.ACCOUNT_WECHAT_LOGIN_ERROR, new Throwable());
//            }
//            User user = userService.findByPhone(phoneNumber);
//            //之前注册过
//            //如果Unionid或openid不一致,抛异常，如果没有 则更新
//            if(user != null){
//                if(StringUtils.isNotEmpty(user.getUnionid())){
//                    if(!user.getUnionid().equals(userInfoDecode.getUnionId())) {
//                        throw new BizException(ErrorCodeConstant.ACCOUNT_WECHAT_LOGIN_ERROR, new Throwable());
//                    }else{
//                        String openIds = user.getOpenId();
//                        List<OpenIdDetail> openIdDetails = JSON.parseArray(openIds, OpenIdDetail.class);
//                        List<OpenIdDetail> collect = openIdDetails.stream().filter(o -> o.getSourceType().equals(OpenIdConstant.WECHAT_APPLET)).collect(Collectors.toList());
//                        if(collect == null || collect.size() == 0){
//                            OpenIdDetail detail = new OpenIdDetail();
//                            detail.setOpenId(openid);
//                            detail.setSourceType(OpenIdConstant.WECHAT_APPLET);
//                            collect.add(detail);
//                            User dbUser = new User();
//                            dbUser.setUid(user.getUid());
//                            dbUser.setOpenId(JSON.toJSONString(collect));
//                            userService.update(dbUser);
//                        }
//                    }
//                }else{
//                    //如果Unionid都没有那么就默认openid也没有
//                    List<OpenIdDetail> collect = new ArrayList<>();
//                    OpenIdDetail detail = new OpenIdDetail();
//                    detail.setOpenId(openid);
//                    detail.setSourceType(OpenIdConstant.WECHAT_APPLET);
//                    collect.add(detail);
//                    user.setUnionid(userInfoDecode.getUnionId());
//                    user.setOpenId(JSON.toJSONString(collect));
//                    userService.update(user);
//                }
//            }else{
//                //之前没注册就去注册
//                user = new User();
//                List<OpenIdDetail> collect = new ArrayList<>();
//                OpenIdDetail detail = new OpenIdDetail();
//                detail.setOpenId(openid);
//                detail.setSourceType(OpenIdConstant.WECHAT_APPLET);
//                collect.add(detail);
//                user.setUnionid(userInfoDecode.getUnionId());
//                user.setOpenId(JSON.toJSONString(collect));
//                user.setNickname(userInfoDecode.getNickName());
//                user.setPhone(phoneNumber);
//                user.setPassword(UUID.randomUUID().toString());
//                user.setGender(null);
//                user.setAvatar(userInfoDecode.getAvatarUrl());
//                user.setState(UserStateEnum.UNVERIFIED_FIRST.getCode());
//                user.setDept(5);
//                //地址为空
//                user.setAddressId(null);
//                userService.save(user);
//            }
//            JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(phoneNumber);
//            userDetails.setSessionKey(response.getSession_key());
//            String token = jwtTokenUtil.generateToken(userDetails);
//            //判断是否已经是登录态
//            if(redisService.exists(SmsConstant.WECHATTOKEN + phoneNumber)){
//                redisService.remove(SmsConstant.WECHATTOKEN + phoneNumber);
//            }
//            redisService.addMap(SmsConstant.WECHATTOKEN + phoneNumber, token, userDetails, jwtTokenUtil.getExpiration()/1000);
//            return token;
//        }else{
//            throw new BizException(ErrorCodeConstant.ACCOUNT_WECHAT_LOGIN_ERROR, new Throwable());
//        }
//    }
//
//    public Long checkAndSetAMapAddress(Double longitude, Double latitude){
//        StringBuilder requestUrl = new StringBuilder();
//        requestUrl.append(coordinateConvertUrl);
//        requestUrl.append("?");
//        requestUrl.append("key=").append(amapKey);
//        requestUrl.append("&locations=").append(String.valueOf(longitude) + "," + String.valueOf(latitude));
//        requestUrl.append("&coordsys=").append("gps");
//        requestUrl.append("&output=").append("JSON");
//
//        String respJson = restTemplate.getForObject(requestUrl.toString(), String.class);
//        CoordinateConvertResponse coordinateConvertResponse = JSON.parseObject(respJson, CoordinateConvertResponse.class);
//        if(coordinateConvertResponse == null ||
//                coordinateConvertResponse.getStatus().intValue() == 0 ||
//                StringUtils.isEmpty(coordinateConvertResponse.getLocations())){
//            throw new BizException(ErrorCodeConstant.ACCOUNT_WECHAT_LOGIN_ERROR, new Throwable());
//        }else{
//            requestUrl = new StringBuilder();
//            requestUrl.append(placeAroundUrl);
//            requestUrl.append("?");
//            requestUrl.append("key=").append(amapKey);
//            requestUrl.append("&locations=").append(coordinateConvertResponse.getLocations());
//            requestUrl.append("&types=").append("120300");
//            requestUrl.append("&radius=").append("50000");
//            requestUrl.append("&sortrule=").append("distance");
//            requestUrl.append("&offset=").append("1");
//            requestUrl.append("&extensions=").append("all");
//            requestUrl.append("&output=").append("JSON");
//
//            respJson = restTemplate.getForObject(requestUrl.toString(), String.class);
//            PlaceAroundResponse placeAroundResponse = JSON.parseObject(respJson, PlaceAroundResponse.class);
//            if(placeAroundResponse == null ||
//                placeAroundResponse.getStatus().equals("0") ||
//                placeAroundResponse.getPois() == null ||
//                placeAroundResponse.getPois().size() == 0){
//                throw new BizException(ErrorCodeConstant.ACCOUNT_WECHAT_LOGIN_ERROR, new Throwable());
//            }else{
//                PlaceAroundPOIDetailResponse detailResponse = placeAroundResponse.getPois().get(0);
//                Location location = new Location();
//                location.setCityCode(detailResponse.getCitycode());
//                location.setCityName(detailResponse.getCityname());
//                location.setAreaCode(detailResponse.getAdcode());
//                location.setAreaName(detailResponse.getAdname());
//                location.setCommunityAddress(detailResponse.getAddress());
//                location.setCommunityCode(detailResponse.getId());
//                location.setCommunityName(detailResponse.getName());
//                String[] coordinate = detailResponse.getLocation().split(",");
//                location.setLongitude(Double.parseDouble(coordinate[0]));
//                location.setLatitude(Double.parseDouble(coordinate[1]));
//                return addressService.checkAndSet(location);
//            }
//        }
//    }
//}
