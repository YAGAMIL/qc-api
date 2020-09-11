package com.quantumtime.qc;

import com.quantumtime.qc.constant.TlsConstant;
import com.quantumtime.qc.entity.poi.Business;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.entity.feeds.Video;
import com.quantumtime.qc.repository.AddressRepository;
import com.quantumtime.qc.repository.BusinessRepository;
import com.quantumtime.qc.repository.UserRepository;
import com.quantumtime.qc.repository.VideoRepository;
import com.quantumtime.qc.service.*;
import com.quantumtime.qc.utils.UidUtil;
import com.quantumtime.qc.vo.RegisterUser;
import com.quantumtime.qc.vo.UserInfo;
import com.quantumtime.qc.vo.tls.TlsModifyAccount;
import com.quantumtime.qc.vo.tls.TlsModifyAccountProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QCApplicationTests {

    @Resource
    private BusinessRepository businessRepository;

    @Resource
    private UserRepository userRepository;

    @Autowired
    private ITlsService tlsService;

    @Resource
    private IUserService userService;

    @Resource
    private IRedisService redisService;

    @Resource
    private IAddressService addressService;

    @Resource
    private AddressRepository addressRepository;

    @Resource
    private VideoRepository videoRepository;

    @Resource
    private UserTaskService userTaskService;

 /* @Value("${user.avatar}")
  private String defaultAvatar;

  @Test
  public void registerTlsUser() {
    TlsRegisterAccount tlsAccount = new TlsRegisterAccount();
    tlsAccount.setNick("test2");
    tlsAccount.setFaceUrl(defaultAvatar);
    tlsAccount.setIdentifier("13767767743");
    tlsAccount.setType(0);
    boolean b = tlsService.registerTlsUser(tlsAccount);
  }*/

    @Test
    public void registerTest() {
        RegisterUser registerUser = RegisterUser.builder().unionId("XXXXXXXXX").wxNumber("Tablo").wxNickname("clearLove777").username("你大爷").build();
        UserInfo user = userService.registerEnd(registerUser);
        System.err.println(user.toString());
    }

    @Test
    public void maxIdTest() {
        System.err.println(userRepository.findByUnionId("sfadfasdfaweefefe4451"));
        System.err.println(userService.getMaxId());
    }

    @Test
    public void buildIdTest() {
        System.err.println(UidUtil.buildUid());
    }

    @Test
    public void streamTest() {
//        Object obj1 = redisService.get(SmsConstant.SMS_MODIFY_PHONE_FIRST_PREFIX + "15592991875");
//        Object obj2 =
//                redisService.get(SmsConstant.SMS_MODIFY_PHONE_SECOND_PREFIX + "15592991875");
//        ExpFunction.of(true).true4Throw(() -> new JpaCrudException("You cannot update an empty entity class."));
        Optional<Video> one = videoRepository.findById("73d740ce68b34bc7ac54ce380eee0afe");
        Video video = one.orElse(null);
        System.err.println(one);
    }
    @Test
    public void findIdTest() {

        User byId = userService.findById("2c91558b6ddcd1ca016ddcfa91110000");
        byId.setOpusSum(150);
//        userService.save(byId);
        UserInfo userInfo = byId.convert2Info();
//        BeanUtils.copyProperties(byId,userInfo);
        System.err.println(userInfo);
        System.err.println("byId copy---"+byId);
        User use = new User();
        System.err.println("userInfo2"+userInfo+"/n"+"use：----" + use);

//        System.err.println(userService.getCurrentId("07483c4f03bd6424ea05c0c60c507b"));
//        Address addressById = addressService.findAddressById(44444444L);
//        Address byId = addressService.findById(44444444L);
//        System.err.println(byId);
//        System.err.println(addressById);
    }

    @Test
    public void findByCase(){
        String nickName = "CleaRlOve";
        System.err.println(userRepository.findUserByNickname(nickName));

    }
    @Test
    public void modifyTlsUser() {
        TlsModifyAccountProfile nickProfile = new TlsModifyAccountProfile();
        nickProfile.setTag(TlsConstant.NICK);
        nickProfile.setValue("test11");
        List<TlsModifyAccountProfile> profiles = new ArrayList<>();
        profiles.add(nickProfile);
        TlsModifyAccount tlsModifyAccount = new TlsModifyAccount();
        tlsModifyAccount.setFrom_Account("18824843235");
        tlsModifyAccount.setProfileItem(profiles);
        boolean b = tlsService.modifyTlsUser(tlsModifyAccount);
    }

    @Test
    public void contextLoads() {
        Business business =
                new Business();
        businessRepository.save(business);
        System.err.println(business);
        System.err.println("-----------------over----------------");
    }

    public static void main(String[] args) {
        String string = UUID.randomUUID().toString();
        System.err.println(string);
    }
}
