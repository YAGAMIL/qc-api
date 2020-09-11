package com.quantumtime.qc.service;

import com.quantumtime.qc.common.service.IBaseService;
import com.quantumtime.qc.entity.poi.PoiBusiness;

import java.util.List;

/**
 * Description:商圈地址关系业务接口 Program:qc-api Created on 2019-09-17 16:12
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo/a>
 * @version 1.0
 */
public interface IPoiBusinessService extends IBaseService<PoiBusiness, Long> {

    /**
     * Author: Tablo
     * <p> Description:[根据PoiId查询社区所属的商圈关系]
     * Created on 16:52 2019/09/17

     * @param poiId 社区Id
     * @return java.util.List<com.quantumtime.qc.entity.poi.PoiBusiness>
     **/
    List<PoiBusiness> getBusinessByPoi(String poiId);

    /**
     * Author: Tablo
     * <p> Description:[根据商圈Id查询商圈所属的社区关系]
     * Created on 16:52 2019/09/17

     * @param businessId 商圈id
     * @return java.util.List<com.quantumtime.qc.entity.poi.PoiBusiness>
     **/
    List<PoiBusiness> getByBusinessId(Long businessId);

    /**
     * Author: Tablo
     * <p> Description:[根据Poi和商圈查询关系]
     * Created on 17:11 2019/10/15

     * @param poiBusiness 关系实体
     * @return com.quantumtime.qc.entity.poi.PoiBusiness
     **/
    PoiBusiness findNexus(PoiBusiness poiBusiness);

    /**
     * Author: Tablo
     * <p> Description:[校验关系是否存在，不存在则插入]
     * Created on 17:39 2019/09/17

     * @param poiBusiness 商圈POI关系
     **/
    void checkAndSet(PoiBusiness poiBusiness);
}
