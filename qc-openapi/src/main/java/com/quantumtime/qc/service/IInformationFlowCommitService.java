package com.quantumtime.qc.service;

import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.common.service.IBaseService;
import com.quantumtime.qc.entity.information.InformationFlowCommit;
import com.quantumtime.qc.wrap.information.InformationFlowDetailCommitWrap;

public interface IInformationFlowCommitService extends IBaseService<InformationFlowCommit, Long> {


    InformationFlowCommit commitInformationFlow(InformationFlowCommit entity);

    Boolean removeCommitInformation(Long id);

    BasePage<InformationFlowDetailCommitWrap, Long> findPage(BasePage<InformationFlowCommit, Long> page);

    InformationFlowDetailCommitWrap findWrapById(Long id);

}
