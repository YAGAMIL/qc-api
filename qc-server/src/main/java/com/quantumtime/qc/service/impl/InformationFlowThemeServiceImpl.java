package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.exception.JpaCrudException;
import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.common.service.impl.BaseServiceImpl;
import com.quantumtime.qc.entity.information.InformationFlowTheme;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.repository.InformationFlowThemeRepository;
import com.quantumtime.qc.service.IInformationFlowThemeService;
import com.quantumtime.qc.service.IRelUserAndThemeService;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.wrap.information.InformcationFlowThemeWrap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class InformationFlowThemeServiceImpl extends BaseServiceImpl<InformationFlowTheme, Long, InformationFlowThemeRepository> implements IInformationFlowThemeService {

    @Autowired
    private IUserService userService;

    @Autowired
    private AccountHelp accountHelp;

    //用于增删改查的接口
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private IRelUserAndThemeService relUserAndThemeService;

    @Cacheable(value = "cache-theme", key = "'theme'+#id", unless="#result == null")
    @Override
    public InformationFlowTheme findById(Long id) {
        return super.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InformationFlowTheme save(InformationFlowTheme entity) throws JpaCrudException {
        return super.save(entity);
    }

    @Override
    public BasePage<InformcationFlowThemeWrap, Long> findThemeList(BasePage<InformationFlowTheme, Long> page) {
        accountHelp.checkCurrentAccountStatusOfRegist();
        User currentUser = accountHelp.getCurrentUser();
        StringBuilder hql = new StringBuilder();
        hql.append(" select t, case when  r.id is null then false else true end as isJoin from InformationFlowTheme as t left join RelUserAndTheme as r on t.id = r.themeId AND r.userId = ?1");
        hql.append(" where 1=1 ");
        hql.append(" and t.addressId=?2 ");
        if(page.getFilters() != null && StringUtils.isNotEmpty(page.getFilters().getTitle())){
            hql.append(" and t.title like ?3 ");
        }
        if(page.getId() != null){
            hql.append(" and t.id> ?4");
        }
        Query query = em.createQuery(hql.toString());
        query.setParameter(1, currentUser.getUid());
        query.setParameter(2, currentUser.getAddressId());
        if(page.getFilters() != null && StringUtils.isNotEmpty(page.getFilters().getTitle())) {
            query.setParameter(3, "%"+page.getFilters().getTitle()+"%");
        }
        if(page.getId() != null){
            query.setParameter(4, page.getId());
        }
        query.setMaxResults(page.getSize());
        List<Object[]> themeList = query.getResultList();
        List<InformcationFlowThemeWrap> wrapList = new ArrayList<>();
        for(Object[] o : themeList){
            InformcationFlowThemeWrap wrap = new InformcationFlowThemeWrap();
            wrap.setInformationFlowTheme((InformationFlowTheme) o[0]);
            wrap.setIsJoin((Boolean) o[1]);
            wrap.setCreateNickName(userService.findById(wrap.getInformationFlowTheme().getCreateId()).getNickname());
            wrapList.add(wrap);
        }
        return new BasePage<InformcationFlowThemeWrap, Long>(page.getId(), page.getSize(), wrapList.size(), wrapList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean initTheme(Long addressId) {
        //初始化呢theme
        List<InformationFlowTheme> themes = this.baseRepository.findAllByAddressId(0L);
        for(InformationFlowTheme theme : themes){
            InformationFlowTheme dbTheme = new InformationFlowTheme();
            BeanUtils.copyProperties(theme, dbTheme);
            dbTheme.setId(null);
            dbTheme.setAddressId(addressId);
            this.save(dbTheme);
        }
        return true;
    }

    @Override
    public Boolean joinTheme(Long themeId) {
        return relUserAndThemeService.joinTheme(themeId);
    }

    @Override
    public Boolean unjoinTheme(Long themeId) {
        return relUserAndThemeService.unjoinTheme(themeId);
    }


}
