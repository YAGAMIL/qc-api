package com.quantumtime.qc.service;

import com.quantumtime.qc.common.service.IBaseService;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.entity.poi.Business;
import com.quantumtime.qc.vo.AddressParam;
import java.util.List;
import java.util.Set;

/**
 * Description:Address业务接口设计 Created on 2019/09/16 21:04
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface IAddressService extends IBaseService<Address, Long> {

    /**
     * Author: Tablo
     *
     * <p>Description:[根据父级Id查询所属地址LIST] Created on 21:00 2019/09/16
     *
     * @param parentId 上级ID
     * @return java.util.List<com.quantumtime.qc.entity.poi.Address>
     */
    List<Address> findByParentId(Long parentId);

    /**
     * Author: Tablo
     *
     * <p>Description:[根据本库自增Id查询详细地址] Created on 21:01 2019/09/16
     *
     * @param id 自增主键
     * @return java.util.List<com.quantumtime.qc.entity.poi.Address>
     */
    Address findAddressById(Long id);

    /**
     * Author: Tablo
     *
     * <p>Description:[根据code查询地址信息，停掉] Created on 20:48 2019/09/16
     *
     * @param code 地址的code信息
     * @return com.quantumtime.qc.entity.poi.Address
     */
    Address findByCode(String code);

    /**
     * Author: Tablo
     *
     * <p>Description:[根据PoiId查询地址] Created on 20:47 2019/09/16
     *
     * @param poiId 社区id（来自高德）
     * @return com.quantumtime.qc.entity.poi.Address
     */
    Address findByPoiId(String poiId);

    /**
     * Author: Tablo
     *
     * <p>Description:[校验地址是否存在，不存在插入] Created on 21:01 2019/09/16
     *
     * @param location 返回自增Id
     * @return java.lang.Long
     */
    //dong
    Long checkAndSet(Address location);

    List<Address> findAllByIds(Set<Long> ids);

    List<Long> findAllByPoIds(List<String> ids);

    /**
     * Author: Tablo
     *
     * <p>Description:[检验存储商圈、地址信息] Created on 16:52 2019/10/10
     *
     * @param businessList 商圈列表
     * @param poiId 地域ID（高德）
     * @param adCode （高德Code）
     */
    void checkAndSetBusiness(List<Business> businessList, String poiId, String adCode);

    /**
     * Author: Tablo
     *
     * <p>Description:[注册用户地址校验] Created on 16:08 2019/10/08
     *
     * @param subclass 继承自Address的对象（包含所有Address属性的对象）
     * @return Long 新生成的或已存在的地址Id
     */
    Long checkAndSetAddress(AddressParam subclass);

    /**
     * Created on 21:03 2019/11/08 Author: Tablo.
     *
     * <p>Description:[根据主键List顺序查找Address]
     *
     * @param addressList 主键List
     * @return java.util.List<com.quantumtime.qc.entity.poi.Address>
     */
    List<Address> findAllByIds(List<Long> addressList);
}
