package com.quantumtime.qc.vo.notification;

import com.quantumtime.qc.wrap.notification.NotificationWrap;
import lombok.Data;

import java.util.List;

@Data
public class AggregationNotification {

    private Integer type;

    private String title;

    private Integer size;

    private List<Long> notificationIds;

    private NotificationWrap notificationWrap;
}
