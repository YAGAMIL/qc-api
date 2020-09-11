package com.quantumtime.qc.wrap.information;

import com.quantumtime.qc.entity.information.InformationFlowCommit;
import lombok.Data;

@Data
public class InformationFlowDetailCommitWrap {

    private InformationFlowCommit informationFlowCommit;

    private String commitUserId;

    private String commitNickName;

    private String commitAvatar;

}
