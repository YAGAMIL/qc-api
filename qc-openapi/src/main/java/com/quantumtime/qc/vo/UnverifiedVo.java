package com.quantumtime.qc.vo;

import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.entity.poi.Business;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Description: 验证VO类 Created on 2019/09/16 20:31
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class UnverifiedVo extends Address {

  /** 用户所在经度 */
  private String userLongitude;

  /** 用户所在纬度 */
  private String userLatitude;

  /** 门牌号等信息，暂时不管 */
  private String house;

  /** 所属商圈 */
  private List<Business> businessList;
}
