package com.quantumtime.qc.entity.poi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quantumtime.qc.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * Description: 地址实体表 Created on 2019/09/16 18:18
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_address")
@Data
@Accessors(chain = true)
public class Address extends BaseEntity {

  private static final int AD_CODE_LENGTH = 6;

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "parent_id")
  private Long parentId;

  @Column(name = "code")
  private String code;

  /** 例如”后厂村路与西北旺三街交叉口路南200米“ */
  @Column(name = "address")
  private String address;

  /** 经度 */
  @Column(name = "longitude")
  private String longitude;

  /** 纬度 */
  @Column(name = "latitude")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private String latitude;

  /** 详细地址，目前被Address代替 */
  @Column(name = "details")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private String details;

  /** 小区ID，真·主键 */
  @NotBlank
  @Column(name = "poi_id")
  private String poiId;

  /** 例如”"方恒国际中心B座" */
  @NotBlank
  @Column(name = "poi_name")
  private String poiName;

  /** 例如990221，代表poi的类型 */
  @Column(name = "type_code")
  private String typeCode;

  /** 乡镇街道编码，例如“110101001000” */
  @Column(name = "town_code")
  private String townCode;

  /** 社区街道/乡，例如“燕园街道” */
  @Column(name = "township")
  private String township;

  /** 例如“阜通东大街“ */
  @Column(name = "street")
  private String street;

  /** 例如“朝阳区” */
  @Column(name = "district")
  private String district;

  /** 例如“010” */
  @Column(name = "city_code")
  private String cityCode;

  /** 例如：“北京市” */
  @Column(name = "city")
  private String city;

  /** 省级code */
  @Column(name = "province_code")
  private String provinceCode;

  /** 例如“北京市”“河北省” */
  @Column(name = "province")
  private String province;

  /** 国家code */
  @Column(name = "country_code")
  private String countryCode;

  /** 国家名称 */
  @Column(name = "country")
  private String country;

  /** 区编码，"110101" */
  @Column(name = "ad_code")
  private String adCode;

  public Address makeProvinceCode() {

    return adCode.length() == AD_CODE_LENGTH
        ? setProvinceCode(adCode.substring(0, 2) + "0000").setCountryCode("100000")
        : this;
  }
}
