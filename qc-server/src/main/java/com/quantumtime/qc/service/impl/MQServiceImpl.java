package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.service.IMQService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@Slf4j
public class MQServiceImpl implements IMQService {

    @Value("${rocketmq.namesrv}")
    private String nameServer;

    @Value("${rocketmq.producer.group}")
    private String group;

    @Value("${rocketmq.producer.qualify.topic}")
    private String qualifyTopic;

    @Value("${rocketmq.producer.qualify.tag}")
    private String qualifyTag;

    @Value("${rocketmq.producer.feeds.topic}")
    private String feedsTopic;

    @Value("${rocketmq.producer.feeds.tag}")
    private String feedsTag;

    @Value("${rocketmq.accesskey-id}")
    private String accessKey;

    @Value("${rocketmq.accesskey-secret}")
    private String accessSecret;

    private DefaultMQProducer producer;

    @PostConstruct
    public void init() throws MQClientException {
        producer = new DefaultMQProducer(group,
                new AclClientRPCHook(new SessionCredentials(accessKey, accessSecret)));
        producer.setNamesrvAddr(nameServer);
        producer.setAccessChannel(AccessChannel.CLOUD);
        producer.start();
    }

    @PreDestroy
    public void close() {
        if (producer != null) {
            producer.shutdown();
        }
    }

    private boolean send(String topic, String producerTag, String json) {
        try {
            Message message = new Message(topic, producerTag, json.getBytes("UTF-8"));
            SendResult result = producer.send(message);
            if (result.getSendStatus() == SendStatus.SEND_OK) {
                return true;
            } else {
                log.error("发送消息队列异常:" + result.getSendStatus().name());
            }
        } catch (Exception e) {
            log.error("发送消息队列异常:", e);
        }
        return false;
    }

    @Override
    public boolean sendQualify(String json) {
        return send(qualifyTopic, qualifyTag, json);
    }

    @Override
    public boolean sendFeeds(String json) {
        return send(feedsTopic, feedsTag, json);
    }
}
