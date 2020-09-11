package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.poi.Address;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Description: 地址业务持久层 Created on 2019/09/16 21:00
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Author: Tablo
     *
     * <p>Description:[根据父级Id查询子地址] Created on 20:58 2019/09/16
     *
     * @param parentId 上级Id
     * @return java.util.List<com.quantumtime.qc.entity.poi.Address>
     */
    @Query(value = "select a from Address as a where a.parentId = ?1")
    List<Address> findByParentId(Long parentId);

    /**
     * Author: Tablo
     *
     * <p>Description:[根据Code（自定义）查询地址详情] Created on 20:59 2019/09/16
     *
     * @param code 自定义Code
     * @return com.quantumtime.qc.entity.poi.Address
     */
    Address findOneByCode(String code);

    /**
     * Author: Tablo
     *
     * <p>Description:[根据高德PoiId查询详细地址] Created on 20:59 2019/09/16
     *
     * @param poiId 高德Poi地址，核心主键
     * @return com.quantumtime.qc.entity.poi.Address
     */
    Address findAddressByPoiId(String poiId);


    @Query(value = "select a.id from Address as a where a.poiId in ?1")
    List<Long> findByPoiIds(List<String> poiIds);
    @Query(value = "select * from tb_address  where poi_id in ?1", nativeQuery = true)
    List<Address> findAddressByPoiIds(List<String> poiIds);
    Address findById (long addressId );
}
