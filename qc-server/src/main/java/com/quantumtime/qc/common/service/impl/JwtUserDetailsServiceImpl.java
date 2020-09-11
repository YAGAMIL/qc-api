//package com.quantumtime.qc.common.service.impl;
//
//import com.quantumtime.qc.common.constant.ErrorCodeConstant;
//import com.quantumtime.qc.common.exception.BizException;
//import com.quantumtime.qc.common.jwt.JwtUser;
//import com.quantumtime.qc.entity.User;
//import com.quantumtime.qc.common.enums.UserStateEnum;
//import com.quantumtime.qc.service.IUserService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//
//@Slf4j
//@Service
//public class JwtUserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private IUserService userService;
//
//    @Override
//    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
//        User user = userService.findByPhone(phone);
//        if (user == null || StringUtils.isEmpty(user.getUid())) {
//            throw new BizException(ErrorCodeConstant.GLOBAL_NOT_EXIST, new Throwable(), "用户");
//        } else if(user.getState().equals(UserStateEnum.DISABLE.getCode()) || user.getState().equals(UserStateEnum.DELETE.getCode())){
//            throw new BizException(ErrorCodeConstant.ACCOUNT_STATUS_ERROR, new Throwable());
//        } else {
//            return new JwtUser(user.getUid(), user.getAccount(), user.getNickname(),  user.getPhone(), user.getPassword(), user.getState(), null, null);
//        }
//    }
//}
