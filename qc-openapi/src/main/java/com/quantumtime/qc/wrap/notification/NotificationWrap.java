package com.quantumtime.qc.wrap.notification;

import com.quantumtime.qc.entity.undo.Notification;
import lombok.Data;

@Data
public class NotificationWrap {
    private Notification notification;

    private String sourceUserNickName;

    private String content;

    private String sourceContentPrefix;

    private String sourceUserAvatar;

}
