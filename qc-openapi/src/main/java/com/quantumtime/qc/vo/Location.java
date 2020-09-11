package com.quantumtime.qc.vo;

import lombok.Data;

/**
 * Description: 地址信息 Created on 2019/09/16 18:25
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
public class Location {

  private String cityCode;

  private String city;

  private String adCode;

  private String district;

  private String poiId;

  private String poiName;

  private String address;

  private String longitude;

  private String latitude;
}
