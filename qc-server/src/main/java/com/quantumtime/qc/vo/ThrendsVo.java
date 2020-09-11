package com.quantumtime.qc.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ThrendsVo {

    @JSONField(name="id")
    private String author_id;

    @JSONField(name="content")
    private String content;

    @JSONField(name="create_name")
    private String author_name;
    @JSONField(name="create_time")
    private String t_create_time;


}
