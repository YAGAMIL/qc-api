package com.quantumtime.qc.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * .Description:redis集群配置 & Created on 2019/11/05 11:10
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Component
@ConfigurationProperties(prefix = "redis.cluster")
@Data
public class RedisClusterProperties {

    private String masterName;

    private String masterPassword;

    private String sentinelHosts;
}
