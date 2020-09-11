package com.quantumtime.qc.service;

import com.quantumtime.qc.entity.ClickContent;
import com.quantumtime.qc.vo.ClickContentVo;
import com.quantumtime.qc.vo.RobotClickVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ClickContentService {

    int LIKE_FACTOR = 5;

    int EXTRA = 10;

    int FINAL_MULTIPLES = 10;

    int MIS_MULTIPLE = 19;

    int V_L_SCALE = 10;

    int HANDLE_LIMIT = 10;

    Map<String, Integer> getClickMap(List<String> videoIds, boolean isLike);

    List<ClickContent> getRecords(Integer clickType, List<Long> contentIds, Integer contentType, String createUid);
    /** 我的點贊 */
    List<ClickContentVo> myRecords(Integer pageNumber, Integer pageSize);
    /** 马甲号随即点赞 */

    /**
     * Author: Tablo
     *
     * <p>Description:[获取随机马甲用户] Created on 14:46 2019/11/30
     *
     * @param viewSum 查看数
     * @param contentId 内容Id
     * @return java.util.List<java.lang.String>
     */
    List<String> randomUid(int viewSum, String contentId);

    RobotClickVo robotClick(int handleLimit);

    List<ClickContent> robotClickOne(List<ClickContent> result, String videoId, int handleLimit);

    Boolean removeRobot();

    /**
     * Author: Tablo
     *
     * <p>Description:[] Created on 20:14 2019/12/02
     *
     * @param result 需要执行的最终实体
     */
    void batchSave(List<ClickContent> result);
}
