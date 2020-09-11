package com.quantumtime.qc.service;

import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.common.service.IBaseService;
import com.quantumtime.qc.entity.information.InformationFlowTheme;
import com.quantumtime.qc.wrap.information.InformcationFlowThemeWrap;

public interface IInformationFlowThemeService extends IBaseService<InformationFlowTheme, Long> {

    /**
     * 模糊查询话题
     * @param page
     * @return
     */
    BasePage<InformcationFlowThemeWrap, Long> findThemeList(BasePage<InformationFlowTheme, Long> page);

    Boolean initTheme(Long addressId);

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



}
