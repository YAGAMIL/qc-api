package com.quantumtime.qc.service;

import com.quantumtime.qc.common.service.IBaseService;
import com.quantumtime.qc.entity.poi.Business;

import java.util.List;

/**
 * Description:商圈业务接口 Program:qc-api Created on 2019-09-17 16:12
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo/a>
 * @version 1.0
 */
public interface IBusinessService extends IBaseService<Business, Long> {

  /**
   * Author: Tablo
   *
   * <p>Description:[根据名称查询商圈/AdCode] Created on 17:15 2019/09/17
   *
   * @param business 商圈名
   * @param adCode 区Id
   * @return List<Business>
   */
  Business getBusinessByName(String business, String adCode);

  /**
   * Author: Tablo
   *
   * <p>Description:[判断存储商圈信息，返回商圈Id] Created on 17:28 2019/09/17
   *
   * @param business 商圈
   * @return com.quantumtime.qc.entity.poi.Business
   */
  Business checkAndSet(Business business);

  /**
   *
   * @param poiId  Address的poiId
   * @return 所属所有商圈
   */
  List<Business> getAllByPoiId(String poiId);
}
