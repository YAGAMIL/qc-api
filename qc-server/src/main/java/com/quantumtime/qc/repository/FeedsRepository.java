package com.quantumtime.qc.repository;
import com.quantumtime.qc.entity.feeds.Feeds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface FeedsRepository extends JpaRepository<Feeds, Long>{
   @Query(value = "select s.* from tb_information_trends as s  where s.create_uid = ?1 and delete_flag=1 order by s.t_create_time desc limit ?2,?3 ",nativeQuery = true  )
   List<Feeds> myTrends(String create_uid,Integer pageNumber, Integer pageSize);
   @Query(value = "select count(*) from tb_information_trends as s  where s.create_uid = ?1 and delete_flag=1 ",nativeQuery = true  )
   Integer myFeedsSum(String create_uid);
//   List<Feeds> findAllById(List<Long> ids);

   /* List<Feeds> findBycreate_uidOrderBycreate_timeDesc(String create_uid );
   *
   * order by s.t_create_time desx
    */

}
