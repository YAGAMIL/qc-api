package com.quantumtime.qc.service;

import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.common.service.IBaseService;
import com.quantumtime.qc.entity.information.InformationFlow;
import com.quantumtime.qc.wrap.information.InformationFlowDetailForwardWrap;
import com.quantumtime.qc.wrap.information.InformationFlowWrap;

public interface IInformationFlowService extends IBaseService<InformationFlow, Long> {

    /**
     * 分页查询发现信息流
     * @param page
     * @return
     */
    BasePage<InformationFlowWrap, Long> findPage(BasePage<InformationFlow, Long> page);



    /**
     * 发布信息流
     * @return
     */
    InformationFlow publishInformationFlow(InformationFlow informationFlow);


    /**
     * 查询详情
     * @param id
     * @return
     */
    InformationFlowWrap findWrapById(Long id);


    /**
     * 我发布的消息
     * @param page
     * @return
     */
    BasePage<InformationFlowWrap, Long> findPublishList(BasePage<InformationFlow, Long> page);

    /**
     * 查找信息流转发list
     * @param page
     * @return
     */
    BasePage<InformationFlowDetailForwardWrap, Long> findForwardList(BasePage<InformationFlow, Long> page);

    Long countNewInformation(Long oldId);


    void updateLikeNum(Long id, Long num);

    void updateCommitNum(Long id, Long num);

    void updateForwardNum(Long id, Long num);


}
