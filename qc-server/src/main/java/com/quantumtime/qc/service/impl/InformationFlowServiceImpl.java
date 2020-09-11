package com.quantumtime.qc.service.impl;

import com.alibaba.fastjson.JSON;
import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.constant.ThemeConstant;
import com.quantumtime.qc.common.exception.BizException;
import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.common.service.impl.BaseServiceImpl;
import com.quantumtime.qc.constant.InformationConstant;
import com.quantumtime.qc.constant.NotificationConstant;
import com.quantumtime.qc.entity.*;
import com.quantumtime.qc.entity.information.InformationFlow;
import com.quantumtime.qc.entity.information.InformationFlowLike;
import com.quantumtime.qc.entity.undo.Notification;
import com.quantumtime.qc.entity.undo.RelUserAndTheme;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.repository.InformationFlowRepository;
import com.quantumtime.qc.service.*;
import com.quantumtime.qc.wrap.information.InformationFlowDetailForwardWrap;
import com.quantumtime.qc.wrap.information.InformationFlowWrap;
import com.quantumtime.qc.wrap.information.InformationHierarchy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@Slf4j
public class InformationFlowServiceImpl extends BaseServiceImpl<InformationFlow, Long, InformationFlowRepository> implements IInformationFlowService{

    @Autowired
    private IInformationFlowCommitService commitService;

    @Autowired
    private IInformationFlowLikeService likeService;

    @Autowired
    private IInformationFlowThemeService themeService;

    @Autowired
    private IAddressService addressService;

    @Autowired
    private IUserService userService;

    @Autowired
    private AccountHelp accountHelp;

    @Autowired
    private INotificationService notificationService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private IRelUserAndThemeService relUserAndThemeService;


    @Cacheable(value = "cache-information", key = "'id'+#id", unless="#result == null")
    @Override
    public InformationFlow findById(Long id) {
        return super.findById(id);
    }

    @Override
    public InformationFlowWrap findWrapById(Long id){
        InformationFlow flow = ((IInformationFlowService) AopContext.currentProxy()).findById(id);
        if(flow.getIsDelete()){
            throw new BizException(ErrorCodeConstant.INFORMATION_DELETE, new Throwable());
        }
        InformationFlowWrap wrap = new InformationFlowWrap();
        wrap.setInformationFlow(flow);
        wrap = syncInformationWrap(wrap);
        return wrap;
    }

    @Override
    public BasePage<InformationFlowWrap, Long> findPublishList(BasePage<InformationFlow, Long> page) {
        List<InformationFlowWrap> result = new ArrayList<>();
        accountHelp.checkCurrentAccountStatusOfRegist();
        User currentUser = accountHelp.getCurrentUser();
        Integer currentUserScope = accountHelp.getCurrentUserScope();
        List<InformationFlow> flows = null;
        StringBuilder hql = new StringBuilder();
        hql.append(" from InformationFlow");
        hql.append(" where 1=1 ");
        hql.append(" and isDelete=0 ");
        hql.append(" and type=1");
        if(page.getId() != null && page.getId() > 0) {
            hql.append(" and id < ?1 ");
        }
        hql.append( " and createId=?2 ");
        hql.append(" order by upTime desc");
        Query query = em.createQuery(hql.toString());
        if(page.getId() != null && page.getId() > 0) {
            query.setParameter(1, page.getId());
        }
        query.setParameter(2, currentUser.getUid());
        query.setMaxResults(page.getSize());
        flows = query.getResultList();
        for(InformationFlow flow : flows){
            if(StringUtils.isNotEmpty(flow.getForwardResult())){
                //同步昵称
                InformationHierarchy hierarchy = JSON.parseObject(flow.getForwardResult(), InformationHierarchy.class);
                hierarchy = syncInformationWrapInTheInformationHierarchy(hierarchy);
                flow.setForwardResult(JSON.toJSONString(hierarchy));
            }
            InformationFlowWrap wrap = new InformationFlowWrap();
            wrap.setInformationFlow(flow);
            wrap = syncInformationWrap(wrap, false);
            result.add(wrap);
        }
        return new BasePage<InformationFlowWrap, Long>(page.getId(), page.getSize(), result.size(), result);
    }

