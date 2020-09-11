package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.exception.ExpFunction;
import com.quantumtime.qc.entity.ClickContent;
import com.quantumtime.qc.entity.Invite;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.entity.activity.ActivityTask;
import com.quantumtime.qc.entity.feeds.Video;
import com.quantumtime.qc.entity.score.ScoreLog;
import com.quantumtime.qc.entity.score.Withdraw;
import com.quantumtime.qc.repository.ActivityTaskRepository;
import com.quantumtime.qc.repository.ClickContentRepository;
import com.quantumtime.qc.repository.ExchangeRepository;
import com.quantumtime.qc.repository.InviteRelationRepository;
import com.quantumtime.qc.repository.ScoreLogRepository;
import com.quantumtime.qc.repository.TaskUserFetchRepository;
import com.quantumtime.qc.repository.UserRepository;
import com.quantumtime.qc.repository.VideoRepository;
import com.quantumtime.qc.repository.WithdrawRepository;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.repository.*;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.service.RelationshipService;
import com.quantumtime.qc.vo.InviteDetailVo;
import com.quantumtime.qc.vo.MyInviteUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.quantumtime.qc.entity.score.BaseScoreModel.TASK;

@Service
@Slf4j
public class RelationshipServiceImpl implements RelationshipService {
    private static final int IS_RECEIVE_TRUE = 1;
    private static final int IS_RECEIVE_FALSE = 0;
    private static final int DEFAULT_TASK_STATUS = 0;
    private static final long DEFAULT_SCORE = 0;
    private static final int INTEGRAL_EXCHANGE = 10000;
    private static final int WITHDRAW_TYPE_FIRST = 0;
    private static final int WITHDRAW_TYPE_NEXT = 1;
    @Resource
    private UserRepository userRepository;
    @Resource
    private WithdrawRepository withdrawRepository;
    @Resource
    private InviteRelationRepository inviteRelationRepository;
    @Resource
    private ScoreLogRepository scoreLogRepository;
    @Resource
    private ExchangeRepository exchangeRepository;
    @Resource
    private ClickContentRepository clickContentRepository;
    @Resource
    private VideoRepository videoRepository;
    @Resource
    private ActivityTaskRepository activityTaskRepository;
    @Resource
    private IUserService userService;
    @Resource
    private TaskUserFetchRepository taskUserFetchRepository;


    @Override
    public InviteDetailVo integralDetail(String uid) {
        User user = userRepository.getOne(uid);
        int first = withdrawRepository.findFirst(uid);
        InviteDetailVo inviteDetailVo = new InviteDetailVo();
        inviteDetailVo.setId(user.getId());
        inviteDetailVo.setIsReceive(isInvite(user.getId()));
        if (user.getScore() == null) {
            inviteDetailVo.setMoney((double) 0);
            inviteDetailVo.setScore((long) 0);
        } else {
            inviteDetailVo.setMoney(Math.floor(user.getScore() / INTEGRAL_EXCHANGE));
            inviteDetailVo.setScore(user.getScore());
        }
        if (first == 0) {
            inviteDetailVo.setWithdrawType(WITHDRAW_TYPE_FIRST);
        } else {
            inviteDetailVo.setWithdrawType(WITHDRAW_TYPE_NEXT);
        }

        return inviteDetailVo;
    }

    public int isInvite(Integer id) {
        Invite invite = inviteRelationRepository.myInviter(id);
        if (invite == null) {
            return IS_RECEIVE_TRUE;
        } else {
            return IS_RECEIVE_FALSE;
        }
    }

    @Override
    public List<MyInviteUserVo> myInviteUseList(Integer id, Integer pageNumber, Integer pageSize) {
        Integer offset = pageSize * (pageNumber - 1);
        List<MyInviteUserVo> myInviteUseList = new ArrayList<>();

        List<Invite> myInviteList = inviteRelationRepository.myInviteUseList(id, offset, pageSize);
        if (myInviteList.size() == 0) {
            return myInviteUseList;
        } else {
            List<Integer> idList =
                    myInviteList.stream().map(Invite::getInviteeId).collect(Collectors.toList());
            List<User> userList = userRepository.findByIdList(idList);
            for (int i = 0; i < myInviteList.size(); i++) {
                for (int j = 0; j < userList.size(); j++) {
                    if (myInviteList.get(i).getInviteeId().equals(userList.get(j).getId())) {
                        MyInviteUserVo myInviteUserVo = new MyInviteUserVo();
                        myInviteUserVo.setNickName(userList.get(j).getNickname());
                        myInviteUserVo.setAvatar(userList.get(j).getAvatar());
                        myInviteUserVo.setCreateTimeLong(
                                myInviteList
                                        .get(i)
                                        .getCreateTime()
                                        .toInstant(ZoneOffset.of("+8"))
                                        .toEpochMilli());
                        myInviteUserVo.setScore(myInviteList.get(i).getScore());
                        myInviteUserVo.setTaskStatus(myInviteList.get(i).getTaskStatus());
                        myInviteUserVo.setCreateTime(myInviteList.get(i).getCreateTime());
                        myInviteUserVo.setInviteeId(myInviteList.get(i).getInviteeId());
                        myInviteUserVo.setInviterId(myInviteList.get(i).getInviterId());
                        myInviteUserVo.setId(myInviteList.get(i).getId());
                        myInviteUseList.add(myInviteUserVo);
                    }
                }
            }

            return myInviteUseList;
        }
    }

