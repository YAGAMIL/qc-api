package com.quantumtime.qc.common.properties;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * .Description: 默认头像配置 & Created on 2019/11/05 11:08
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Component
@ConfigurationProperties(prefix = "user")
@Data
public class DefaultAvatarsProperties {
    private List<String> defaultAvatars = new ArrayList<>();
}
