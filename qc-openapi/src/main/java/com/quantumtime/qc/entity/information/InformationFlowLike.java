package com.quantumtime.qc.entity.information;


import com.quantumtime.qc.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_information_flow_like")
@Data
public class InformationFlowLike extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "information_flow_id")
    private Long informationFlowId;

    @Column(name = "create_id")
    private String createId;

    @Column(name = "is_delete")
    private Boolean isDelete;
}
