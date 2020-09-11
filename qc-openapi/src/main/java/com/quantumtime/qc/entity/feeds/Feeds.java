package com.quantumtime.qc.entity.feeds;

import com.quantumtime.qc.common.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.print.attribute.standard.DateTimeAtCompleted;
import java.io.Serializable;

/**
 * Description: 信息流 Created on 2019/09/18 13:36
 *
 * @author 董瑞
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_information_trends")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Feeds extends BaseEntity implements Serializable {


        @Id
        @Column(name = "feeds_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long feeds_id;

        @Column(name = "content")
        private String content;

        @Column(name = "address_id")
        private Long address_id;

        @Column(name = "review_status")
        private Integer review_status;

        @Column(name = "picture")
        private String picture;

        @Column(name = "type")
        private Integer type;

        @Column(name = "delete_flag")
        private Integer delete_flag;

        @Column(name = "create_uid")
        private String create_uid;

        @Column(name = "create_name")
        private String create_name;

        @Column(name = "address_name")
        private String address_name;

        @Column(name = "feeds_avatar")
        private String feeds_avatar;

        @Column(name = "poi_id")
        private String poi_id;

        @Column(name = "city")
        private String city;

        @Column(name = "nickname")
        private String nikeName;
/*
       @Column(name = "t_create_time")
        private Date t_create_time;
*/

       /* @Column(name = "t_up_time")
        private Date t_up_time;
*/

}
