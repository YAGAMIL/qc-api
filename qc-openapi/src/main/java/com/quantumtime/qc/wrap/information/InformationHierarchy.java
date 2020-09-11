package com.quantumtime.qc.wrap.information;

import lombok.Data;

@Data
public class InformationHierarchy {
    private InformationFlowWrap informationFlowWrap;

    private InformationHierarchy parentInformationFlow;

}
