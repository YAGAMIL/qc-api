package com.quantumtime.qc.utils;

import com.quantumtime.qc.service.IUserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * <p>
 * Description:用户Id工具类
 * Program:qc-api
 * </p>
 * Created on 2019-10-12 15:21
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Component
public class UidUtil {

    /**
     * id最小值
     */
    private static final int MIN_ID = 100000;
    /**
     * 静态实例
     */
    private static UidUtil util;
    @Resource
    private IUserService userService;

    /**
     * Author: Tablo
     * <p> Description:[构建新用户的Id]
     * Created on 16:29 2019/10/12
     *
     * @return java.lang.Integer
     **/
    public static Integer buildUid() {
        Integer maxId = util.userService.getMaxId();
        return maxId != null && MIN_ID <= maxId ? ++maxId : MIN_ID;
    }

    @PostConstruct
    public void init() {
        util = this;
    }
}