    @Override
    public Long countNewInformation(Long oldId){
        User currentUser = accountHelp.getCurrentUser();
        Integer currentUserScope = accountHelp.getCurrentUserScope();
        StringBuilder hql = new StringBuilder();
        hql.append(" select count(1) from InformationFlow as f");
        hql.append(" where 1=1 ");
        hql.append(" and f.isDelete=0 ");
        hql.append(" and type=1");
        hql.append(" and f.id > ?1 ");
        hql.append(" and f.addressId=?2");
        hql.append(" and f.themeId is null ");
        if(currentUserScope.equals(ThemeConstant.PUBLIC)){
            hql.append(" and f.scope = 0 ");
        }
        Query query = em.createQuery(hql.toString());
        query.setParameter(1, oldId);
        query.setParameter(2, currentUser.getAddressId());
        Long count = (Long) query.getSingleResult();
        return count;
    }

    @Override
    public BasePage<InformationFlowDetailForwardWrap, Long> findForwardList(BasePage<InformationFlow, Long> page) {
        List<InformationFlowDetailForwardWrap> wraps = new ArrayList<>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select f from InformationFlow as f ");
        hql.append(" where 1=1 ");
        hql.append(" and isDelete = 0");
        hql.append(" and type=1");
        hql.append(" and (f.forwardHierarchy like ?1 or f.forwardHierarchy = ?2)");
        if(page.getId() != null && page.getId() > 0) {
            hql.append(" and f.id < ?3 ");
        }
        hql.append(" order by f.upTime desc ");
        Query query = em.createQuery(hql.toString());
        query.setParameter(1, page.getFilters().getForwardHierarchy() + "-" + "%");
        query.setParameter(2, page.getFilters().getForwardHierarchy());
        if(page.getId() != null && page.getId() > 0) {
            query.setParameter(3, page.getId());
        }
        query.setMaxResults(page.getSize());
        List<InformationFlow> list = query.getResultList();
        for(InformationFlow flow : list){
            InformationFlowDetailForwardWrap wrap = new InformationFlowDetailForwardWrap();
            User forwardUser = userService.findById(flow.getCreateId());
            wrap.setInformationFlowId(flow.getId());
            wrap.setContent(flow.getContent());
            wrap.setForwardUserId(flow.getCreateId());
            wrap.setForwardNickName(forwardUser.getNickname());
            wrap.setForwardAvatar(forwardUser.getAvatar());
            wraps.add(wrap);
        }
        return new BasePage<InformationFlowDetailForwardWrap, Long>(page.getId(), page.getSize(), wraps.size(), wraps);
    }

    @CacheEvict(value = "cache-information", key = "'id'+#id")
    @Override
    public void updateLikeNum(Long id, Long num) {
        this.baseRepository.updateLikeNum(id, num);
    }

    @CacheEvict(value = "cache-information", key = "'id'+#id")
    @Override
    public void updateCommitNum(Long id, Long num) {
        this.baseRepository.updateCommitNum(id, num);
    }

    @CacheEvict(value = "cache-information", key = "'id'+#id")
    @Override
    public void updateForwardNum(Long id, Long num) {
        this.baseRepository.updateForwardNum(id, num);
    }

