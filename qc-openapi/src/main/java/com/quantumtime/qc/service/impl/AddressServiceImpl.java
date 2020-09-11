package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.annotation.CacheExpire;
import static com.quantumtime.qc.common.constant.ErrorCodeConstant.GLOBAL_NOT_EXIST;
import com.quantumtime.qc.common.exception.ExpFunction;
import com.quantumtime.qc.common.service.impl.BaseServiceImpl;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.entity.poi.Business;
import com.quantumtime.qc.entity.poi.PoiBusiness;
import com.quantumtime.qc.repository.AddressRepository;
import com.quantumtime.qc.service.IAddressService;
import com.quantumtime.qc.service.IBusinessService;
import com.quantumtime.qc.service.IPoiBusinessService;
import com.quantumtime.qc.vo.AddressParam;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description: 地址信息 Created on 2019/09/16 20:57
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Service
@Slf4j
public class AddressServiceImpl extends BaseServiceImpl<Address, Long, AddressRepository> implements IAddressService {
    @Resource IAddressService addressService;
    @Resource private IPoiBusinessService poiBusinessService;
    @Resource private IBusinessService businessService;

    @Override
    @Cacheable(value = "cache-address", key = "'id'+#result.getId()", unless = "#result == null")
    public Address findById(Long id) {
        return super.findById(id);
    }

    @Override
    public List<Address> findByParentId(Long parentId) {
        return this.baseRepository.findByParentId(parentId);
    }

    @Override
    public Address findAddressById(Long id) {
        return this.findById(id);
    }

    @Override
    @Cacheable(value = "cache-address", key = "'code'+#code", unless = "#result == null")
    @CacheExpire(1800)
    public Address findByCode(String code) {
        return this.baseRepository.findOneByCode(code);
    }

    @Override
    @Cacheable(value = "cache-address", key = "'poiId'+#poiId", unless = "#result == null")
    public Address findByPoiId(String poiId) {
        return this.baseRepository.findAddressByPoiId(poiId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long checkAndSet(Address location) {
        ExpFunction.true4ThrowBiz(
                "0".equals(location.getLatitude()) || "0".equals(location.getLongitude()), GLOBAL_NOT_EXIST, "经纬度数据");
        return Optional.ofNullable(addressService.findByPoiId(location.getPoiId())).map(Address::getId).orElseGet(() ->
                save(location.makeProvinceCode()).getId());
    }

    @Override
    public List<Address> findAllByIds(Set<Long> ids) {
        return this.baseRepository.findAllById(ids);
    }

    @Override
    public List<Long> findAllByPoIds(List<String> ids) {
        return this.baseRepository.findByPoiIds(ids);
    }

    @Override
    public void checkAndSetBusiness(List<Business> businessList, String poiId, String adCode) {
        businessList.forEach(business -> poiBusinessService.checkAndSet(PoiBusiness.builder()
                .businessAreaId(businessService.checkAndSet(business.setAdCode(adCode)).getBusinessAreaId())
                .poiId(poiId)
                .build()));
    }

    /**
     * Author: Tablo
     *
     * <p>Description:[地址校验增加若商圈List为空，则只增加Poi信息] Created on 16:08 2019/10/08
     *
     * @param subclass 继承自Address的对象（包含所有Address属性的对象）
     * @return Long 新生成的或已存在的地址Id
     */
    @Override
    public Long checkAndSetAddress(AddressParam subclass) {
        Address location = subclass.getAddress();
        List<Business> businesses = subclass.getBusinessList();
        ExpFunction.true4ThrowBiz(
                "0".equals(location.getLatitude()) || "0".equals(location.getLongitude()), GLOBAL_NOT_EXIST, "经纬度数据");
        if (businesses != null && businesses.size() > 0) {
            checkAndSetBusiness(businesses, location.getPoiId(), location.getAdCode());
        }
        return checkAndSet(location);
    }

    @Override
    public List<Address> findAllByIds(List<Long> addressList) {
        return this.baseRepository.findAllById(addressList);
    }
}
