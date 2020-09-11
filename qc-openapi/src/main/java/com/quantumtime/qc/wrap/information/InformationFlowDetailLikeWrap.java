package com.quantumtime.qc.wrap.information;

import com.quantumtime.qc.entity.information.InformationFlowLike;
import lombok.Data;

@Data
public class InformationFlowDetailLikeWrap {

    private InformationFlowLike informationFlowLike;

    private String likeNickName;

    private String likeUserId;

    private String likeAvatar;

    private String content;
}
