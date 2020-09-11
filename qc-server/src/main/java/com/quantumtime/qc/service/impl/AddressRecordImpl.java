package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.entity.poi.AddressRecord;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.repository.AddressRecordRepository;
import com.quantumtime.qc.service.IAddressRecordService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * .Description:地址记录业务实现 Program:qc-api.Created on 2019-10-28 15:16
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Service
public class AddressRecordImpl implements IAddressRecordService {

    @Resource AccountHelp accountHelp;
    @Resource private AddressRecordRepository recordRepository;

    @Override
    public List<AddressRecord> getRecords() {
        return queryRecords(accountHelp.getCurrentUser().getUid());
    }

    @Override
    public List<AddressRecord> queryRecords(String uid) {
        return recordRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                criteriaQuery
                        .where(criteriaBuilder.equal(root.get("uid"), uid))
                        .orderBy(criteriaBuilder.desc(root.get("createTime")))
                        .getRestriction());
    }

    @Override
    public long checkAndSet(String uid, Long addressId) {
        AddressRecord record = recordRepository.findByUidAndAddressId(uid, addressId);
        return recordRepository
                .save(Optional.ofNullable(record)
                        .map(addressRecord -> addressRecord.setCreateTime(LocalDateTime.now()))
                        .orElseGet(() -> AddressRecord.builder().uid(uid).addressId(addressId).build()))
                .getAddressId();
    }
}
