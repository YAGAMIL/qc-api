package com.quantumtime.qc.utils;

import com.quantumtime.qc.vo.UnverifiedVo;
import org.apache.commons.lang3.StringUtils;

/**
 * Description: 位置工具类 Created on 2019/09/16 21:58
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public class LocationUtils {

  public static double calculateLineDistance(double lat1, double lng1, double lat2, double lng2) {
    double var2 = 0.01745329251994329D;
    double var4 = lat1;
    double var6 = lng1;
    double var8 = lat2;
    double var10 = lng2;
    var4 *= 0.01745329251994329D;
    var6 *= 0.01745329251994329D;
    var8 *= 0.01745329251994329D;
    var10 *= 0.01745329251994329D;
    double var12 = Math.sin(var4);
    double var14 = Math.sin(var6);
    double var16 = Math.cos(var4);
    double var18 = Math.cos(var6);
    double var20 = Math.sin(var8);
    double var22 = Math.sin(var10);
    double var24 = Math.cos(var8);
    double var26 = Math.cos(var10);
    double[] var28 = new double[3];
    double[] var29 = new double[3];
    var28[0] = var18 * var16;
    var28[1] = var18 * var12;
    var28[2] = var14;
    var29[0] = var26 * var24;
    var29[1] = var26 * var20;
    var29[2] = var22;
    double var30 =
        Math.sqrt(
            (var28[0] - var29[0]) * (var28[0] - var29[0])
                + (var28[1] - var29[1]) * (var28[1] - var29[1])
                + (var28[2] - var29[2]) * (var28[2] - var29[2]));
    return (double) (Math.asin(var30 / 2.0D) * 1.27420015798544E7D);
  }

  public static void main(String[] args) {
    double distance = calculateLineDistance(40.043541, 116.273645, 40.042111, 116.272311);
    System.out.println("距离" + distance + "米");
  }

  /**
   * Author: Tablo
   *
   * <p>Description:[校验vo地址类是否包含null或空] Created on 21:54 2019/09/16
   *
   * @param vo 地址VO类
   * @return boolean
   */
  public static boolean parseNullResult(UnverifiedVo vo) {
    return vo.getLatitude() == null
        || vo.getLongitude() == null
        || vo.getUserLatitude() == null
        || vo.getUserLongitude() == null
        || StringUtils.isEmpty(vo.getHouse())
        || vo.getCityCode() == null
        || vo.getCity() == null
        || vo.getAdCode() == null
        || vo.getDistrict() == null
        || vo.getPoiId() == null
        || vo.getPoiName() == null
        || vo.getAddress() == null;
  }
}
