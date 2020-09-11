package com.quantumtime.qc.service.impl.activity;

import com.quantumtime.qc.entity.activity.Activity;
import com.quantumtime.qc.entity.activity.ActivityTask;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.entity.task.Task;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.repository.ActivityRepository;
import com.quantumtime.qc.repository.ActivityTaskRepository;
import com.quantumtime.qc.repository.AddressRepository;
import com.quantumtime.qc.repository.TaskRepository;
import com.quantumtime.qc.service.ActivityService;
import com.quantumtime.qc.service.IAddressService;
import com.quantumtime.qc.service.impl.RelationshipServiceImpl;
import com.quantumtime.qc.service.impl.UserTaskServiceImpl;
import com.quantumtime.qc.vo.ActivityDetailVo;
import com.quantumtime.qc.vo.PoiIdAndActivityVo;
import com.quantumtime.qc.vo.TaskDetailVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Description:
 * Program:qc-api
 * </p>
 * Created on 2019-12-13 15:49
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    ActivityRepository activityRepository;
    @Resource
    AddressRepository addressRepository;
    @Resource
    ActivityTaskRepository activityTaskRepository;
    @Resource
    TaskRepository taskRepository;
    @Resource
    IAddressService iAddressService;
    @Resource
    UserTaskServiceImpl userTaskService;
    @Resource
    private AccountHelp accountHelp;
    @Resource
    private RelationshipServiceImpl relationshipService;

    @Override
    public Activity activityList(Address address) {
        long addressId = iAddressService.checkAndSet(address);
        return isActivity(addressId);
    }

    Activity isActivity(long addressId) {
        Activity returnActivity = new Activity();
        Activity activity = activityRepository.isAddressId(addressId);
        if (null != activity) {
            returnActivity = activity;
        } else {
            Activity activityBackstage = activityRepository.isBackstage(addressId);
            if (null != activityBackstage) {
                returnActivity = activityBackstage;
            } else {
                Address address = addressRepository.findById(addressId);
                Activity isBackstageName = activityRepository.isBackstageName(address.getCity());
                if (null != isBackstageName) {
                    return isBackstageName;
                } else {
                    Activity isAllActivity = activityRepository.isBackstageName("全国");
                    if (null != isAllActivity) {
                        returnActivity = isAllActivity;
                    } else {
                        return null;
                    }
                }
            }
        }
        return returnActivity;


    }


    @Override
    public ActivityDetailVo activityDetail(String poiId) {
        ActivityDetailVo activityDetailVo = new ActivityDetailVo();
        Address address = addressRepository.findAddressByPoiId(poiId);
        Activity activity = isActivity(address.getId());

        List<TaskDetailVo> taskDetailVoList = new ArrayList<>();
        if (activity == null) {
            return null;
        } else {
            List<Long> taskIds = activityTaskRepository.findByActivityId(activity.getId());
            activityDetailVo.setActivity(activity);
            List<Task> taskList = taskRepository.findAllById(taskIds);
            String uid = accountHelp.getCurrentUser().getUid();
            for (Task task : taskList) {
                TaskDetailVo taskDetailVo = taskStatus(task.getType(), task, uid, activity);
                BeanUtils.copyProperties(task, taskDetailVo);
                taskDetailVoList.add(taskDetailVo);
            }
            activityDetailVo.setTaskList(taskDetailVoList);
        }
        return activityDetailVo;
    }


    public TaskDetailVo taskStatus(int type, Task task, String uid, Activity activity) {
        TaskDetailVo taskDetailVo = new TaskDetailVo();
        if (type == 8) {
            Boolean isReceiveTask = userTaskService.isReceive(task.getId(), uid);
            if (isReceiveTask == true) {
                taskDetailVo.setTaskStatus(1);
            } else {
                taskDetailVo.setTaskStatus(0);
            }
            Boolean isIntegral = relationshipService.isIntegral(uid, activity.getId());
            if (isIntegral == true) {
                taskDetailVo.setIntegralStatus(1);
            } else {
                taskDetailVo.setIntegralStatus(0);
            }


        } else if (type == 9) {


        } else if (type == 10) {

        }
        ActivityTask activityTask = activityTaskRepository.findByTaskId(task.getId());
        taskDetailVo.setScore(activityTask.getRewardScore());

        return taskDetailVo;
    }


    @Override
    public List<PoiIdAndActivityVo> nearbyActivity(List<String> poiId) {
        List<Address> addressList = addressRepository.findAddressByPoiIds(poiId);

        List<PoiIdAndActivityVo> PoiIdAndActivityVoList = new ArrayList<>();
        for (Address address : addressList) {
            Activity activity = isActivity(address.getId());
            if (activity != null) {
                PoiIdAndActivityVo poiIdAndActivityVo = new PoiIdAndActivityVo();
                poiIdAndActivityVo.setPoiId(address.getPoiId());
                poiIdAndActivityVo.setActivity(activity.getConversation());
                PoiIdAndActivityVoList.add(poiIdAndActivityVo);
            }
            if (PoiIdAndActivityVoList.size() == 2) {
                break;
            }

        }
        return PoiIdAndActivityVoList;
    }

    @Override
    public List<Activity> queryActive(LocalDateTime now) {
        return activityRepository.findActive(now);
    }

}

