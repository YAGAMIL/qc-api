package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InviteRelationRepository  extends JpaRepository<Invite, Long> {

    @Query(value = " select * From invite where invitee_id = ?1",nativeQuery = true  )
    Invite  myInviter(Integer  id);

    @Query(value = " select * From invite where inviter_id = ?1 order by create_time desc limit ?2,?3",nativeQuery = true  )
    List<Invite> myInviteUseList(Integer id,Integer pageNumber, Integer pageSize);


}
