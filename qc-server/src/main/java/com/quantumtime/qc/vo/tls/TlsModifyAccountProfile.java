package com.quantumtime.qc.vo.tls;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class TlsModifyAccountProfile {

    @JSONField(name = "Tag")
    private String Tag;

    @JSONField(name = "Value")
    private String Value;

}
