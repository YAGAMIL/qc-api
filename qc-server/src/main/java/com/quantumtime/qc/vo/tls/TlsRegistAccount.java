package com.quantumtime.qc.vo.tls;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class TlsRegistAccount{

    /**
     * 用户名，长度不超过 32 字节
     */
    @JSONField(name="Identifier")
    private String Identifier;

    /**
     * 用户昵称
     */
    @JSONField(name="Nick")
    private String Nick;

    /**
     * 用户头像 URL
     */
    @JSONField(name="FaceUrl")
    private String FaceUrl;

    /**
     * 	帐号类型，开发者默认无需填写，值 0 表示普通帐号，1 表示机器人帐号
     */
    @JSONField(name="Type")
    private Integer Type;

    @Override
    public String toString() {
        return "TlsAccount{" +
                "Identifier='" + Identifier + '\'' +
                ", Nick='" + Nick + '\'' +
                ", FaceUrl='" + FaceUrl + '\'' +
                ", Type=" + Type +
                '}';
    }
}
