package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.annotation.CacheExpire;
import com.quantumtime.qc.common.service.impl.BaseServiceImpl;
import com.quantumtime.qc.entity.poi.PoiBusiness;
import com.quantumtime.qc.repository.PoiBusinessRepository;
import com.quantumtime.qc.service.IPoiBusinessService;
import java.util.List;
import static java.util.Objects.isNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Description: Program:qc-api Created on 2019-09-17 16:15
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Service
@Slf4j
public class PoiBusinessServiceImpl extends BaseServiceImpl<PoiBusiness, Long, PoiBusinessRepository> implements IPoiBusinessService {


    @Override
    public List<PoiBusiness> getBusinessByPoi(String poiId) {
        return baseRepository.findByPoiId(poiId);
    }

    @Override
    public List<PoiBusiness> getByBusinessId(Long businessId) {
        return baseRepository.findByBusinessAreaId(businessId);
    }

    @Override
    @Cacheable(value = "cache-poi_Business", key = "'nexus'+#poiBusiness.getPoiId()+poiBusiness.getBusinessAreaId()", unless = "#result == null")
    @CacheExpire(1800)
    public PoiBusiness findNexus(PoiBusiness poiBusiness) {
        return baseRepository.findByPoiIdAndBusinessAreaId(poiBusiness.getPoiId(), poiBusiness.getBusinessAreaId());
    }

    @Override
    public void checkAndSet(PoiBusiness poiBusiness) {
        if (isNull(findNexus(poiBusiness))) {
            save(poiBusiness);
        }
    }
}