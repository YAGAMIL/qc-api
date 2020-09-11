package com.quantumtime.qc;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.green.model.v20180509.ImageSyncScanRequest;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.quantumtime.qc.common.properties.AliCloudProperties;
import com.quantumtime.qc.entity.StarFan;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.repository.ActivityRepository;
import com.quantumtime.qc.repository.ClickContentRepository;
import com.quantumtime.qc.repository.UserRepository;
import com.quantumtime.qc.repository.VideoRepository;
import com.quantumtime.qc.service.ClickContentService;
import com.quantumtime.qc.service.IAddressService;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.service.LegalCheckService;
import com.quantumtime.qc.service.StarFanService;
import com.quantumtime.qc.service.ActivityService;
import com.quantumtime.qc.service.impl.activity.VideoHeatService;
import com.quantumtime.qc.vo.video.VideoItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import static com.quantumtime.qc.AliStsTest.REGION_CN_HANGZHOU;
import static org.apache.commons.codec.CharEncoding.UTF_8;

@Slf4j
public class MyCheckTest extends QCApplicationTests {
    @Resource
    AliCloudProperties aliCloudProperties;

    @Resource
    LegalCheckService legalCheckService;

    @Resource
    IUserService userService;

    @Resource
    StarFanService starFanService;

    @Resource
    UserRepository userRepository;

    @Resource
    VideoRepository videoRepository;

    @Resource
    ClickContentRepository clickContentRepository;

    @Resource
    IAddressService addressService;

    @Resource
    ClickContentService clickContentService;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    ActivityService activityService;

    @Resource
    ActivityRepository activityRepository;

    @Resource
    VideoHeatService videoHeatService;


