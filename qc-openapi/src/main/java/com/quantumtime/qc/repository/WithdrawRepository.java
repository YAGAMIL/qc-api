package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.score.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:体现持久层 Program:qc-api Created on 2019-12-03 16:39
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Repository
public interface WithdrawRepository extends JpaRepository<Withdraw, Integer> {

    /**
     * Author: Tablo
     *
     * <p>Description:[查询该用户未完成的提现] Created on 18:34 2019/12/03
     *
     * @param uid 用户Id
     * @return java.util.List<com.quantumtime.qc.entity.score.Withdraw>
     */
    @Query(value = "SELECT * FROM withdraw WHERE uid = ?1 AND status = 0 ", nativeQuery = true)
    List<Withdraw> findNotComplete(String uid);

    /**
     * Author: Tablo
     *
     * <p>Description:[查询该用户提现次数] Created on 18:39 2019/12/03
     *
     * @param uid 用户id
     * @return int
     */
    @Query(value = "SELECT COUNT(*) FROM withdraw WHERE uid = ?1", nativeQuery = true)
    int findFirst(String uid);
    @Query(value = "select * from withdraw where uid=?1  order by create_time desc limit ?2,?3" , nativeQuery = true)
    List<Withdraw> findByUid(String uid ,Integer pageNumber, Integer pageSize );
}