    @Override
    public List<Withdraw> myWithdrawList(String uid, Integer pageNumber, Integer pageSize) {
        Integer offset = pageSize * (pageNumber - 1);
        return withdrawRepository.findByUid(uid, offset, pageSize);
    }

    @Override
    public Boolean invitationUser(Integer inviterId, Integer inviteeId) {
        Invite invite = new Invite();
        invite.setInviteeId(inviteeId);
        invite.setInviterId(inviterId);
        invite.setTaskStatus(DEFAULT_TASK_STATUS);
        invite.setScore(DEFAULT_SCORE);
        invite.setCreateTime(LocalDateTime.now());
        inviteRelationRepository.save(invite);
        return true;
    }

    @Override
    public List<ScoreLog> myIntegralList(String uid, Integer pageNum, Integer size) {
        return scoreLogRepository.findLogs(uid, PageRequest.of(pageNum - 1, size));
    }

    @Override
    public List<ScoreLog> myExchangeList(String uid, Integer pageNumber, Integer pageSize) {
        return scoreLogRepository.findExchangeLogs(uid, PageRequest.of(pageNumber - 1, pageSize));
//        return exchangeRepository.findLogs(uid, PageRequest.of(pageNumber - 1, pageSize));
    }

    @Override
    public List<ScoreLog> myScoreLogList(String uid, Integer pageNumber, Integer pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        int limit = pageSize;
        return scoreLogRepository.findByUidPage(uid, offset, limit);
    }
    public Boolean isIntegral(String uid, Long activityId) {
        List<ClickContent> todayClick = clickContentRepository.taskClick(uid);

        if (todayClick.size() < 5) {
            return false;
        } else {
            int i = 0;
            for (ClickContent clickContent : todayClick) {
                Video video = videoRepository.findByVideoId(clickContent.getContentId());
                if (video.getActivityId() != null && video.getActivityId().longValue() == activityId.longValue()) {
                    i++;
                }
            }
            if (i < 5) {
                return false;
            } else {
                return true;
            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean clickTaskReceive(String uid, Long activityId) {
        Boolean IsIntegral = isIntegral(uid, activityId);
        if (IsIntegral) {
            ActivityTask activityTask = activityTaskRepository.ActivityIs(activityId);
            ScoreLog scoreLog = new ScoreLog();
            scoreLog.setUid(uid)
                    .setRecordId(activityTask.getTaskId())
                    .setType(true)
                    .setScore(activityTask.getRewardScore())
                    .setDescription("斗地主点赞获得积分")
                    .setTranType(0)
                    .setCreateTime(LocalDateTime.now());
            scoreLogRepository.save(scoreLog);
            User user =userService.findById(uid);
            Long newScore = user.getScore() + activityTask.getRewardScore();
            user.setScore(newScore);
            userService.save(user);
            return true;
        } else {
            return false;
        }


    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean videoTaskReceive(String uid, Long activityId) {


        return true;
    }

    @Override
    public Boolean sendVideoReceive(String uid, Long activityId) {
        List<ActivityTask> activityTasks = activityTaskRepository.findActivityTaskByActivityId(activityId);
        Optional<ActivityTask> exist = activityTasks.stream().filter(activityTask -> activityTask.getIsMain().equals(3)).findAny();
        if (exist.isPresent()){
            List<Video> videos = videoRepository.taskSendAct(uid, activityId);
            ExpFunction.throwNoCodeBiz(videos.isEmpty(),"您尚未完成任务");
            ActivityTask task = exist.get();
//            TaskUserFetch fetch = taskUserFetchRepository.findTodayTask(uid,task.getTaskId(),activityId);
//            if (fetch.getCompleted())
            Long rewardScore = task.getRewardScore();
            ScoreLog scoreLog = buildScoreLog(uid, task.getRewardScore(), task.getTaskId());
            User user = userService.findById(uid);
            userService.save(user.setScore(user.getScore() + rewardScore));
            scoreLogRepository.save(scoreLog);
        }

        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        System.out.println(today_start.toInstant(ZoneOffset.of("+8")).toEpochMilli());

        return null;
    }

    private ScoreLog buildScoreLog(String uid, Long consumeScore, Long recordId) {
        return new ScoreLog()
                .setUid(uid)
                .setScore(consumeScore)
                .setRecordId(recordId)
                .setTranType(TASK)
                .setDescription("斗地主发视频获得积分")
                .setType(true);
    }
}
