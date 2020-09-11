package com.quantumtime.qc.entity.poi;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * .Description:用户地址记录 Program:qc-api.Created on 2019-10-26 15:20
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Entity
@Table(name = "address_record")
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户地址记录")
public class AddressRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty("主键，无意义")
    private Long id;

    @Column(name = "uid")
    @ApiModelProperty("用户Id")
    private String uid;

    @Column(name = "address_id")
    @ApiModelProperty("地址Id")
    private Long addressId;

    @Column(name = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
