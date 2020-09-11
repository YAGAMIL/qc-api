package com.quantumtime.qc.service.impl;

import com.alibaba.fastjson.JSON;
import com.quantumtime.qc.common.exception.BizException;
import com.quantumtime.qc.entity.*;
import com.quantumtime.qc.entity.feeds.Article;
import com.quantumtime.qc.entity.feeds.Feeds;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.service.*;
import com.quantumtime.qc.utils.PoiUtil;
import com.quantumtime.qc.vo.recommend.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class RecommendServiceImpl implements IRecommendService {
    private static int CLICK_TYPE_LIKE = 0;
    private static int CLICK_TYPE_VIEW = 1;

    private static int CONTENT_TYPE_ARTICLE = 0;
    private static int CONTENT_TYPE_FEEDS = 1;

    @Value("${recommend.url}")
    private String url;

    @Autowired
    private AccountHelp accountHelp;

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private IAddressService addressService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private FeedsService feedsService;

    @Autowired
    private IBusinessService businessService;

    @Autowired
    private ClickContentService clickRecordService;

    @Override
    public SquareResponse getSquareResponse(int pageNum, int pageSize) {
        User currentUser = accountHelp.getCurrentUser();
        Assert.notNull(currentUser, "无法获取当前用户");
        Long addressId = currentUser.getAddressId();
        Assert.notNull(addressId, "该用户没有地址信息");
        Address address = addressService.findAddressById(addressId);
        Assert.notNull(address, "该用户没有地址信息");
        if (log.isDebugEnabled()) {
            log.debug("Square request user:" + currentUser.getUid() + " address:" + address.getPoiName());
        }
        address.makeProvinceCode();
        GlobalRequest globalRequest = createGlobalRequest(pageNum, pageSize, currentUser, address);
        GlobalResponse globalResponse = getRecommend(globalRequest);
        if (globalResponse.getCode() != 0) {
            log.error("访问Recommend异常,code="
                    + globalResponse.getCode() + ",message=" + globalResponse.getMessage());
            throw new BizException("访问Recommend异常,code="
                    + globalResponse.getCode() + ",message=" + globalResponse.getMessage(), new Throwable());
        }
        return createSquareResponse(globalResponse, currentUser);
    }

    private SquareResponse createSquareResponse(GlobalResponse globalResponse, User currentUser) {
        SquareResponse squareResponse = new SquareResponse();
        squareResponse.setCount(globalResponse.getData().getCount());
        squareResponse.setCurrentCount(globalResponse.getData().getCurrentCount());
        squareResponse.setPageNum(globalResponse.getData().getPageNum());
        squareResponse.setPageSize(globalResponse.getData().getPageSize());
        if (globalResponse.getData().getCount() == 0) {
            return squareResponse;
        }
        List<Recommend> recommendList = globalResponse.getData().getRecommendList();
        //批量查询用户信息
        Map<String, User> userMap = getUserInfo(recommendList);
        //批量查询文章信息
        Map<Long, Article> articleMap = getArticles(recommendList);
        //批量查询动态信息
        Map<Long, Feeds> feedsMap = getFeeds(recommendList);
        //批量查询Address信息
        Map<Long, Address> addressMap = getAddresses(articleMap, feedsMap);
        //批量查询当前用户点赞的文章
        Map<Long, Article> likeArticleMap = getLikeArticles(articleMap, currentUser);
        //批量查询当前用户点赞的动态
        Map<Long, Feeds> likeFeedsMap = getLikeFeeds(feedsMap, currentUser);

        List<FeedSummary> summaryList = new ArrayList<>(recommendList.size());
        for (Recommend recommend : recommendList) {
            if ("article".equals(recommend.getFeedsType())) {
               String userId = recommend.getAuthorId();
               Long articleId = recommend.getFeedsId();
               User user = userMap.get(userId);
               Article article = articleMap.get(articleId);
               //防止recommend和article不一致
               if (article == null || user == null) {
                   continue;
               }
               ArticleFeed articleFeed = new ArticleFeed();
               articleFeed.setArticleId(article.getArticleId());
               articleFeed.setCoverUrl(article.getCoverImg());
               articleFeed.setTitle(article.getTitle());
               articleFeed.setCreateTime(article.getCreateTime());
               Address address = addressMap.get(article.getAddressId());
               if (address != null) {
                   articleFeed.setCity(address.getCity());
                   articleFeed.setCommunity(address.getPoiName());
               }
               articleFeed.setAvatar(user.getAvatar());
               articleFeed.setCharacterUrl(user.getCharacterUrl());
               articleFeed.setFeedType("article");
               articleFeed.setNickname(user.getNickname());
               articleFeed.setLike(recommend.getClickInfo().getLike());
               articleFeed.setView(recommend.getClickInfo().getView());
               if (likeArticleMap.get(articleId) != null) {
                   articleFeed.setUserLike(true);
               } else {
                   articleFeed.setUserLike(false);
               }
               summaryList.add(articleFeed);
            } else {
                String userId = recommend.getAuthorId();
                Long feedsId = recommend.getFeedsId();
                User user = userMap.get(userId);
                Feeds feeds = feedsMap.get(feedsId);
                //防止recommend和feeds不一致
                if (feeds == null || user == null) {
                    continue;
                }
                TrendFeed trendFeed = new TrendFeed();
                trendFeed.setTrendId(feeds.getFeeds_id());
                trendFeed.setPics(feeds.getPicture());
                trendFeed.setSummary(feeds.getContent());
                trendFeed.setCreateTime(feeds.getCreateTime());
                Address address = addressMap.get(feeds.getAddress_id());
                if (address != null) {
                    trendFeed.setCity(address.getCity());
                    trendFeed.setCommunity(address.getPoiName());
                }
                trendFeed.setAvatar(user.getAvatar());
                trendFeed.setCharacterUrl(user.getCharacterUrl());
                trendFeed.setFeedType("feeds");
                trendFeed.setNickname(user.getNickname());
                trendFeed.setLike(recommend.getClickInfo().getLike());
                trendFeed.setView(recommend.getClickInfo().getView());
                if (likeFeedsMap.get(feedsId) != null) {
                    trendFeed.setUserLike(true);
                } else {
                    trendFeed.setUserLike(false);
                }
                summaryList.add(trendFeed);
            }
        }
        squareResponse.setFeedsList(summaryList);
        if (log.isDebugEnabled()) {
            log.debug("square response:" + JSON.toJSONString(squareResponse));
        }
        return squareResponse;
    }

    private Map<Long, Feeds> getLikeFeeds(Map<Long, Feeds> feedsMap, User currentUser) {
        Map<Long, Feeds> likeFeedsMap = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        for (Feeds feeds : feedsMap.values()) {
            ids.add(feeds.getFeeds_id());
        }
        if (ids.size() == 0) {
            return likeFeedsMap;
        }
        List<ClickContent> list = clickRecordService.getRecords(CLICK_TYPE_LIKE, ids, CONTENT_TYPE_FEEDS, currentUser.getUid());
        if (list == null || list.size() == 0) {
            return likeFeedsMap;
        }
        for (ClickContent clickContent : list) {
            Feeds feeds = feedsMap.get(clickContent.getContentId());
            if (feeds != null) {
                likeFeedsMap.put(feeds.getFeeds_id(), feeds);
            }
        }
        return likeFeedsMap;
    }

    private Map<Long,Article> getLikeArticles(Map<Long, Article> articleMap, User currentUser) {
        Map<Long, Article> likeArticleMap = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        for (Article article : articleMap.values()) {
            ids.add(article.getArticleId());
        }
        if (ids.size() == 0) {
            return likeArticleMap;
        }
        List<ClickContent> list = clickRecordService.getRecords(CLICK_TYPE_LIKE, ids, CONTENT_TYPE_ARTICLE, currentUser.getUid());
        for (ClickContent clickContent : list) {
            Article article = articleMap.get(clickContent.getContentId());
            if (article != null) {
                likeArticleMap.put(article.getArticleId(), article);
            }
        }
        return likeArticleMap;
    }

    private Map<Long, Address> getAddresses(Map<Long, Article> articleMap,
                                            Map<Long, Feeds> feedsMap) {
        Map<Long, Address> addressMap = new HashMap<>();
        Set<Long> ids = new HashSet<>();
        for (Article article : articleMap.values()) {
            ids.add(article.getAddressId());
        }
        for (Feeds feeds : feedsMap.values()) {
            ids.add(feeds.getAddress_id());
        }
        List<Address> addressList = addressService.findAllByIds(ids);
        for (Address address : addressList) {
            addressMap.put(address.getId(), address);
        }
        return addressMap;
    }

    private Map<Long,Feeds> getFeeds(List<Recommend> recommendList) {
        Map<Long, Feeds> feedsMap = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        for (Recommend recommend : recommendList) {
            if ("feeds".equals(recommend.getFeedsType())) {
                ids.add(recommend.getFeedsId());
            }
        }
        List<Feeds> feedsList = feedsService.findAllByIds(ids);
        for (Feeds feeds : feedsList) {
            feedsMap.put(feeds.getFeeds_id(), feeds);
        }
        return feedsMap;
    }

    private Map<Long,Article> getArticles(List<Recommend> recommendList) {
        Map<Long, Article> articleMap = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        for (Recommend recommend : recommendList) {
            if ("article".equals(recommend.getFeedsType())) {
                ids.add(recommend.getFeedsId());
            }
        }
        List<Article> articleList = articleService.findAllByIds(ids);
        for (Article article : articleList) {
            articleMap.put(article.getArticleId(), article);
        }
        return articleMap;
    }

    private Map<String, User> getUserInfo(List<Recommend> recommendList) {
        Map<String, User> userMap = new HashMap<>();
        Set<String> ids = new HashSet<>();
        for (Recommend recommend : recommendList) {
            ids.add(recommend.getAuthorId());
        }
        List<User> userList = userService.findAllByIds(ids);
        for (User user : userList) {
            userMap.put(user.getUid(), user);
        }
        return userMap;
    }

    private GlobalResponse getRecommend(GlobalRequest globalRequest) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            httpPost.addHeader("Content-Type", "application/json");
            String reqJson = JSON.toJSONString(globalRequest);
            httpPost.setEntity(new StringEntity(reqJson, "UTF-8"));
            if (log.isDebugEnabled()) {
                log.debug("square recommend request:" + reqJson);
            }
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = response.getEntity();
                String content = EntityUtils.toString(httpEntity, "UTF-8");
                if (log.isDebugEnabled()) {
                    log.debug("square recommend response:" + content);
                }
                return JSON.parseObject(content, GlobalResponse.class);
            }
            throw new Exception("status code:"
                    + response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            log.error("访问Recommend异常:", e);
            throw new BizException("访问Recommend异常", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error(" error:", e);
                }
            }
        }
    }

    private GlobalRequest createGlobalRequest(int pageNum, int pageSize,
                                              User currentUser, Address address) {
        //构造rank请求
        GlobalRequest globalRequest = new GlobalRequest();
        globalRequest.setPageNum(pageNum);
        globalRequest.setPageSize(pageSize);
        globalRequest.setUserId(currentUser.getUid());
        globalRequest.setPoiList(PoiUtil.createPoiList(address, businessService));
        return globalRequest;
    }
}
