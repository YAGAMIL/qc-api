package com.quantumtime.qc.service;

import com.quantumtime.qc.entity.undo.Notification;

public interface AliPushService {

    void push(Notification notification);
}
