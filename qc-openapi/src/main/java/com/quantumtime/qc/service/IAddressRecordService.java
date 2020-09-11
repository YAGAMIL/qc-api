package com.quantumtime.qc.service;

import com.quantumtime.qc.entity.poi.AddressRecord;
import java.util.List;

/**
 * .Description:地址记录持久层 Program:qc-api.Created on 2019-10-28 15:14
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface IAddressRecordService {

    /**
     * Created on 17:01 2019/10/28 Author: Tablo.
     *
     * <p>Description:[查询当前用户的已有地址，按时间倒序排列]
     *
     * @return java.util.List<com.quantumtime.qc.entity.poi.AddressRecord>
     */
    List<AddressRecord> getRecords();

    /**
     * Created on 17:02 2019/10/28 Author: Tablo.
     *
     * <p>Description:[根据用户Id查询用户的地址Id记录]
     *
     * @param uid 用户Uid
     * @return java.util.List<com.quantumtime.qc.entity.poi.AddressRecord>
     */
    List<AddressRecord> queryRecords(String uid);

    /**
     * Created on 20:11 2019/11/08 Author: Tablo.
     *
     * <p>Description:[保存用户新生活圈，已有则更新时间]
     *
     * @param uid 用户Id
     * @param addressId 地址Id
     * @return long
     */
    long checkAndSet(String uid, Long addressId);
}
