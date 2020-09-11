package com.quantumtime.qc.service;

import java.util.Map;

public interface ISensorAnalytics {
    void track(String distinctId, boolean isLoginId, String eventName, Map<String, Object> properties);
    void trackSignUp(String registerId, String anonymousId);
}
