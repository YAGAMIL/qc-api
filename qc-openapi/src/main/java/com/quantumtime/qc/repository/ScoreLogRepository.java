package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.score.ScoreLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:积分记录持久层 Created on 2019/12/03 17:36
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Repository
public interface ScoreLogRepository extends JpaRepository<ScoreLog, Long> {
    @Query(value = "select * from score_log where uid=?1 order by create_time desc limit ?2,?3", nativeQuery = true)
    List<ScoreLog> findByUidPage(String uid, int offset, int limit);

    /**
     * Author: Tablo
     *
     * <p>Description:[获取积分记录] Created on 2:28 2019/12/05
     *
     * @param uid 用户id
     * @param pageable 分页数据
     * @return java.util.List<com.quantumtime.qc.entity.score.ScoreLog>
     */
    @Query(value = "select f from ScoreLog f  where f.uid =?1 order by f.createTime desc")
    List<ScoreLog> findLogs(String uid, Pageable pageable);

    /**
     * Author: Tablo
     *
     * <p>Description:[查询匹配的积分记录] Created on 16:27 2019/12/09
     *
     * @param recordId 流水id
     * @param tranType 类型
     * @return com.quantumtime.qc.entity.score.ScoreLog
     */
    ScoreLog findScoreLogByRecordIdAndTranType(Long recordId, int tranType);

    /**
     * Author: Tablo
     * <p> Description:[查询兑换记录]
     * Created on 17:17 2019/12/11

     * @param uid 用户id
     * @param pageable 分页信息
     * @return java.util.List<com.quantumtime.qc.entity.score.Exchange>
     **/

    @Query(value = "select f from ScoreLog f  where f.uid =?1 AND f.tranType = 2 order by f.createTime desc")
    List<ScoreLog> findExchangeLogs(String uid, Pageable pageable);
}
