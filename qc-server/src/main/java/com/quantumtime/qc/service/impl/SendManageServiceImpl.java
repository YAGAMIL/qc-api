package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.service.impl.BaseServiceImpl;
import com.quantumtime.qc.entity.undo.SendManage;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.repository.SendManageRepository;
import com.quantumtime.qc.service.ISendManageService;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.utils.QrCodeCreateUtil;
import com.quantumtime.qc.vo.SendManageDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class SendManageServiceImpl extends BaseServiceImpl<SendManage, Long, SendManageRepository> implements ISendManageService {

    @Autowired
    private IUserService userService;

    @Override
    public SendManageDetailsVo viewDetails(Long sendMessageId) throws IOException{
        SendManageDetailsVo vo = new SendManageDetailsVo();
        SendManage sendManage = this.findById(sendMessageId);
        User user = userService.findById(sendManage.getSendUser());
        vo.setUser(user);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        QrCodeCreateUtil.createQrCode(baos, sendManage.getIdentificationCode(), 900, "JPEG");
        File logo = ResourceUtils.getFile("classpath:images/logo.png");
        QrCodeCreateUtil.drawLogoQRCode(baos, logo, sendManage.getIdentificationCode(), "扫描二维码完成身份验证", "png");
        BASE64Encoder encoder = new BASE64Encoder();
        vo.setBase64QrCode(encoder.encode(baos.toByteArray()));
        vo.setSendManage(sendManage);
        return vo;
    }

    @Override
    public Integer updateIsSend(Long id, Boolean isSend) {
        return this.baseRepository.updateIsSend(id, isSend);
    }
}
