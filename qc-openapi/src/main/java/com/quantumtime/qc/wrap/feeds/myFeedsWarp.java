package com.quantumtime.qc.wrap.feeds;

import lombok.Data;

import java.util.Date;

@Data
public class myFeedsWarp {

    private long feeds_id;
    private String content;
    private long address_id;
    private int review_status;
    private String picture;
    private int type;
    private String create_uid;
    private String create_name;
    private int read_num;
    private Date t_create_time;
}
