package com.quantumtime.qc.service;

import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.common.service.IBaseService;
import com.quantumtime.qc.entity.undo.Notification;
import com.quantumtime.qc.vo.notification.AggregationNotification;
import com.quantumtime.qc.wrap.notification.NotificationWrap;

import java.util.List;

public interface INotificationService extends IBaseService<Notification, Long> {

    Long countUnReadNotification(String userId);


    BasePage<NotificationWrap, Long> findPage(BasePage<Notification, Long> page);


    List<AggregationNotification> findAggregationNotificationList(String userId);

    List<NotificationWrap> findByIds(List<Long> ids);

    Boolean isReadByIds(List<Long> ids);

    Notification pushNotification(Notification notification);





}
