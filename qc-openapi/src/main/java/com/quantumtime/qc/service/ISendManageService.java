package com.quantumtime.qc.service;

import com.quantumtime.qc.common.service.IBaseService;
import com.quantumtime.qc.entity.undo.SendManage;
import com.quantumtime.qc.vo.SendManageDetailsVo;

import java.io.IOException;

public interface ISendManageService extends IBaseService<SendManage, Long> {


    /**
     * 查看详情
     * @param sendMessageId
     * @return
     */
    SendManageDetailsVo viewDetails(Long sendMessageId) throws IOException;



    Integer updateIsSend(Long id, Boolean isSend);


}
