package com.quantumtime.qc.common.duiba;

import com.quantumtime.qc.common.duiba.entity.CreditAuditParams;
import com.quantumtime.qc.common.duiba.entity.CreditConfirmParams;
import com.quantumtime.qc.common.duiba.entity.ExpressInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.quantumtime.qc.common.DuiBaProperties.APP_KEY;
import static com.quantumtime.qc.common.DuiBaProperties.APP_SECRET;

/**
 * Description: 兑吧URL构建工具 Created on 2019/12/09 11:02
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public  class BulidUrl {

    /**
     * 构建在兑吧商城自动登录的url地址
     *
     * @param uid 用户id
     * @param redirect 免登陆接口回传回来 DbRedirect参数
     * @param credits 用户积分余额
     * @return 自动登录的url地址
     */
    public static String buildAutoLoginRequest(String uid, Long credits, String redirect) {
        CreditTool tool = new CreditTool(APP_KEY, APP_SECRET);
        String url = "https://www.duiba.com.cn/autoLogin/autologin?";
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("credits", credits + "");
        if (redirect != null) {
            params.put("redirect", "redirect");
        }
        return tool.buildUrlWithSign(url, params);
    }

    /**
     * 构建向兑吧查询兑换订单状态的url地址
     *
     * @param orderNum 兑吧的订单号
     * @return
     */
    public static String buildCreditOrderStatusRequest(String orderNum, String bizId) {
        CreditTool tool = new CreditTool(APP_KEY, APP_SECRET);
        if (orderNum == null) {
            orderNum = "";
        }
        if (bizId == null) {
            bizId = "";
        }
        String url = "http://www.duiba.com.cn/status/orderStatus?";
        Map<String, String> params = new HashMap<>(2);
        params.put("orderNum", orderNum);
        params.put("bizId", bizId);
        return tool.buildUrlWithSign(url, params);
    }
    /**
     * 构建开发者审核结果的的方法
     *
     * @param params 审核参数
     * @return 发起请求的url
     */
    public static String buildCreditAuditRequest(CreditAuditParams params) {
        CreditTool tool = new CreditTool(APP_KEY, APP_SECRET);
        String url = "http://www.duiba.com.cn/audit/apiAudit?";
        Map<String, String> signParams = new HashMap<String, String>();
        if (params.getPassOrderNums() != null && !params.getPassOrderNums().isEmpty()) {
            StringBuilder s = null;
            for (String o : params.getPassOrderNums()) {
                if (s == null) {
                    s = new StringBuilder(o);
                } else {
                    s.append(",").append(o);
                }
            }
            signParams.put("passOrderNums", Objects.requireNonNull(s).toString());
        } else {
            signParams.put("passOrderNums", "");
        }
        if (params.getRejectOrderNums() != null && !params.getRejectOrderNums().isEmpty()) {
            StringBuilder s = null;
            for (String o : params.getRejectOrderNums()) {
                if (s == null) {
                    s = new StringBuilder(o);
                } else {
                    s.append(",").append(o);
                }
            }
            signParams.put("rejectOrderNums", Objects.requireNonNull(s).toString());
        } else {
            signParams.put("rejectOrderNums", "");
        }
        return tool.buildUrlWithSign(url, signParams);
    }
    /**
     * 构建开发者向兑吧发起兑换成功失败的确认通知请求
     *
     * @param p 开发者端兑换请求
     * @return URL
     */
    public static String buildCreditConfirmRequest(CreditConfirmParams p) {
        CreditTool tool = new CreditTool(APP_KEY, APP_SECRET);
        String url = "http://www.duiba.com.cn/confirm/confirm?";
        Map<String, String> params = new HashMap<String, String>();
        params.put("success", p.isSuccess() + "");
        params.put("errorMessage", p.getErrorMessage());
        params.put("orderNum", p.getOrderNum());
        return tool.buildUrlWithSign(url, params);
    }

    /**
     * 前置商品查询URL
     *
     * @param count 查询返回的商品数量，最大支持50个
     * @return
     */
    public static String queryForFrontItem(String count) {
        CreditTool tool = new CreditTool(APP_KEY, APP_SECRET);
        Map<String, String> params = new HashMap<String, String>();
        params.put("count", count);
        return tool.buildUrlWithSign("http://www.duiba.com.cn/queryForFrontItem/query?", params);
    }

    /**
     * 构建向兑吧请求增加活动次数的url地址
     *
     * @param uid ：用户id activityId:活动id, times:增加活动次数, bizId：本次请求开发者订单号，保证唯一性
     * @return URL
     */
    public static String getActivityTimes(String uid, String activityId, String times, String bizId) {
        CreditTool tool = new CreditTool(APP_KEY, APP_SECRET);
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("bizId", bizId);
        params.put("activityId", activityId);
        params.put("times", times);
        return tool.buildUrlWithSign("https://activity.m.duiba.com.cn/activityVist/addTimes?", params);
    }

    /**
     * 自有商品批量取消发货
     *
     * @param orderNums 最大支持100个 方法中，对于超过100的会自动截取前100个
     * @return URL
     */
    public static String batchCancel(List<String> orderNums) {
        CreditTool tool = new CreditTool(APP_KEY, APP_SECRET);
        Map<String, String> params = new HashMap<String, String>(16);
        if (orderNums.size() > 100) {
            orderNums = orderNums.subList(0, 100);
        }
        params.put("orderNums", orderNums.toString().substring(1, orderNums.toString().length() - 1));
        return tool.buildUrlWithSign("http://www.duiba.com.cn/sendObject/batchCancel?", params);
    }

    /**
     * 自有商品批量发货
     *
     * @param infos 格式如下 发货的数量，每次请求不超过100个 方法中，对于超过100的会自动截取前100个
     * @return URL
     */
    public static String batchSend(List<ExpressInfo> infos) {
        CreditTool tool = new CreditTool(APP_KEY, APP_SECRET);
        Map<String, String> params = new HashMap<>(16);
        StringBuilder expressInfo = new StringBuilder();
        if (infos.size() > 100) {
            infos = infos.subList(0, 100);
        }
        infos.forEach(expressInfo::append);
        expressInfo.deleteCharAt(expressInfo.length() - 1);
        params.put("expressInfo", expressInfo.toString());
        return tool.buildUrlWithSign("http://www.duiba.com.cn/sendObject/batchSend?", params);
    }
}
