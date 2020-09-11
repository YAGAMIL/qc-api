package com.quantumtime.qc.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "tb_error_code")
@Data
@EqualsAndHashCode(callSuper = false)
public class ErrorCodeEntity extends BaseEntity implements Serializable {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "message")
    private String message;

    @Column(name = "scope")
    private String scope;
}
