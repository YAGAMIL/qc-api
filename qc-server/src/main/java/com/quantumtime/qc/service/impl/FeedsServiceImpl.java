package com.quantumtime.qc.service.impl;

import com.alibaba.fastjson.JSON;
import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.exception.BizException;
import com.quantumtime.qc.common.exception.JpaCrudException;
import com.quantumtime.qc.common.service.impl.BaseServiceImpl;
import com.quantumtime.qc.entity.*;
import com.quantumtime.qc.entity.feeds.Feeds;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.repository.ClickContentRepository;
import com.quantumtime.qc.repository.FeedsRepository;
import com.quantumtime.qc.service.*;
import com.quantumtime.qc.utils.PoiUtil;
import com.quantumtime.qc.vo.*;
import com.quantumtime.qc.vo.wechat.FeedVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FeedsServiceImpl extends BaseServiceImpl<Feeds, Long, FeedsRepository> implements FeedsService {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static {
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
    }

    @PersistenceContext
    private EntityManager em;//用于增删改查的接口
    @Autowired
    private FeedsService feedsService;
    @Autowired
    IAddressService iAddressService;
    @Autowired
    IArticleService iArticleService;
    @Autowired
    private ISensorAnalytics SensorAnalyticsi;
    @Autowired
    IBusinessService iBusinessService;
    @Autowired
    FeedsRepository feedsRepository;
    @Autowired
    IUserService userService;
    @Autowired
    IMQService mqService;
    @Autowired
    private AccountHelp accountHelp;
    @Autowired
    private ClickContentRepository clickContentRepository;
    @Autowired
    private ClickContentService clickContentService;
    private int DELETED_STATUS = 1;
    private int REVIEW_STATUS = 0;
    private static int CONTENT_TYPE_ARTICLE = 0;
    private static int CONTENT_TYPE_FEEDS = 1;
    private static int CLICK_TYPE_LIKE = 0;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Feeds addTrends(Feeds feeds) throws BizException {
        User user = accountHelp.getCurrentUser();
        Address address = iAddressService.findAddressById(user.getAddressId());
        if (user.getAuthState() != 2) {
            throw new BizException(ErrorCodeConstant.ACCOUNT_NOT_AUTH, new Throwable());
        }
        feeds.setDelete_flag(DELETED_STATUS);
        feeds.setReview_status(REVIEW_STATUS);
        feeds.setAddress_id(user.getAddressId());
        feeds.setCreate_uid(user.getUid());
        feeds.setCreate_name(user.getNickname());
        feeds.setFeeds_avatar(user.getAvatar());
        feeds.setAddress_name(address.getPoiName());
        feeds.setCity(address.getCity());
        feeds.setNikeName(user.getNickname());
        Feeds feedsmq= feedsService.save(feeds);
        log.info("save :  {}", feedsmq);
        if (address == null) {
            throw new BizException(ErrorCodeConstant.ADDRESS_NULL, new Throwable());
        }
        MqFeeds mqFeeds = new MqFeeds();
        mqFeeds.setPhone(user.getPhone());
        mqFeeds.setFeeds(feedsmq);
        mqFeeds.setPoi(PoiUtil.createPoiList(address, iBusinessService));
        if (!mqService.sendFeeds(JSON.toJSONString(mqFeeds))) {
            throw new BizException(ErrorCodeConstant.MQ_REQUEST_FAIL, new Throwable());
        }
        Map<String, Object> properties = new HashMap<>();
        properties.put("content_name", feeds.getContent());
        properties.put("content_id", feeds.getFeeds_id());
        properties.put("content_show_type", "FeedsTrends");
        properties.put("platform_type", "java_publishManuscript");
        SensorAnalyticsi.track(user.getUid(), true, "publishManuscript", properties);//埋点
        return feeds;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FeedsTrendsVo trends(String uid) throws JpaCrudException {
        log.info("DONGDONG :  {}", uid);
        User user = userService.findById(uid);
        if (user == null) {
            throw new BizException(ErrorCodeConstant.ACCOUNT_NOT_EXIST, new Throwable());
        }
        StringBuilder hql = new StringBuilder();
        hql.append("  from Feeds ");
        hql.append(" where 1=1 ");
        hql.append(" and delete_flag=1 ");
        hql.append(" and create_uid = ?1 ");
        hql.append(" order by createTime desc");
        Query query = em.createQuery(hql.toString());
        query.setParameter(1, uid);
        List<Feeds> list = query.getResultList();
        List lists = new ArrayList();
        for (Feeds fllows : list) {
            FeedVo feedVo = new FeedVo();
            Address address = iAddressService.findAddressById(fllows.getAddress_id());
            if (address == null) {
                continue;
            }
            feedVo.setFeeds_id(fllows.getFeeds_id());
            feedVo.setCreate_time(dateFormat.format(fllows.getCreateTime()));
            feedVo.setExt_param("");
            feedVo.setPois(PoiUtil.createPoiList(address, iBusinessService));
            lists.add(feedVo);
        }
        FeedsTrendsVo feedsTrendsVo = new FeedsTrendsVo();
        feedsTrendsVo.setAuthor_id(uid);
        feedsTrendsVo.setAuthor_name(user.getNickname());
        feedsTrendsVo.setThrends(lists);
        log.info("DONGDONG11 :  {}", feedsTrendsVo);
        return feedsTrendsVo;
    }

    @Override
    public boolean updateIsDelete(Long feedsId) {
        return save(findById(feedsId).setDelete_flag(0)).getDelete_flag() == 0;
    }

    @Override
    public TrendsDetailedVo trendsDetailed(Long feedsId) {
        TrendsDetailedVo trendsDetailedVo = new TrendsDetailedVo();
      User user=  accountHelp.getCurrentUser();
        if (user == null) {
            trendsDetailedVo.setUserLike(false);
        } else {
            ClickContent clickContent = clickContentRepository.feedsClickLike(feedsId, user.getUid());
            if (clickContent != null) {
                trendsDetailedVo.setUserLike(true);
                log.info("trendsDetailedVo :  {}", trendsDetailedVo);
            } else {
                trendsDetailedVo.setUserLike(false);
            }
        }
        Feeds feeds = feedsService.findById(feedsId);
        Integer like = clickContentRepository.clickContentSum(feedsId, 0);
        Integer view = clickContentRepository.clickContentSum(feedsId, 1);
        trendsDetailedVo.setView(view != null ? view : 0);
        trendsDetailedVo.setLike(like != null ? like : 0);
        BeanUtils.copyProperties(feeds,trendsDetailedVo);
        return trendsDetailedVo;
    }

    @Override
    public MyFeedsSumVo myTrends(Integer pageNumber, Integer pageSize) {
        MyFeedsSumVo myFeedsSumVo = new MyFeedsSumVo();
        Integer sizes = pageSize * (pageNumber - 1);
        String uid = accountHelp.getCurrentUser().getUid();
        List<Feeds> list = feedsRepository.myTrends(uid, sizes, pageSize);
        if(list.size()==0){
            return myFeedsSumVo;
        }
        List<Long> feedsIds = list.stream().map(Feeds::getFeeds_id).collect(Collectors.toList());
        List<ClickContent> contentList = clickContentRepository.findClickContentsByContentIdIsInAndContentType(feedsIds, 1);
        long like = contentList.stream().map(clickContent -> clickContent.getClickTypeId() == 0).count();
        long view = contentList.stream().map(clickContent -> clickContent.getClickTypeId() == 1).count();
        feedsIds.stream().forEach(feedsId -> contentList.stream().map(clickContent -> clickContent.getClickTypeId() == 0).count());
        //查询出当前用户点赞的动态
        Map<Long, Feeds> likeFeedsMap = getLikeFeeds(list, uid,feedsIds);
        List<MyFeedsVo> myFeedsVos = new ArrayList<>();
        list.stream().forEach(feeds ->{
            MyFeedsVo myFeedsVo = new MyFeedsVo();
           // BeanUtils.copyProperties(feeds,myFeedsVo);
            myFeedsVo.setFeedsId(feeds.getFeeds_id());
            myFeedsVo.setAddressName(feeds.getAddress_name());
            myFeedsVo.setCity(feeds.getCity());
            myFeedsVo.setCreateTime(feeds.getCreateTime());
            myFeedsVo.setPicture(feeds.getPicture());
            myFeedsVo.setContent(feeds.getContent());
            myFeedsVo.setFeedsAvatar(feeds.getFeeds_avatar());
            myFeedsVo.setNickName(feeds.getNikeName());
//            myFeedsVo.setLike(contentList.stream().filter(clickContent -> clickContent.getClickTypeId() == 0&&clickContent.getContentId()==feeds.getFeeds_id()).count());
//            myFeedsVo.setView(contentList.stream().filter(clickContent -> clickContent.getClickTypeId() == 1&&clickContent.getContentId()==feeds.getFeeds_id()).count());
            if (likeFeedsMap.get(feeds.getFeeds_id()) != null) {
                myFeedsVo.setUserLike(true);
            } else {
                myFeedsVo.setUserLike(false);
            }
            myFeedsVos.add(myFeedsVo);
        });
        int sum = feedsRepository.myFeedsSum(uid);
        myFeedsSumVo.setFeedsList(myFeedsVos);
        myFeedsSumVo.setSum(sum);
        return myFeedsSumVo;
    }

    @Override
    public List<Feeds> findAllByIds(List<Long> ids) {
        return baseRepository.findAllById(ids);
    }

    @Override
    public Boolean RepealFeeds(Feeds feeds) {
        save(findById(feeds.getFeeds_id()).setReview_status(feeds.getReview_status()));
        return true;
    }

    private Map<Long, Feeds> getLikeFeeds(List<Feeds> feedsList, String uid,List<Long> ids) {
        Map<Long, Feeds> likeFeedsMap = new HashMap<>();
        List<ClickContent> list = clickContentService.getRecords(CLICK_TYPE_LIKE, ids, CONTENT_TYPE_FEEDS, uid);
        if (list == null || list.size() == 0) {
            return likeFeedsMap;
        }
        for (ClickContent clickContent : list) {
             for (Feeds feeds : feedsList) {
                 if (feeds.getFeeds_id().equals(clickContent.getContentId())) {
                     likeFeedsMap.put(feeds.getFeeds_id(), feeds);
                     break;
                 }
             }
        }
        return likeFeedsMap;
    }

}