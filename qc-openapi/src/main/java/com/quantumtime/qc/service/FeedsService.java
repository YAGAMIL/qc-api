package com.quantumtime.qc.service;

import com.quantumtime.qc.common.service.IBaseService;

import com.quantumtime.qc.entity.feeds.Feeds;
import com.quantumtime.qc.vo.FeedsTrendsVo;
import com.quantumtime.qc.vo.MyFeedsSumVo;
import com.quantumtime.qc.vo.TrendsDetailedVo;


import java.util.List;


public interface FeedsService extends IBaseService<Feeds, Long> {


    /**
     * 发布动态
     *
     * @param feeds
     * @return
     */
    Feeds addTrends(Feeds feeds);

    /**
     * 删除动return态(更改delete_flag状态值
     *
     * @param feedsId ，num
     * @
     */
    boolean updateIsDelete(Long feedsId);

    /**
     * 根据userid，返回该用户的最近100条动态
     *
     * @param id，num
     * @return
     */
    FeedsTrendsVo trends(String id);

    /**
     * 动态详情接口
     *
     * @param feeds_id
     * @return
     */
    TrendsDetailedVo trendsDetailed(Long feeds_id);

    /**
     * 根据userid，返回该用户的最近100条动态
     *
     * @param ，num
     * @return
     */
    /*  List trends(long id);*/

    List<Feeds> findAllByIds(List<Long> ids);

    /**
     * 我的动态
     *
     * @param
     * @return
     */

    MyFeedsSumVo myTrends(Integer pageNumber, Integer pageSize);

    /**
     * 动态的签发和废止
     *
     * @param
     * @return
     */
    Boolean RepealFeeds(Feeds feeds);


}
