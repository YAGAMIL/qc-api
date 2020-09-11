//package com.quantumtime.qc;
//
//import com.quantumtime.qc.common.constant.SmsConstant;
//import com.quantumtime.qc.common.jwt.JwtUser;
//import com.quantumtime.qc.entity.information.InformationFlow;
//import com.quantumtime.qc.entity.undo.Notification;
//import com.quantumtime.qc.service.IInformationFlowService;
//import com.quantumtime.qc.service.INotificationService;
//import com.quantumtime.qc.service.IRedisService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Map;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class InformationTest {
//
//    @Autowired
//    private IInformationFlowService informationFlowService;
//
//    @Autowired
//    private IRedisService redisService;
//
//    @Autowired
//    private INotificationService notificationService;
//
//    public void init(){
//        String authToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxODgyNDg0MzIzNSIsImNyZWF0ZWQiOjE1NjA3Nzk1NTQ1MDV9.KXn77IjvJz3sI1qMWP49U6n-Vk61VuCi9qX2W3SW-coYoqEoiEQkdSGxMkKH1bOvq54sTwfPfmGW3A6MbG5NZA";
//        Map map = redisService.getMap(SmsConstant.TOKEN_PREFIX + "18824843235");
//        JwtUser userDetails = (JwtUser) map.get(authToken);
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//
//    @Test
//    public void testInsert(){
//        init();
//        InformationFlow flow1 = new InformationFlow();
//        flow1.setContent("第一条信息流 全部人可见 没有主题");
//        flow1.setType(Byte.parseByte("1"));
//        flow1.setIslike(true);
//        flow1.setIsComment(true);
//        flow1.setIsForward(true);
//        flow1.setForwardHierarchy(null);
//        flow1.setThemeId(null);
//        flow1.setIsDelete(false);
//        flow1.setAddressId(3L);
//        flow1.setLikeNum(1L);
//        flow1.setCommentNum(1L);
//        flow1.setForwardNum(1L);
//        flow1.setCreateId("402881e7672689ac0167268b687a0000");
//        flow1.setForwardResult(null);
//        flow1 = informationFlowService.publishInformationFlow(flow1);
//
//        flow1.setForwardHierarchy(flow1.getId()+"");
//        flow1.setId(null);
//        flow1.setContent("转发 第一条信息流 全部人可见 没有主题");
//        flow1 = informationFlowService.publishInformationFlow(flow1);
//
//        flow1.setForwardHierarchy(flow1.getId()+"");
//        flow1.setId(null);
//        flow1.setContent("转发 转发 第一条信息流 全部人可见 没有主题");
//        flow1 = informationFlowService.publishInformationFlow(flow1);
//
//        flow1.setForwardHierarchy(null);
//        flow1.setId(null);
//        flow1.setContent("第二条信息流 全部人可见 主题id 1");
//        flow1.setThemeId(1L);
//        flow1 = informationFlowService.publishInformationFlow(flow1);
//
//        flow1.setForwardHierarchy(flow1.getId()+"");
//        flow1.setId(null);
//        flow1.setContent("转发 第二条信息流 全部人可见 主题id 1");
//        flow1.setThemeId(1L);
//        flow1 = informationFlowService.publishInformationFlow(flow1);
//
//        flow1.setForwardHierarchy(flow1.getId()+"");
//        flow1.setId(null);
//        flow1.setContent("转发 转发 第二条信息流 全部人可见 主题id 1");
//        flow1.setThemeId(1L);
//        flow1 = informationFlowService.publishInformationFlow(flow1);
//
//        flow1.setForwardHierarchy(null);
//        flow1.setId(null);
//        flow1.setContent("第三条信息流 仅完成身份验证可见 主题id 2");
//        flow1.setThemeId(2L);
//        flow1 = informationFlowService.publishInformationFlow(flow1);
//
//        flow1.setForwardHierarchy(flow1.getId()+"");
//        flow1.setId(null);
//        flow1.setContent("转发 第三条信息流 仅完成身份验证可见 主题id 2");
//        flow1.setThemeId(2L);
//        flow1 = informationFlowService.publishInformationFlow(flow1);
//
//        flow1.setForwardHierarchy(flow1.getId()+"");
//        flow1.setId(null);
//        flow1.setContent("转发 转发 第三条信息流 仅完成身份验证可见 主题id 2");
//        flow1.setThemeId(2L);
//        flow1 = informationFlowService.publishInformationFlow(flow1);
//
//    }
//
//    @Test
//    public void testPush(){
//        Notification notification = new Notification();
//        notification.setNotifiUserId("2c9155846bf89e01016bf950ba590001");
//        notificationService.pushNotification(notification);
//    }
//
//
//}
