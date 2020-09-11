package com.quantumtime.qc.vo.tls.request;

import lombok.Data;

@Data
public class TlsRequestBase {

    private String sdkappid;

    private String identifier;

    private String usersig;

    private Integer random;

}
