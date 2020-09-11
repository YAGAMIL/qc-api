package com.quantumtime.qc.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.aliyuncs.utils.ParameterHelper;
import com.quantumtime.qc.entity.undo.Notification;
import com.quantumtime.qc.service.AliPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class AliPushServiceImpl implements AliPushService {

  @Value("${aliyun.accessKey}")
  private String accessKeyId;

  @Value("${aliyun.secret}")
  private String secret;

  @Value("${aliyun.pushIOSAppKey}")
  private String pushIOSAppkey;

  @Value("${aliyun.pushAndroidAppKey}")
  private String pushAndroidAppkey;

  @Override
  @Async
  public void push(Notification notification) {
    IClientProfile profile = DefaultProfile.getProfile("cn-beijing", accessKeyId, secret);
    DefaultAcsClient client = new DefaultAcsClient(profile);
    PushRequest pushRequest = new PushRequest();

    pushRequest.setAndroidPopupTitle(notification.getTitle());
    pushRequest.setAndroidPopupBody(notification.getBody());
    // 消息的标题
    pushRequest.setTitle(notification.getTitle());
    // 消息的内容
    pushRequest.setBody(notification.getBody());
    // 推送目标
    pushRequest.setAppKey(Long.parseLong(pushIOSAppkey));
    // 推送目标: DEVICE:按设备推送 ALIAS : 按别名推送 ACCOUNT:按帐号推送  TAG:按标签推送; ALL: 广播推送
    pushRequest.setTarget("ALIAS");
    // 根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
    pushRequest.setTargetValue(notification.getNotifiUserId());
    // 消息类型 MESSAGE NOTICE
    pushRequest.setPushType("NOTICE");
    // 设备类型 ANDROID iOS ALL.
    pushRequest.setDeviceType("ALL");
    // 推送配置
    //        pushRequest.setTitle("ALi Push Title"); // 消息的标题
    //        pushRequest.setBody("Ali Push Body"); // 消息的内容
    // 推送配置: iOS
    //        pushRequest.setIOSBadge(5); // iOS应用图标右上角角标
    // iOS通知声音
    pushRequest.setIOSMusic("default");
    //        pushRequest.setIOSSubtitle("iOS10 subtitle");//iOS10通知副标题的内容

    // 指定iOS10通知Category
    pushRequest.setIOSNotificationCategory("iOS10 Notification Category");
    // 是否允许扩展iOS通知内容
    pushRequest.setIOSMutableContent(true);
    // iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
    pushRequest.setIOSApnsEnv("DEV");
    // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：离线消息转通知仅适用于生产环境
    pushRequest.setIOSRemind(true);
    // iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效
    pushRequest.setIOSRemindBody("iOSRemindBody");
    pushRequest.setIOSExtParameters(
        "{\"page\":\"2\",\"feedsId\":\""
            + notification.getFlowId()
            + "\",\"type\":\""
            + notification.getType()
            + "\"}"); // 通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)
    // 推送配置: Android

    // 通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
    pushRequest.setAndroidNotifyType("BOTH");
    // 通知栏自定义样式0-100
    pushRequest.setAndroidNotificationBarType(1);
    // 通知栏自定义样式0-100
    pushRequest.setAndroidNotificationBarPriority(1);
    // 点击通知后动作 "APPLICATION" : 打开应用 "ACTIVITY" : 打开AndroidActivity "URL" : 打开URL
    pushRequest.setAndroidOpenType("ACTIVITY");
    // "NONE" : 无跳转
    //        pushRequest.setAndroidOpenUrl("http://www.aliyun.com");
    // //Android收到推送后打开对应的url,仅当AndroidOpenType="URL"有效

    // 设定通知打开的activity，仅当AndroidOpenType="Activity"有效
    pushRequest.setAndroidActivity("com.quantum.community.splash.view.WelcomeActivity");
    // Android通知音乐
    pushRequest.setAndroidMusic("default");
    //        pushRequest.setAndroidPopupActivity("com.ali.demo.PopupActivity");//设置该参数后启动辅助弹窗功能,
    // 此处指定通知点击后跳转的Activity（辅助弹窗的前提条件：1. 集成第三方辅助通道；2. StoreOffline参数设为true）

    // 设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
    pushRequest.setAndroidExtParameters(
        "{\"page\":\"2\",\"feedsId\":\""
            + notification.getFlowId()
            + "\",\"type\":\""
            + notification.getType()
            + "\"}");
    pushRequest.setAndroidNotificationChannel("quantum");
    // 推送控制

    // 30秒之间的时间点, 也可以设置成你指定固定时间
    Date pushDate = new Date(System.currentTimeMillis());
    String pushTime = ParameterHelper.getISO8601Time(pushDate);
    // 延后推送。可选，如果不设置表示立即推送

    // 12小时后消息失效, 不会再发送
    pushRequest.setPushTime(pushTime);
    String expireTime =
        ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 12 * 3600 * 1000));
    pushRequest.setExpireTime(expireTime);
    // 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到
    pushRequest.setStoreOffline(true);
    PushResponse pushResponse = null;
    try {
      pushResponse = client.getAcsResponse(pushRequest);
      log.info(
          "IOS RequestId: %s, MessageID: %s\n",
          pushResponse.getRequestId(), pushResponse.getMessageId());
      pushRequest.setAppKey(Long.parseLong(pushAndroidAppkey));
      pushResponse = client.getAcsResponse(pushRequest);
      log.info(
          "Android RequestId: %s, MessageID: %s\n",
          pushResponse.getRequestId(), pushResponse.getMessageId());
    } catch (ClientException e) {
      log.error(e.getMessage(), e.getStackTrace());
    }
  }
}
