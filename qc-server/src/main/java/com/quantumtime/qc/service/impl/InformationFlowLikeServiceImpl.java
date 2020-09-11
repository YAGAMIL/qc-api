package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.exception.BizException;
import com.quantumtime.qc.common.exception.JpaCrudException;
import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.common.service.impl.BaseServiceImpl;
import com.quantumtime.qc.constant.InformationConstant;
import com.quantumtime.qc.constant.NotificationConstant;
import com.quantumtime.qc.entity.information.InformationFlow;
import com.quantumtime.qc.entity.information.InformationFlowLike;
import com.quantumtime.qc.entity.undo.Notification;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.repository.InformationFlowLikeRepository;
import com.quantumtime.qc.service.IInformationFlowLikeService;
import com.quantumtime.qc.service.IInformationFlowService;
import com.quantumtime.qc.service.INotificationService;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.wrap.information.InformationFlowDetailLikeWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class InformationFlowLikeServiceImpl extends BaseServiceImpl<InformationFlowLike, Long, InformationFlowLikeRepository> implements IInformationFlowLikeService {

    @Autowired
    private AccountHelp accountHelp;

    @Autowired
    private IInformationFlowService informationFlowService;

    @Autowired
    private INotificationService notificationService;

    @Autowired
    private IUserService userService;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InformationFlowLike likeInformationFlow(InformationFlowLike entity) throws JpaCrudException {
        accountHelp.checkCurrentAccountStatusOfEnable();
        User currentUser = accountHelp.getCurrentUser();
        InformationFlowLike like = this.baseRepository.findByinformationFlowIdAndCreateId(entity.getInformationFlowId(), currentUser.getUid());
        informationFlowService.updateLikeNum(entity.getInformationFlowId(), 1L);
        if(like == null) {
            entity.setCreateId(currentUser.getUid());
            entity.setIsDelete(false);
            entity = this.save(entity);
            //加入通知
            Notification notification = new Notification();
            InformationFlow flow = informationFlowService.findById(entity.getInformationFlowId());
            notification.setNotifiUserId(flow.getCreateId());
            notification.setBizId(entity.getId());
            notification.setFlowId(flow.getId());
            notification.setStatus(NotificationConstant.UN_READ);
            notification.setType(NotificationConstant.TYPE_LIKE);
            notification.setTitle(currentUser.getNickname() + NotificationConstant.LIKE_CONTENT);
            notification.setBody(flow.getContent());
            notificationService.pushNotification(notification);
            return entity;
        }else if(like.getIsDelete()){
            InformationFlowLike dbLike = new InformationFlowLike();
            dbLike.setIsDelete(false);
            dbLike.setId(like.getId());
            return this.update(dbLike);
        }else{
            throw new BizException(ErrorCodeConstant.INFORMATION_LIKE_EXIST, new Throwable());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeLikeInformation(Long id) throws JpaCrudException {
        accountHelp.checkCurrentAccountStatusOfEnable();
        User currentUser = accountHelp.getCurrentUser();
        InformationFlowLike like = this.findById(id);
        if(!currentUser.getUid().equals(like.getCreateId())){
            throw new BizException(ErrorCodeConstant.GLOBAL_PERMISSION_ERROR, new Throwable());
        }
        if(!like.getIsDelete()) {
            informationFlowService.updateLikeNum(like.getInformationFlowId(), -1L);
            InformationFlowLike dbLike = new InformationFlowLike();
            dbLike.setId(like.getId());
            dbLike.setIsDelete(true);
            this.update(dbLike);
        }else{
            throw new BizException(ErrorCodeConstant.INFORMATION_LIKE_NOT_EXIST, new Throwable());
        }
        return true;
    }

    @Override
    public BasePage<InformationFlowDetailLikeWrap, Long> findPage(BasePage<InformationFlowLike, Long> page) {
        List<InformationFlowDetailLikeWrap> result = new ArrayList<>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select l from InformationFlowLike as l ");
        hql.append(" where 1=1 ");
        hql.append(" and l.informationFlowId = ?1 ");
        hql.append(" and l.isDelete = 0");
        if(page.getId() != null && page.getId() > 0) {
            hql.append(" and l.id < ?2 ");
        }
        hql.append(" order by l.upTime desc");
        Query query = em.createQuery(hql.toString());
        query.setParameter(1, page.getFilters().getInformationFlowId());
        if(page.getId() != null && page.getId() > 0) {
            query.setParameter(2, page.getId());
        }
        query.setMaxResults(page.getSize());
        List<InformationFlowLike> list = query.getResultList();
        for(InformationFlowLike like : list){
            result.add(wrapInformationFlowLike(like));
        }
        return new BasePage<InformationFlowDetailLikeWrap, Long>(page.getId(), page.getSize(), result.size(), result);
    }

    @Override
    public InformationFlowDetailLikeWrap findWrapById(Long id) {
        InformationFlowLike like = ((IInformationFlowLikeService) AopContext.currentProxy()).findById(id);
        if(like.getIsDelete()){
            throw new BizException(ErrorCodeConstant.INFORMATION_LIKE_DELETE, new Throwable());
        }
        return wrapInformationFlowLike(like);
    }


    @Override
    public InformationFlowLike findByinformationFlowIdAndCreateId(Long informationFlowId, String createId) {
        return this.baseRepository.findByinformationFlowIdAndCreateId(informationFlowId, createId);
    }

    private InformationFlowDetailLikeWrap wrapInformationFlowLike(InformationFlowLike like){
        User user = userService.findById(like.getCreateId());
        InformationFlowDetailLikeWrap wrap = new InformationFlowDetailLikeWrap();
        wrap.setInformationFlowLike(like);
        wrap.setContent(InformationConstant.INFORMATION_LIKE);
        wrap.setLikeAvatar(user.getAvatar());
        wrap.setLikeNickName(user.getNickname());
        wrap.setLikeUserId(user.getUid());
        return wrap;
    }

}
