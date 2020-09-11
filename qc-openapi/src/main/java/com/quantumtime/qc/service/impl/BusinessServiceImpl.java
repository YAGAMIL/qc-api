package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.service.impl.BaseServiceImpl;
import com.quantumtime.qc.entity.poi.Business;
import com.quantumtime.qc.repository.BusinessRepository;
import com.quantumtime.qc.service.IBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * Description: Program:qc-api Created on 2019-09-17 16:16
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Service
@Slf4j
public class BusinessServiceImpl extends BaseServiceImpl<Business, Long, BusinessRepository> implements IBusinessService {

  @Resource
  private IBusinessService businessService;

    @Override
    @Cacheable(value = "cache-business", key = "'business'+#adCode", unless = "#result == null")
    public Business getBusinessByName(String business, String adCode) {
        return baseRepository.findBusinessByBusinessAreaAndAdCode(business, adCode);
    }

    @Override
    public Business checkAndSet(Business business) {
        Business result = businessService.getBusinessByName(business.getBusinessArea(), business.getAdCode());
        return Objects.isNull(result) ? save(business) : result;
    }

    @Override
    public List<Business> getAllByPoiId(String poiId) {
        return baseRepository.selectByPoiId(poiId);
    }
}