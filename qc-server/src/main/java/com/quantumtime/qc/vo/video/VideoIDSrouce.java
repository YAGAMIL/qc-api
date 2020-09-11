package com.quantumtime.qc.vo.video;
import	java.io.Serializable;

import lombok.Data;

@Data
public class VideoIDSrouce implements Serializable{
    public static final int SOURCE_USER = 0;
    public static final int SOURCE_BACKEND = 1;
    private static final long serialVersionUID = 1L;

    private String videoID;
    private int source;
}
