package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.exception.BizException;
import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.common.service.impl.BaseServiceImpl;
import com.quantumtime.qc.constant.NotificationConstant;
import com.quantumtime.qc.entity.information.InformationFlow;
import com.quantumtime.qc.entity.information.InformationFlowCommit;
import com.quantumtime.qc.entity.undo.Notification;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.repository.InformationFlowCommitRepository;
import com.quantumtime.qc.service.IInformationFlowCommitService;
import com.quantumtime.qc.service.IInformationFlowService;
import com.quantumtime.qc.service.INotificationService;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.wrap.information.InformationFlowDetailCommitWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 信息流提交实现
 * Created on 2019/10/11 10:49
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Service
@Slf4j
public class InformationFlowCommitServiceImpl extends BaseServiceImpl<InformationFlowCommit, Long, InformationFlowCommitRepository> implements IInformationFlowCommitService {
    @Resource
    private AccountHelp accountHelp;

    @Resource
    private IInformationFlowService informationFlowService;

    @Resource
    private IUserService userService;

    @Resource
    private INotificationService notificationService;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InformationFlowCommit commitInformationFlow(InformationFlowCommit entity) {
        accountHelp.checkCurrentAccountStatusOfEnable();
        User currentUser = accountHelp.getCurrentUser();
        informationFlowService.updateCommitNum(entity.getInformationFlowId(), 1L);
        entity.setIsDelete(false);
        entity.setCreateId(currentUser.getUid());
        //加入通知
        InformationFlowCommit saveCommit = this.save(entity);
        Notification notification = new Notification();
        InformationFlow flow = informationFlowService.findById(entity.getInformationFlowId());
        notification.setNotifiUserId(flow.getCreateId());
        notification.setBizId(saveCommit.getId());
        notification.setFlowId(flow.getId());
        notification.setStatus(NotificationConstant.UN_READ);
        notification.setType(NotificationConstant.TYPE_COMMIT);
        notification.setTitle(currentUser.getNickname() + NotificationConstant.COMMIT_CONTENT);
        notification.setBody(entity.getContent());
        notificationService.pushNotification(notification);
        return saveCommit;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeCommitInformation(Long id) {
        accountHelp.checkCurrentAccountStatusOfEnable();
        User currentUser = accountHelp.getCurrentUser();
        InformationFlowCommit commit = this.findById(id);
        if(!currentUser.getUid().equals(commit.getCreateId())){
            throw new BizException(ErrorCodeConstant.GLOBAL_PERMISSION_ERROR, new Throwable());
        }
        informationFlowService.updateCommitNum(commit.getInformationFlowId(), -1L);
        commit.setIsDelete(true);
        this.update(commit);
        return true;
    }

    @Override
    public BasePage<InformationFlowDetailCommitWrap, Long> findPage(BasePage<InformationFlowCommit, Long> page) {
        List<InformationFlowDetailCommitWrap> result = new ArrayList<>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select c from InformationFlowCommit as c ");
        hql.append(" where 1=1 ");
        hql.append(" and c.informationFlowId = ?1 ");
        hql.append(" and c.isDelete = 0");
        if(page.getId() != null && page.getId() > 0) {
            hql.append(" and c.id < ?2 ");
        }
        hql.append(" order by c.upTime desc");
        Query query = em.createQuery(hql.toString());
        query.setParameter(1, page.getFilters().getInformationFlowId());
        if(page.getId() != null && page.getId() > 0) {
            query.setParameter(2, page.getId());
        }
        query.setMaxResults(page.getSize());
        List<InformationFlowCommit> list = query.getResultList();
        for(InformationFlowCommit commit : list){
            result.add(wrapInformationFlowCommit(commit));
        }
        return new BasePage<InformationFlowDetailCommitWrap, Long>(page.getId(), page.getSize(), result.size(), result);
    }

    @Override
    public InformationFlowDetailCommitWrap findWrapById(Long id) {
        InformationFlowCommit commit = ((IInformationFlowCommitService) AopContext.currentProxy()).findById(id);
        if(commit.getIsDelete()){
            throw new BizException(ErrorCodeConstant.INFORMATION_COMMIT_DELETE, new Throwable());
        }
        return wrapInformationFlowCommit(commit);
    }

    private InformationFlowDetailCommitWrap wrapInformationFlowCommit(InformationFlowCommit commit){
        User user = userService.findById(commit.getCreateId());
        InformationFlowDetailCommitWrap wrap = new InformationFlowDetailCommitWrap();
        wrap.setInformationFlowCommit(commit);
        wrap.setCommitAvatar(user.getAvatar());
        wrap.setCommitNickName(user.getNickname());
        wrap.setCommitUserId(user.getUid());
        return wrap;
    }
}
