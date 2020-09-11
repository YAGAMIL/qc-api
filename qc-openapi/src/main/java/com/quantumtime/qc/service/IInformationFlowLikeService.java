package com.quantumtime.qc.service;

import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.common.service.IBaseService;
import com.quantumtime.qc.entity.information.InformationFlowLike;
import com.quantumtime.qc.wrap.information.InformationFlowDetailLikeWrap;

public interface IInformationFlowLikeService extends IBaseService<InformationFlowLike, Long> {

    InformationFlowLike likeInformationFlow(InformationFlowLike entity);

    Boolean removeLikeInformation(Long id);

    BasePage<InformationFlowDetailLikeWrap, Long> findPage(BasePage<InformationFlowLike, Long> page);

    InformationFlowDetailLikeWrap findWrapById(Long id);

    InformationFlowLike findByinformationFlowIdAndCreateId(Long informationFlowId, String createId);

}
