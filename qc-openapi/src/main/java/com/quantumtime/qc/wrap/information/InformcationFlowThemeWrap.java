package com.quantumtime.qc.wrap.information;


import com.quantumtime.qc.entity.information.InformationFlowTheme;
import lombok.Data;


@Data
public class InformcationFlowThemeWrap {

    private InformationFlowTheme informationFlowTheme;

    private Boolean isJoin;

    private String createNickName;

}
