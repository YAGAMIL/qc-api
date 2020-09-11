package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.exception.ExpFunction;
import com.quantumtime.qc.entity.Invite;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.entity.feeds.Video;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.entity.score.ScoreLog;
import com.quantumtime.qc.entity.task.GoodContentScore;
import com.quantumtime.qc.entity.task.Task;
import com.quantumtime.qc.entity.task.TaskRule;
import com.quantumtime.qc.entity.task.TaskUserFetch;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.repository.GoodContentScoreRepository;
import com.quantumtime.qc.repository.InviteRelationRepository;
import com.quantumtime.qc.repository.ScoreLogRepository;
import com.quantumtime.qc.repository.TaskRepository;
import com.quantumtime.qc.repository.TaskRuleRepository;
import com.quantumtime.qc.repository.TaskUserFetchRepository;
import com.quantumtime.qc.repository.UserRepository;
import com.quantumtime.qc.repository.VideoRepository;
import com.quantumtime.qc.service.IAddressService;
import com.quantumtime.qc.service.UserTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserTaskServiceImpl implements UserTaskService {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final String TIME_DIFFERENCE = " 23:59:59";
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskUserFetchRepository taskUserFetchRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private TaskRuleRepository taskRuleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScoreLogRepository scoreLogRepository;

    @Autowired
    private InviteRelationRepository inviteRelationRepository;

    @Autowired
    private AccountHelp accountHelp;

    @Autowired
    private IAddressService addressService;

    @Autowired
    private GoodContentScoreRepository goodContentScoreRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String processInvite(Task task) {
        long start = System.currentTimeMillis();
        List<TaskRule> taskRuleList = taskRuleRepository.getByTaskId(task.getId());
        if (taskRuleList == null || taskRuleList.size() == 0) {
            return "processInvite no taskRuleList \n";
        }
        TaskRule taskRule = taskRuleList.get(0);
        int days = Integer.parseInt(taskRule.getParam1Value());
        int num = Integer.parseInt(taskRule.getParam2Value());
        int lowScore = Integer.parseInt(taskRule.getParam3Value());
        long reward = Long.parseLong(taskRule.getParam4Value());

        //找出days天内维护邀请关系的新用户
        long delta = days * 24L * 3600 * 1000;
        Date startTime = new Date(System.currentTimeMillis() - delta);
        //和活动开始时间比较
        if (startTime.before(task.getStartTime())) {
            startTime = task.getStartTime();
        }
        List<User> userList = null;
        if (task.getAreaType().equals(Task.TASK_AREA_TYPE_COUNTRY)) {
            userList = userRepository.findNewInviteUser(startTime);
        } else if (task.getAreaType().equals(Task.TASK_AREA_TYPE_CITY)) {
            userList = userRepository.findNewInviteUserCity(task.getAreaName(), startTime);
        } else {
            userList = userRepository.findNewInviteUserProvince(task.getAreaName(), startTime);
        }
        if (userList == null || userList.size() == 0) {
            return "processInvite no user \n";
        }
        StringBuilder sb = new StringBuilder();
        int total = 0;
        for (User user : userList) {
            List<Video> videoList = videoRepository
                    .findTopScore(user.getUid(), num, startTime, new Date());
            long incr = 0;
            if (videoList != null && videoList.size() == num) {
                boolean pass = true;
                for (Video video : videoList) {
                    Integer score = video.getScore();
                    if (score == null || score < lowScore) {
                        pass = false;
                        break;
                    }
                }
                if (pass) {
                    incr = reward;
                }
            }
            if (incr > 0) {
                Invite invite = inviteRelationRepository.myInviter(user.getId());
                invite.setTaskStatus(Invite.TASK_STATUS_SUCCESS);
                invite.setScore(reward);
                Integer inviterId = invite.getInviterId();
                User inviter = userRepository.findUserById(inviterId);
                //地域限制检查
                if (!checkArea(inviter, task)) {
                    continue;
                }
                inviter.setScore(inviter.getScore() + incr);
                inviteRelationRepository.save(invite);
                userRepository.save(inviter);
                sb.append("User:").append(inviter.getUid())
                        .append(' ').append(inviter.getNickname()).append(' ');
                ScoreLog scoreLog = new ScoreLog();
                scoreLog.setUid(inviter.getUid());
                scoreLog.setScore(reward);
                scoreLog.setType(true);
                scoreLog.setTranType(0);
                scoreLog.setRecordId(invite.getId());
                scoreLog.setDescription(task.getName());
                scoreLog.setCreateTime(LocalDateTime.now());
                scoreLogRepository.save(scoreLog);
                total++;
                sb.append(" score incr:").append(incr).append('\n');
            }
        }
        String msg = "processInvite elapsed:" + (System.currentTimeMillis() - start) / 1000 + "s,user total:" + total + " increase score\n";
        log.info(msg);
        sb.append(msg);
        return sb.toString();
    }

    @Override
    public List<Task> fetchTaskList(String province, String city) {
        return taskRepository.getTaskByArea(province, city);
    }

    private boolean notEffective(Task task) {
        return !task.getEnable() || task.getEndTime() != null && task.getEndTime().before(new Date());
    }

    @Override
    public TaskUserFetch fetchOne(Long taskId) throws Exception {
        User user = accountHelp.getCurrentUser();
        Task task = taskRepository.getOne(taskId);
        ExpFunction.throwNoCodeBiz(notEffective(task),"任务不存在或者已经停止了");
        //查看用户生活圈所在位置是否满足任务举办区域限制
        checkTaskArea(user, task);
        // 查询是否已经领取了
        Optional.ofNullable(taskUserFetchRepository.findExist(taskId, user.getUid()))
                .ifPresent(exist -> ExpFunction.true4ThrowBiz(
                        task.getDurationType() != Task.TASK_DURATION_TYPE_DAY
                                || exist.getExpireTime().after(new Date()),
                        "已经领取过了"));
        TaskUserFetch taskUserFetch = new TaskUserFetch();
        taskUserFetch.setUserId(user.getUid());
        taskUserFetch.setTaskId(taskId);
        taskUserFetch.setCreateTime(new Date());
        if (task.getDurationType() == Task.TASK_DURATION_TYPE_DAY) {
            Date date = new Date();
            String today = sdf.format(date);
            String endTimeStr = today + TIME_DIFFERENCE;
            Date endTime = sdfTime.parse(endTimeStr);
            taskUserFetch.setExpireTime(endTime);
        } else if (task.getDurationType() == Task.TASK_DURATION_TYPE_ONCE) {
            taskUserFetch.setExpireTime(task.getEndTime());
        }
        taskUserFetch.setCount(0);
        taskUserFetch.setCompleted(false);
        taskUserFetchRepository.save(taskUserFetch);
        return taskUserFetch;
    }

    private void checkTaskArea(User user, Task task) {
        if (!task.getAreaType().equals(Task.TASK_AREA_TYPE_COUNTRY)) {
            ExpFunction.throwNoCodeBiz(user.getAddressId() == null,"没有设置生活圈");
            Address address = addressService.findAddressById(user.getAddressId());
            ExpFunction.throwNoCodeBiz(address == null,"找不到地址id:" + user.getAddressId());
            ExpFunction.throwNoCodeBiz(task.getAreaType().equals(Task.TASK_AREA_TYPE_PROVINCE)
                    && !task.getAreaName().equals(address.getProvince()),"所在省不在活动范围之内");
            ExpFunction.throwNoCodeBiz(task.getAreaType().equals(Task.TASK_AREA_TYPE_CITY)
                    && !task.getAreaName().equals(address.getCity()),"所在市不在活动范围之内");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String processGoodContent(Task task) throws ParseException {
        long start = System.currentTimeMillis();
        //找到领取任务的用户
        Date now = new Date();
        String todayStr = sdf.format(now);
        String startTimeStr = todayStr + " 00:00:00";
        String endTimeStr = todayStr + " 23:59:59";
        Date startTime = sdfTime.parse(startTimeStr);
        Date endTime = sdfTime.parse(endTimeStr);

        List<TaskUserFetch> taskUserFetchList = taskUserFetchRepository.findToday(task.getId(), startTime, endTime);
        if (taskUserFetchList == null || taskUserFetchList.size() == 0) {
            return "processGoodContent no taskUserFetchList \n";
        }
        //找出规则
        List<TaskRule> taskRuleList = taskRuleRepository.getByTaskId(task.getId());
        if (taskRuleList == null || taskRuleList.size() == 0) {
            return "processGoodContent no taskRuleList \n";
        }
        int top = Integer.parseInt(taskRuleList.get(0).getParam1Value());
        Map<Integer, Integer> scoreMap = new HashMap<>();
        Integer max = 0;
        for (int i = 1; i < taskRuleList.size(); i++) {
            TaskRule taskRule = taskRuleList.get(i);
            Integer score = Integer.parseInt(taskRule.getParam1Value());
            if (score > max) {
                max = score;
            }
            scoreMap.put(score,
                    Integer.parseInt(taskRule.getParam2Value()));
        }
        StringBuilder sb = new StringBuilder();
        int total = 0;
        for (TaskUserFetch taskUserFetch : taskUserFetchList) {
            //找出该用户今日所发前n个合格视频
            User user = userRepository.findUserByUid(taskUserFetch.getUserId());
            //检测区域
            if (!checkArea(user, task)) {
                continue;
            }
            sb.append("Cal uid:" + taskUserFetch.getUserId()).append(' ');
            List<Video> videoList = videoRepository
                    .findFront(taskUserFetch.getUserId(), top, startTime, endTime);
            //找出已经给过积分的
            List<GoodContentScore> goodContentScoreList = goodContentScoreRepository.getByTaskUserFetchId(taskUserFetch.getId());
            Set<String> videoIds = new HashSet<>();
            if (goodContentScoreList != null && goodContentScoreList.size() > 0) {
                for (GoodContentScore goodContentScore : goodContentScoreList) {
                    videoIds.add(goodContentScore.getVideoId());
                }
            }
            int incr = 0;
            if (videoList != null && taskUserFetch.getCount() < top
                    && videoList.size() > 0) {
                for (int i = 0; i < videoList.size(); i++) {
                    Video video = videoList.get(i);
                    if (videoIds.contains(video.getVideoId())) {
                        continue;
                    }
                    Integer score = video.getScore();
                    Integer plus = 0;
                    if (score != null) {
                        if (score >= max) {
                            plus = scoreMap.get(max);
                            incr += plus;
                        } else {
                            plus = scoreMap.get(score);
                            if (plus != null) {
                                incr += plus;
                            }
                        }
                    }
                    GoodContentScore goodContentScore = new GoodContentScore();
                    goodContentScore.setScore(plus != null ? (long) plus : 0L);
                    goodContentScore.setVideoId(video.getVideoId());
                    goodContentScore.setTaskUserFetchId(taskUserFetch.getId());
                    goodContentScore.setCreateTime(now);
                    goodContentScoreRepository.save(goodContentScore);
                    taskUserFetch.setCount(taskUserFetch.getCount() + 1);
                    videoIds.add(video.getVideoId());
                    if (taskUserFetch.getCount() == top) {
                        break;
                    }
                }
            }
            sb.append("user score incr:").append(incr).append('\n');
            if (incr > 0) {
                user.setScore(user.getScore() + incr);
                userRepository.save(user);
                if (taskUserFetch.getCount().equals(top)) {
                    taskUserFetch.setCompleted(true);
                }
                taskUserFetchRepository.save(taskUserFetch);
                ScoreLog scoreLog = new ScoreLog();
                scoreLog.setUid(user.getUid());
                scoreLog.setScore((long) incr);
                scoreLog.setType(true);
                scoreLog.setTranType(0);
                scoreLog.setRecordId(taskUserFetch.getId());
                scoreLog.setDescription(task.getName());
                scoreLog.setCreateTime(LocalDateTime.now());
                scoreLogRepository.save(scoreLog);
                total++;
            }
        }
        String msg = "processGoodContent elapsed:"
                + (System.currentTimeMillis() - start) / 1000 + "s,user total:" + total + " increase score\n";
        log.info(msg);
        sb.append(msg);
        return sb.toString();
    }

    private boolean checkArea(User user, Task task) {
        if (user.getAddressId() == null) {
            return false;
        }
        Address address = addressService.findAddressById(user.getAddressId());
        if (address == null) {
            return false;
        }
        if (!task.getAreaType().equals(Task.TASK_AREA_TYPE_COUNTRY)) {
            if (task.getAreaType().equals(Task.TASK_AREA_TYPE_PROVINCE)
                    && !task.getAreaName().equals(address.getProvince())) {
                return false;
            }
            if (task.getAreaType().equals(Task.TASK_AREA_TYPE_CITY)
                    && !task.getAreaName().equals(address.getCity())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isToday(Date inputJudgeDate) {
        boolean flag = false;
        //获取当前系统时间
        long longDate = System.currentTimeMillis();
        Date nowDate = new Date(longDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(nowDate);
        String subDate = format.substring(0, 10);
        //定义每天的24h时间范围
        String beginTime = subDate + " 00:00:00";
        String endTime = subDate + " 23:59:59";
        Date paseBeginTime = null;
        Date paseEndTime = null;
        try {
            paseBeginTime = dateFormat.parse(beginTime);
            paseEndTime = dateFormat.parse(endTime);

        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        if (inputJudgeDate.after(paseBeginTime) && inputJudgeDate.before(paseEndTime)) {
            flag = true;
        }
        return flag;
    }

    public Boolean isReceive(long taskId, String uid) {
        TaskUserFetch userCurrentTask = taskUserFetchRepository.findCurrentTask(uid, taskId);
        if (userCurrentTask == null) {
            return true;
        } else {
            Boolean isToday = isToday(userCurrentTask.getCreateTime());
            if (isToday == true) {
                return false;
            } else {
                return true;
            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean likeTaskStart(long taskId, String uid)  {
        Task task = taskRepository.getOne(taskId);
         Boolean isTrue= isReceive(taskId,uid);
         if(isTrue==true){
             TaskUserFetch taskUserFetch = new TaskUserFetch();
             taskUserFetch.setUserId(uid)
                     .setCreateTime(new Date())
                     .setCompleted(false)
                     .setExpireTime(task.getEndTime())
                     .setTaskId(taskId);
             taskUserFetchRepository.save(taskUserFetch);
             return true;
         }else{
             return false;
         }




    }
}
