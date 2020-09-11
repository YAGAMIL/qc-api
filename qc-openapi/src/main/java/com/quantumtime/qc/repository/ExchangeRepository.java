package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.score.Exchange;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Description:兑换订单持久层 Program:qc-api Created on 2019-12-09 13:59
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

    /**
     * Author: Tablo
     *
     * <p>Description:[根据订单号查询兑换订单] Created on 14:35 2019/12/09
     *
     * @param orderId 订单号
     * @return com.quantumtime.qc.entity.score.Exchange
     */
    Exchange findExchangeByOrderId(String orderId);

    @Query(value = "select f from Exchange f  where f.uid =?1 order by f.createTime desc")
    List<Exchange> findLogs(String uid, Pageable pageable);
}
