package com.quantumtime.qc.service;

import com.quantumtime.qc.entity.score.ScoreLog;
import com.quantumtime.qc.entity.score.Withdraw;
import com.quantumtime.qc.vo.InviteDetailVo;
import com.quantumtime.qc.vo.MyInviteUserVo;

import java.util.List;

public interface RelationshipService {

  // 用户的积分分界面信息
  InviteDetailVo integralDetail(String uid);
  // 邀请记录
  List<MyInviteUserVo> myInviteUseList(Integer id, Integer pageNumber, Integer pageSize);
  // 提现记录
  List<Withdraw> myWithdrawList(String uid, Integer pageNumber, Integer pageSize);
  // 建立邀请关系
  Boolean invitationUser(Integer inviterId, Integer inviteeId);

  List<ScoreLog> myScoreLogList(String uid, Integer pageNumber, Integer pageSize);

  List<ScoreLog> myIntegralList(String uid, Integer pageNumber, Integer pageSize);

  /**
   * Author: Tablo
   *
   * <p>Description:[及扽兑换记录] Created on 17:10 2019/12/11
   *
   * @param uid 当前uid
   * @param pageNumber 页码
   * @param pageSize 页面大小
   * @return java.util.List<com.quantumtime.qc.entity.score.Exchange>
   */
  List<ScoreLog> myExchangeList(String uid, Integer pageNumber, Integer pageSize);
  Boolean clickTaskReceive(String uid ,Long activityId);

  Boolean sendVideoReceive(String uid, Long activityId);

  Boolean videoTaskReceive(String uid ,Long activityId);
}
