package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.poi.AddressRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * .Description:地址记录数据层 Program:qc-api.Created on 2019-10-26 15:39
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface AddressRecordRepository
        extends JpaRepository<AddressRecord, Long>, JpaSpecificationExecutor<AddressRecord> {

    /**
     * Created on 15:05 2019/10/28 Author: Tablo.
     *
     * <p>Description:[根据用户Id查询用户使用地址记录]
     *
     * @param uid 用户Id
     * @param addressId 地址Id
     * @return com.quantumtime.qc.entity.poi.AddressRecord
     */
    AddressRecord findByUidAndAddressId(String uid, Long addressId);

    /**
     * Created on 16:33 2019/11/08 Author: Tablo.
     *
     * <p>Description:[查询用户的所有历史生活圈]
     *
     * @param uid 用户Id
     * @return java.util.List<com.quantumtime.qc.entity.poi.AddressRecord>
     */
    List<AddressRecord> findByUid(String uid);

    /**
     * Created on 14:31 2019/11/13 Author: Tablo.
     *
     * <p>Description:[根据用户Id和生活圈地址Id删除用户历史记录]
     *
     * @param uid 用户Id
     * @param addressId 地址Id
     * @return boolean
     */

    Integer deleteAddressRecordByUidAndAddressId(String uid, Long addressId);
}
