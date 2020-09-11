package com.quantumtime.qc.vo.tls;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class TlsModifyAccount {

    @JSONField(name = "From_Account")
    private String From_Account;

    @JSONField(name = "ProfileItem")
    private List<TlsModifyAccountProfile> ProfileItem;

    @Override
    public String toString() {
        return "TlsModifyAccount{" +
                "From_Account='" + From_Account + '\'' +
                '}';
    }
}