    @Test
    public void check() {
        IClientProfile profile = DefaultProfile.getProfile(
                REGION_CN_HANGZHOU, aliCloudProperties.getAccessKey(), aliCloudProperties.getSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setSysAcceptFormat(FormatType.JSON); // 指定api返回格式
        textScanRequest.setHttpContentType(FormatType.JSON);
        textScanRequest.setSysMethod(MethodType.POST); // 指定请求方法
        textScanRequest.setSysEncoding("UTF-8");
        textScanRequest.setSysRegionId(REGION_CN_HANGZHOU);
        List<Map<String, Object>> tasks = new ArrayList<>();
        Map<String, Object> task1 = new LinkedHashMap<>();
        task1.put("dataId", UUID.randomUUID().toString());
        /* 待检测的文本，长度不超过10000个字符 */
        task1.put("content", "test content");
        tasks.add(task1);
        JSONObject data = new JSONObject();

        /* 检测场景，文本垃圾检测传递：antispam */
        data.put("scenes", Collections.singletonList("antispam"));
        data.put("tasks", tasks);
        System.out.println(JSON.toJSONString(data, true));
        textScanRequest.setHttpContent(data.toJSONString().getBytes(StandardCharsets.UTF_8), UTF_8, FormatType.JSON);
        // 请务必设置超时时间
        textScanRequest.setSysConnectTimeout(3000);
        textScanRequest.setSysReadTimeout(6000);
        try {
            HttpResponse httpResponse = client.doAction(textScanRequest);
            if (httpResponse.isSuccess()) {
                JSONObject scrResponse =
                        JSON.parseObject(new String(httpResponse.getHttpContent(), StandardCharsets.UTF_8));
                System.out.println(JSON.toJSONString(scrResponse, true));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if (200 == ((JSONObject) taskResult).getInteger("code")) {
                            JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                            for (Object sceneResult : sceneResults) {
                                String scene = ((JSONObject) sceneResult).getString("scene");
                                String suggestion = ((JSONObject) sceneResult).getString("suggestion");
                                // 根据scene和suggetion做相关处理
                                // suggestion == pass 未命中垃圾  suggestion == block 命中了垃圾，可以通过label字段查看命中的垃圾分类
                                System.out.println("args = [" + scene + "]");
                                System.out.println("args = [" + suggestion + "]");
                            }
                        } else {
                            System.out.println("task process fail:" + ((JSONObject) taskResult).getInteger("code"));
                        }
                    }
                } else {
                    System.out.println("detect not success. code:" + scrResponse.getInteger("code"));
                }
            } else {
                System.out.println("response not success. status:" + httpResponse.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void imgTest() {
        IClientProfile profile = DefaultProfile.getProfile(
                REGION_CN_HANGZHOU, aliCloudProperties.getAccessKey(), aliCloudProperties.getSecret());
        DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "Green");
        IAcsClient client = new DefaultAcsClient(profile);

        ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
        // 指定api返回格式
        imageSyncScanRequest.setSysAcceptFormat(FormatType.JSON);
        // 指定请求方法
        imageSyncScanRequest.setSysMethod(MethodType.POST);
        imageSyncScanRequest.setSysEncoding("utf-8");
        // 支持http和https
        imageSyncScanRequest.setSysProtocol(ProtocolType.HTTP);

        JSONObject httpBody = new JSONObject();
        /*
         设置要检测的场景, 计费是按照该处传递的场景进行 一次请求中可以同时检测多张图片，每张图片可以同时检测多个风险场景，计费按照场景计算
         例如：检测2张图片，场景传递porn、terrorism，计费会按照2张图片鉴黄，2张图片暴恐检测计算 porn: porn表示色情场景检测
        */
        httpBody.put("scenes", Arrays.asList("porn", "ad", "terrorism"));

        /*
         设置待检测图片， 一张图片一个task 多张图片同时检测时，处理的时间由最后一个处理完的图片决定 通常情况下批量检测的平均rt比单张检测的要长, 一次批量提交的图片数越多，rt被拉长的概率越高
         这里以单张图片检测作为示例, 如果是批量图片检测，请自行构建多个task
        */
        JSONObject task = new JSONObject();
        task.put("dataId", UUID.randomUUID().toString());

        // 设置图片链接
        task.put(
                "url",
                "http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83erKJb12UoiagIaa4q2tsQj8Tcew02icSRxlPMTIHk8SYTibIib7geDs4wPpSjicQHgbyacdiaf1NnSDQicQw/132");
        task.put("time", new Date());
        httpBody.put("tasks", Collections.singletonList(task));

        imageSyncScanRequest.setHttpContent(
                StringUtils.getBytesUtf8(httpBody.toJSONString()), "UTF-8", FormatType.JSON);

        /* 请设置超时时间, 服务端全链路处理超时时间为10秒，请做相应设置 如果您设置的ReadTimeout小于服务端处理的时间，程序中会获得一个read timeout异常 */
        imageSyncScanRequest.setSysConnectTimeout(3000);
        imageSyncScanRequest.setSysReadTimeout(10000);
        HttpResponse httpResponse = null;
        try {
            httpResponse = client.doAction(imageSyncScanRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 服务端接收到请求，并完成处理返回的结果
        if (httpResponse != null && httpResponse.isSuccess()) {
            JSONObject scrResponse = JSON.parseObject(StringUtils.newStringUtf8(httpResponse.getHttpContent()));
            System.out.println(JSON.toJSONString(scrResponse, true));
            int requestCode = scrResponse.getIntValue("code");
            // 每一张图片的检测结果
            JSONArray taskResults = scrResponse.getJSONArray("data");
            if (200 == requestCode) {
                for (Object taskResult : taskResults) {
                    // 单张图片的处理结果
                    int taskCode = ((JSONObject) taskResult).getIntValue("code");
                    // 图片要检测的场景的处理结果, 如果是多个场景，则会有每个场景的结果
                    JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                    if (200 == taskCode) {
                        // 根据scene和suggetion做相关处理
                        // do something
                        sceneResults.forEach(sceneResult -> {
                            String scene = ((JSONObject) sceneResult).getString("scene");
                            String suggestion = ((JSONObject) sceneResult).getString("suggestion");
                            System.out.println("scene = [" + scene + "]");
                            System.out.println("suggestion = [" + suggestion + "]");
                        });
                    } else {
                        // 单张图片处理失败, 原因视具体的情况详细分析
                        System.out.println("task process fail. task response:" + JSON.toJSONString(taskResult));
                    }
                }
            } else {
                /* 表明请求整体处理失败，原因视具体的情况详细分析 */
                System.out.println("the whole image scan request failed. response:" + JSON.toJSONString(scrResponse));
            }
        }
    }

    @Test
    public void serviceTest() {
        String text = "666";
        text = legalCheckService.checkText(text);
        System.out.println(text);
    }

    @Test
    public void recordAddr() {
        String uid = "2c91558b6df7b963016e00f563460014";
        List<Address> records1 = userService.queryRecords(uid, 589L);
        records1.forEach(System.err::println);
        //        AddressRecord record = new
        // AddressRecord().setAddressId(13L).setUid(uid).setCreateTime(LocalDateTime.now());
        //        recordRepository.save(record);
        //        System.out.println("------------------");
        //        List<AddressRecord> byUid = recordRepository.findByUid(uid);
        //        byUid.forEach(System.err::println);
        //        List<AddressRecord> records = recordService.queryRecords(uid);
        //        System.out.println("------------------");
        //        records.forEach(System.err::println);
        //        List<Address> addresses = userService.queryRecords();
        //        addresses.forEach(System.err::println);

        //
        //        starFanService.attention("1","2");
        //        starFanService.attention("2","1");
        //        AttentionResult attention = starFanService.isAttention("1", "2");
        //        System.err.println(attention);
        starFanService.unsubscribe("2", "1");
        //        System.err.println(starFanService.isAttention("1", "2"));
        //        boolean b = userService.removeRecords(uid, 13L);

        //        System.out.println(b);
    }

    @Test
    public void jpaPage() {
        Page<StarFan> bookCriteria = starFanService.page4Stars("", 0, 2);
        System.err.println(bookCriteria.toString());
        bookCriteria.getContent().forEach(System.err::println);
        System.out.println(bookCriteria);
    }

    @Test
    public void findByIds() {
        /*   List<String> list = Arrays.asList();
        List<User> byUidList = userRepository.findByUidList(list);
        byUidList.forEach(System.err::println);*/
        String uid = "2c91558b6df7b963016dfbd6ea27000b";
        User one = userRepository.getOne(uid);
        System.err.println("对象" + one.toString());
        System.err.println("uid----" + one.getUid());
        one.setBirthday(new Date());
        userService.update(one);
        ;
        System.err.println(one);
        User byId = userService.findById(uid);
        System.err.println("By-uid" + byId);
    }

    @Test
    public void findVideoView() {
        List<Object> objects =
                clickContentRepository.videoViewNum(Collections.singletonList("00a0b96707a945c38544cfdca9fedbdc"));
        objects.forEach(object -> {
            Object[] ary = (Object[]) object;
            System.err.println(ary[0].toString());
            System.err.println(Integer.parseInt(ary[1].toString()));
        });
        //        Address byPoiId = addressService.findByPoiId("B000A8XHL3");
        //        addressService.checkAndSet(byPoiId.setLatitude("0").setLongitude("0"));

    }

    @Test
    public void jpaMap() {
        List<Object> clickList = clickContentRepository.videoClickSum("c8e554e163d0447ab73a65b636950735");
        //        map.forEach((k, v) -> System.err.println(k+v));
        //        List<ClickContent> clickList =
        // clickContentRepository.findAllByContentIdAndContentType("0de2104b8ed749b4a7884d67117bc1cf",2);
        Map<Integer, Integer> map = new HashMap<>(16);
        if (clickList != null && clickList.size() > 0) {
            clickList.forEach(object -> {
                Object[] ary = (Object[]) object;
                map.put(Integer.parseInt(ary[0].toString()), Integer.parseInt(ary[1].toString()));
            });
        }
        map.forEach((k, v) -> System.err.println(k + "--" + v));

        //        clickList.forEach(System.out::println);
    }

    @Test
    public void jpaBatch() {
        //        long l = System.currentTimeMillis();
        //        List<ClickContent> results = clickContentService.robotClickOne(new ArrayList<>(),
        // "000013aa080141b7b499241a886d5d15");
        //
        //        long b = System.currentTimeMillis();
        //        System.err.println(b - l + "ms");

        clickContentRepository.removeRobot();
    }

    public void checkUrl() {
        String jsonStr = "{\n"
                + "    \"Status\": \"success\",\n"
                + "    \"SnapshotCount\": 1,\n"
                + "    \"VideoId\": \"d34ba4502e5a4a86a6247a320dc7a9b3\",\n"
                + "    \"SnapshotFormat\": \"d34ba4502e5a4a86a6247a320dc7a9b3/snapshots/normal/37C46092-16E58FD12EB-1831-4239-161-53787{SnapshotCount}.jpg\",\n"
                + "    \"SubType\": \"SpecifiedTime\",\n"
                + "    \"EventType\": \"SnapshotComplete\",\n"
                + "    \"Extend\": \"{\\\"videoFrom\\\":1}\",\n"
                + "    \"EventTime\": \"2019-11-11T05:43:31Z\",\n"
                + "    \"SnapshotRegular\": \"http://vod.milinzone.com/d34ba4502e5a4a86a6247a320dc7a9b3/snapshots/normal/37C46092-16E58FD12EB-1831-4239-161-53787{SnapshotCount}.jpg\",\n"
                + "    \"JobId\": \"3c9c6eb4f079429a95c6c089761530d2\",\n"
                + "    \"SnapshotInfos\": [{\n"
                + "        \"SnapshotCount\": 1,\n"
                + "        \"Status\": \"success\",\n"
                + "        \"SnapshotFormat\": \"d34ba4502e5a4a86a6247a320dc7a9b3/snapshots/normal/37C46092-16E58FD12EB-1831-4239-161-53787{SnapshotCount}.jpg\",\n"
                + "        \"SnapshotRegular\": \"http://vod.milinzone.com/d34ba4502e5a4a86a6247a320dc7a9b3/snapshots/normal/37C46092-16E58FD12EB-1831-4239-161-53787{SnapshotCount}.jpg\",\n"
                + "        \"SnapshotType\": \"NormalSnapshot\",\n"
                + "        \"JobId\": \"3c9c6eb4f079429a95c6c089761530d2\"\n"
                + "    }]\n"
                + "}";

        JSONObject event = JSON.parseObject(jsonStr);
    }

    @Test
    public void checkZSet() {
        TreeSet<VideoItem> heatResult = new TreeSet<> ((o1, o2) -> {
            VideoItem p1 = (VideoItem) o1;
            VideoItem p2 = (VideoItem) o2;
            return p1.getHeat().compareTo(p2.getHeat());
        });
        List<VideoItem> items = new ArrayList<> ();
        for (int i = 0; i <10; i++) {
            VideoItem videoItem = new VideoItem().setHeat((long) (i + new Random().nextInt(20)));
            items.add(new VideoItem().setVideoId(i+"").setHeat((long) (i + new Random().nextInt(20))));
            //noinspection unchecked
            Boolean result = redisTemplate.opsForZSet().add("ACTIVITY_ID-1", videoItem, videoItem.getHeat());
        }
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<> ();
        items.forEach(i -> tuples.add(new DefaultTypedTuple<String>(i.getVideoId(),i.getHeat()/1.0)) );
        heatResult.addAll(items);
        VideoItem item = new VideoItem().setVideoId("123").setHeat(889L);
        heatResult.add(item);
//        redisTemplate.opsForZSet().add("ACTIVITY_ID-2", item,889L);
//        redisService.addZset("ACTIVITY_ID-2",tuples);
//        redisTemplate.opsForZSet().range("typedTupleSet", 0, -1);
        redisTemplate.opsForZSet().add("ACTIVITY_ID-3",tuples);
        Set zSetValue = redisTemplate.opsForZSet().range("ACTIVITY_ID-3", 0, -1);
//        redisTemplate.opsForZSet().add("ACTIVITY_ID-1", item, item.getHeat());
//        redisTemplate.opsForZSet().add("ACTIVITY_ID-2", item.setHeat(10L), 10L);
//        Set<Object> range = redisService.getZsetReverseRange("ACTIVITY_ID-1");
//        List<VideoItem> result = range.stream().map(obj -> (VideoItem) obj).collect(Collectors.toList());
//        result.forEach(System.err::println);
//        Long reverseRank = redisTemplate.opsForZSet().reverseRank("ACTIVITY_ID-1", item);

//        ZSetOperations.TypedTuple<Object> typedTuple1 = new DefaultTypedTuple<Object>("E",6.0);
//        ZSetOperations.TypedTuple<Object> typedTuple2 = new DefaultTypedTuple<Object>("F",7.0);
//        ZSetOperations.TypedTuple<Object> typedTuple3 = new DefaultTypedTuple<Object>("G",5.0);
//        Set<ZSetOperations.TypedTuple<Object>> typedTupleSet = new HashSet<ZSetOperations.TypedTuple<Object>>();
//        typedTupleSet.add(typedTuple1);
//        typedTupleSet.add(typedTuple2);
//        typedTupleSet.add(typedTuple3);
//        redisTemplate.opsForZSet().add("typedTupleSet",typedTupleSet);
//        Set zSetValue = redisTemplate.opsForZSet().range("typedTupleSet", 0, -1);
        System.out.println("通过add(K key, Set<ZSetOperations.TypedTuple<V>> tuples)方法添加元素:" + zSetValue);

    }


}