    @Override
    public BasePage<InformationFlowWrap, Long> findPage(BasePage<InformationFlow, Long> page) {
        List<InformationFlowWrap> result = new ArrayList<>();
        accountHelp.checkCurrentAccountStatusOfRegist();
        User currentUser = accountHelp.getCurrentUser();
        Integer currentUserScope = accountHelp.getCurrentUserScope();
        List<InformationFlow> flows = null;
        StringBuilder hql = new StringBuilder();
        hql.append(" from InformationFlow");
        hql.append(" where 1=1 ");
        hql.append(" and isDelete=0 ");
        hql.append(" and type=1");
        if(page.getId() != null && page.getId() > 0) {
            hql.append(" and id < ?1 ");
        }
        hql.append(" and addressId=?2 ");
        if(page.getFilters() != null && page.getFilters().getThemeId() != null){
            hql.append(" and themeId=?3 ");
        }else if(currentUserScope.equals(ThemeConstant.ALL)){
            hql.append(" and themeId is null ");
        }else if(currentUserScope.equals(ThemeConstant.PUBLIC)){
            hql.append(" and themeId is null and scope = 0");
        }

        hql.append(" order by upTime desc");
        Query query = em.createQuery(hql.toString());
        if(page.getId() != null && page.getId() > 0) {
            query.setParameter(1, page.getId());
        }
        query.setParameter(2, currentUser.getAddressId());
        query.setMaxResults(page.getSize());
        if(page.getFilters() != null && page.getFilters().getThemeId() != null) {
            query.setParameter(3, page.getFilters().getThemeId());
        }
        flows = query.getResultList();
        for(InformationFlow flow : flows){
            if(StringUtils.isNotEmpty(flow.getForwardResult())){
                //同步昵称
                InformationHierarchy hierarchy = JSON.parseObject(flow.getForwardResult(), InformationHierarchy.class);
                hierarchy = syncInformationWrapInTheInformationHierarchy(hierarchy);
                flow.setForwardResult(JSON.toJSONString(hierarchy));
            }
            InformationFlowWrap wrap = new InformationFlowWrap();
            wrap.setInformationFlow(flow);
            wrap = syncInformationWrap(wrap, false);
            result.add(wrap);
        }
        return new BasePage<InformationFlowWrap, Long>(page.getId(), page.getSize(), result.size(), result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InformationFlow publishInformationFlow(InformationFlow informationFlow) {
        User currentUser = accountHelp.getCurrentUser();
        InformationFlow parentFlow = null;
        if(informationFlow.getThemeId() == null){
            accountHelp.checkCurrentAccountStatusOfEnable();
        }else{
            accountHelp.checkCurrentAccountStatusOfRegist();
            //查找是否能发这个圈子
            RelUserAndTheme relUserAndTheme = relUserAndThemeService.findRelByUserIdAndThemeId(currentUser.getUid(), informationFlow.getThemeId());
            if(relUserAndTheme == null){
                throw new BizException(ErrorCodeConstant.INFORMATION_THEME_JOIN_ERROR, new Throwable());
            }
        }
        InformationFlow dbFlow = new InformationFlow();
        if(StringUtils.isNotEmpty(informationFlow.getForwardHierarchy())) {
            parentFlow = this.findById(Long.parseLong(informationFlow.getForwardHierarchy()));
            if (StringUtils.isEmpty(informationFlow.getContent())) {
                dbFlow.setContent(InformationConstant.INFORMATION_FORWARD);
            } else {
                dbFlow.setContent(informationFlow.getContent());
            }
            dbFlow.setScope(parentFlow.getScope());
            ((IInformationFlowService) AopContext.currentProxy()).updateForwardNum(Long.parseLong(informationFlow.getForwardHierarchy()), 1L);
        }else if(informationFlow.getThemeId() == null){
            dbFlow.setScope(informationFlow.getScope());
            dbFlow.setContent(informationFlow.getContent());
        }else{
            dbFlow.setContent(informationFlow.getContent());
            dbFlow.setScope(0);
        }
        dbFlow.setResourceUrls(informationFlow.getResourceUrls());
        //默认可以
        dbFlow.setIsComment(true);
        dbFlow.setIsForward(true);
        dbFlow.setIsLike(true);
        dbFlow.setThemeId(informationFlow.getThemeId());
        //类型为说说
        dbFlow.setLikeNum(0L);
        dbFlow.setCommentNum(0L);
        dbFlow.setForwardNum(0L);
        dbFlow.setType(Byte.parseByte("1"));
        dbFlow.setIsDelete(false);
        dbFlow.setAddressId(currentUser.getAddressId());
        dbFlow.setCreateId(currentUser.getUid());
        if(StringUtils.isNotEmpty(informationFlow.getForwardHierarchy())) {
            InformationHierarchy hierarchy = new InformationHierarchy();
            InformationFlowWrap wrap = new InformationFlowWrap();
            dbFlow.setForwardHierarchy((parentFlow.getId() + (StringUtils.isEmpty(parentFlow.getForwardHierarchy()) ? "" :  "-" + parentFlow.getForwardHierarchy())));
            wrap.setInformationFlow(parentFlow);
            hierarchy.setInformationFlowWrap(wrap);
            hierarchy = wrapInformationHierarchy(hierarchy);
            dbFlow.setForwardResult(JSON.toJSONString(hierarchy));
        }
        InformationFlow saveFlow = this.save(dbFlow);
        //转发的加入通知
        if(StringUtils.isNotEmpty(informationFlow.getForwardHierarchy())) {
            Notification notification = new Notification();
            notification.setNotifiUserId(parentFlow.getCreateId());
            notification.setBizId(saveFlow.getId());
            notification.setFlowId(saveFlow.getId());
            notification.setStatus(NotificationConstant.UN_READ);
            notification.setType(NotificationConstant.TYPE_FORWARD);
            notification.setTitle(currentUser.getNickname() + NotificationConstant.FORWARD_CONTENT);
            notification.setBody(informationFlow.getContent());
            notificationService.pushNotification(notification);
        }
        return saveFlow;
    }

    private InformationHierarchy syncInformationWrapInTheInformationHierarchy(InformationHierarchy hierarchy){
        InformationFlowWrap wrap = hierarchy.getInformationFlowWrap();
        wrap = syncInformationWrap(wrap, true);
        hierarchy.setInformationFlowWrap(wrap);
        if(hierarchy.getParentInformationFlow() != null){
            syncInformationWrapInTheInformationHierarchy(hierarchy.getParentInformationFlow());
        }
        return hierarchy;
    }

    private InformationFlowWrap syncInformationWrap(InformationFlowWrap wrap, Boolean... lazy) {
        User currentUser = accountHelp.getCurrentUser();
        User user = userService.findById(wrap.getInformationFlow().getCreateId());
        wrap.setCreateUserNickName(user.getNickname());
        wrap.setCreateUserAvatar(user.getAvatar());
        wrap.setAddressName(addressService.findById(wrap.getInformationFlow().getAddressId()).getName());
        if(wrap.getInformationFlow().getThemeId() != null) {
            wrap.setThemeName(themeService.findById(wrap.getInformationFlow().getThemeId()).getTitle());
        }else{
            wrap.setThemeName("");
        }
        if(lazy.length == 0 || !lazy[0]) {
            InformationFlowLike like = likeService.findByinformationFlowIdAndCreateId(wrap.getInformationFlow().getId(), currentUser.getUid());
            wrap.setLikeId(like == null ? null : like.getIsDelete() ? null : like.getId());
        }
        return wrap;
    }

    private InformationHierarchy wrapInformationHierarchy(InformationHierarchy hierarchy) {
        InformationFlowWrap informationFlowWrap = hierarchy.getInformationFlowWrap();
        InformationFlow informationFlow = informationFlowWrap.getInformationFlow();
        String forwardHierarchy = informationFlow.getForwardHierarchy();
        informationFlowWrap = syncInformationWrap(informationFlowWrap, true);
        hierarchy.setInformationFlowWrap(informationFlowWrap);
        //转发的信息流
        if(StringUtils.isNotEmpty(forwardHierarchy)) {
            InformationFlowWrap wrap = new InformationFlowWrap();
            List<String> informationIds = Arrays.asList(forwardHierarchy.split("-"));
            String informationId = informationIds.get(0);
            InformationFlow parentFlow = this.findById(Long.parseLong(informationId));
            InformationHierarchy parentHierarchy = new InformationHierarchy();
            //转发的微博是否已被删除
            if(parentFlow.getIsDelete()){
                parentFlow.setResourceUrls(null);
                parentFlow.setContent(InformationConstant.INFORMATION_DELETE);
            }
            wrap.setInformationFlow(parentFlow);
            parentHierarchy.setInformationFlowWrap(wrap);
            hierarchy.setParentInformationFlow(parentHierarchy);
            wrapInformationHierarchy(parentHierarchy);
        }
        return hierarchy;
    }

}
