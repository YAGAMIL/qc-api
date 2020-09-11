package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.common.service.impl.BaseServiceImpl;
import com.quantumtime.qc.constant.NotificationConstant;
import com.quantumtime.qc.entity.*;
import com.quantumtime.qc.entity.information.InformationFlow;
import com.quantumtime.qc.entity.information.InformationFlowCommit;
import com.quantumtime.qc.entity.information.InformationFlowLike;
import com.quantumtime.qc.entity.undo.Notification;
import com.quantumtime.qc.repository.NotificationRepository;
import com.quantumtime.qc.service.*;
import com.quantumtime.qc.vo.notification.AggregationNotification;
import com.quantumtime.qc.wrap.notification.NotificationWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl extends BaseServiceImpl<Notification, Long, NotificationRepository> implements INotificationService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private IUserService userService;

    @Autowired
    private IInformationFlowCommitService commitService;

    @Autowired
    private IInformationFlowService flowService;

    @Autowired
    private IInformationFlowLikeService likeService;

    @Autowired
    private AliPushService aliPushService;

    @Override
    public Long countUnReadNotification(String userId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select count(1) from Notification as n ");
        hql.append(" where 1=1 ");
        hql.append(" and n.notifiUserId = ?1 ");
        hql.append(" and n.status = 0 ");
        Query query = em.createQuery(hql.toString());
        query.setParameter(1, userId);
        Long count = (Long) query.getSingleResult();
        return count;
    }

    @Override
    public BasePage<NotificationWrap, Long> findPage(BasePage<Notification, Long> page){
        List<NotificationWrap> result = new ArrayList<>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select n from Notification as n ");
        hql.append(" where 1=1 ");
        hql.append(" and n.notifiUserId = ?1 ");
        if(page.getId() != null && page.getId() > 0) {
            hql.append(" and n.id < ?2 ");
        }
        if(page.getFilters() != null && page.getFilters().getStatus() != null){
            hql.append(" and n.status= ?3 ");
        }
        hql.append(" order by n.upTime desc");
        Query query = em.createQuery(hql.toString());
        query.setParameter(1, page.getFilters().getNotifiUserId());
        if(page.getId() != null && page.getId() > 0) {
            query.setParameter(2, page.getId());
        }
        if(page.getFilters() != null && page.getFilters().getStatus() != null){
            query.setParameter(3, page.getFilters().getStatus());
        }
        query.setMaxResults(page.getSize());
        List<Notification> list = query.getResultList();
        for(Notification n : list){
            NotificationWrap wrap = wrapNotification(n);
            result.add(wrap);
        }
        return new BasePage<NotificationWrap, Long>(page.getId(), page.getSize(), result.size(), result);
    }

    private NotificationWrap wrapNotification(Notification n) {
        NotificationWrap wrap = new NotificationWrap();
        wrap.setNotification(n);
        String nickName = null;
        String content = null;
        String avatar = null;
        if(n.getType().equals(NotificationConstant.TYPE_COMMIT)) {
            wrap.setContent(NotificationConstant.COMMIT_CONTENT);
            InformationFlowCommit commit = commitService.findById(n.getBizId());
            User user = userService.findById(commit.getCreateId());
            nickName = user.getNickname();
            avatar = user.getAvatar();
            content = commit.getContent();
        }else if(n.getType().equals(NotificationConstant.TYPE_FORWARD)){
            wrap.setContent(NotificationConstant.FORWARD_CONTENT);
            InformationFlow flow = flowService.findById(n.getBizId());
            User user = userService.findById(flow.getCreateId());
            nickName = user.getNickname();
            avatar = user.getAvatar();
            content = flow.getContent();
        }else{
            wrap.setContent(NotificationConstant.LIKE_CONTENT);
            InformationFlowLike like = likeService.findById(n.getBizId());
            User user = userService.findById(like.getCreateId());
            nickName = user.getNickname();
            avatar = user.getAvatar();
            content = "";
        }
        wrap.setSourceUserNickName(nickName);
        wrap.setSourceUserAvatar(avatar);
        if(content.length() < NotificationConstant.CONTENT_PREFIX_LENGTH){
            wrap.setSourceContentPrefix(content);
        }else {
            wrap.setSourceContentPrefix(content.substring(0, NotificationConstant.CONTENT_PREFIX_LENGTH) + NotificationConstant.CONTENT_SUFFIX);
        }
        return wrap;
    }

    @Override
    public List<AggregationNotification> findAggregationNotificationList(String userId) {
        List<AggregationNotification> result = new ArrayList<>();
        List<Notification> commitList = this.baseRepository.findAllByNotifiUserIdAndStatusAndType(userId, NotificationConstant.UN_READ, NotificationConstant.TYPE_COMMIT);
        if(commitList.size() > 0) {
            AggregationNotification commitAg = new AggregationNotification();
            commitAg.setTitle(NotificationConstant.COMMIT);
            commitAg.setNotificationIds(commitList.stream().map(n -> n.getId()).collect(Collectors.toList()));
            commitAg.setType(NotificationConstant.TYPE_COMMIT);
            commitAg.setSize(commitAg.getNotificationIds().size());
            commitAg.setNotificationWrap(wrapNotification(commitList.get(0)));
            result.add(commitAg);
        }
        List<Notification> forwardList = this.baseRepository.findAllByNotifiUserIdAndStatusAndType(userId, NotificationConstant.UN_READ, NotificationConstant.TYPE_FORWARD);
        if(forwardList.size() > 0) {
            AggregationNotification forwardAg = new AggregationNotification();
            forwardAg.setTitle(NotificationConstant.FORWARD);
            forwardAg.setNotificationIds(forwardList.stream().map(n -> n.getId()).collect(Collectors.toList()));
            forwardAg.setType(NotificationConstant.TYPE_FORWARD);
            forwardAg.setSize(forwardAg.getNotificationIds().size());
            forwardAg.setNotificationWrap(wrapNotification(forwardList.get(0)));
            result.add(forwardAg);
        }
        List<Notification> likeList = this.baseRepository.findAllByNotifiUserIdAndStatusAndType(userId, NotificationConstant.UN_READ, NotificationConstant.TYPE_LIKE);
        if(likeList.size() > 0) {

            AggregationNotification likeAg = new AggregationNotification();
            likeAg.setTitle(NotificationConstant.LIKE);
            likeAg.setNotificationIds(likeList.stream().map(n -> n.getId()).collect(Collectors.toList()));
            likeAg.setType(NotificationConstant.TYPE_LIKE);
            likeAg.setSize(likeAg.getNotificationIds().size());
            likeAg.setNotificationWrap(wrapNotification(likeList.get(0)));
            result.add(likeAg);
        }
        return result;
    }

    @Override
    public List<NotificationWrap> findByIds(List<Long> ids) {
        List<NotificationWrap> wraps = new ArrayList<>();
        List<Notification> all = this.baseRepository.findAllById(ids);
        for(Notification n : all){
            wraps.add(wrapNotification(n));
        }
        return wraps;
    }

    @Override
    public Boolean isReadByIds(List<Long> ids) {
        this.baseRepository.updateStatusByIds(NotificationConstant.READ, ids);
        return true;
    }

    @Override
    public Notification pushNotification(Notification notification) {
        notification = this.save(notification);
        //异步推送 捕获所有异常
        aliPushService.push(notification);
        return notification;
    }
}
