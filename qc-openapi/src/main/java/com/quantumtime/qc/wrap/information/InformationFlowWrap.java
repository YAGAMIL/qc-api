package com.quantumtime.qc.wrap.information;

import com.quantumtime.qc.entity.information.InformationFlow;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description: 信息流封装类 Created on 2019/09/16 21:27
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class InformationFlowWrap {
  private InformationFlow informationFlow;

  private String createUserNickName;

  private String createUserAvatar;

  private String themeName;

  private String AddressName;

  private Long likeId;
}
