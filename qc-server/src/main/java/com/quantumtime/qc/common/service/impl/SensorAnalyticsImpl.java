package com.quantumtime.qc.common.service.impl;

import com.quantumtime.qc.service.ISensorAnalytics;
import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class SensorAnalyticsImpl implements ISensorAnalytics {

    @Value("${sensorsdata.path}")
    private String path;

    @Value("${sensorsdata.flush}")
    private int flushNum;

    private AtomicInteger counter = new AtomicInteger();

    private SensorsAnalytics sa;

    @PostConstruct
    public void init() throws IOException {
        sa = new SensorsAnalytics(
                new SensorsAnalytics.ConcurrentLoggingConsumer(path));
    }

    @PreDestroy
    public void close() {
        if (sa != null) {
            sa.shutdown();
        }
    }


    @Override
    public void track(String distinctId, boolean isLoginId, String eventName, Map<String, Object> properties) {
        try {
            sa.track(distinctId, isLoginId, eventName, properties);
            if (counter.incrementAndGet() == flushNum) {
                sa.flush();
                counter.set(0);
            }
        } catch (InvalidArgumentException e) {
            log.error("数据收集失败", e);
        }
    }

    @Override
    public void trackSignUp(String registerId, String anonymousId) {
        try {
            sa.trackSignUp(registerId, anonymousId);
            if (counter.incrementAndGet() == flushNum) {
                sa.flush();
                counter.set(0);
            }
        } catch (InvalidArgumentException e) {
            log.error("数据收集失败", e);
        }
    }
}
