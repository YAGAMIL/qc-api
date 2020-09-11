package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.poi.PoiBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Description:商圈&POI关系持久层接口 Program:qc-api Created on 2019-09-17 16:26
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo/a>
 * @version 1.0
 */
public interface PoiBusinessRepository extends JpaRepository<PoiBusiness, Long> {

  /**
   * Author: Tablo
   *
   * <p>Description:[根据商圈Id查询商圈关系] Created on 16:28 2019/09/17
   *
   * @param businessId 商圈Id
   * @return com.quantumtime.qc.entity.poi.PoiBusiness
   */
  List<PoiBusiness> findByBusinessAreaId(Long businessId);

  /**
   * Author: Tablo
   *
   * <p>Description:[根据poiId查询所属商圈] Created on 16:30 2019/09/17
   *
   * @param poiId 小区Id
   * @return com.quantumtime.qc.entity.poi.PoiBusiness
   */
  List<PoiBusiness> findByPoiId(String poiId);

  /**
   * Author: Tablo
   *
   * <p>Description:[根据小区Id&商圈Id查询唯一关系] Created on 16:32 2019/09/17
   *
   * @param poiId 小区Id
   * @param businessAreaId 商圈Id
   * @return com.quantumtime.qc.entity.poi.PoiBusiness
   */
  PoiBusiness findByPoiIdAndBusinessAreaId(String poiId, Long businessAreaId);
}
