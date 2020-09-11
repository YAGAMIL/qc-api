package com.quantumtime.qc.service;

import com.quantumtime.qc.common.service.IBaseService;
import com.quantumtime.qc.entity.undo.RelUserAndTheme;

public interface IRelUserAndThemeService extends IBaseService<RelUserAndTheme, Long> {

    /**
     * 加入圈子
     * @param themeId
     * @return
     */
    Boolean joinTheme(Long themeId);

    /**
     * 取消加入圈子
     * @param themeId
     * @return
     */
    Boolean unjoinTheme(Long themeId);


    RelUserAndTheme findRelByUserIdAndThemeId(String userId, Long themeId);
}
