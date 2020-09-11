package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.entity.UserBlacklist;
import com.quantumtime.qc.entity.feeds.Video;
import com.quantumtime.qc.repository.HaotuVideoRepository;
import com.quantumtime.qc.repository.UserBlacklistRepository;
import com.quantumtime.qc.repository.VideoRepository;
import com.quantumtime.qc.service.IAddressService;
import com.quantumtime.qc.service.IGaodeService;
import com.quantumtime.qc.service.IVideoRecommendService;
import com.quantumtime.qc.vo.video.PortalRequest;
import com.quantumtime.qc.vo.video.VideoIDSrouce;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.quantumtime.qc.vo.video.VideoIDSrouce.SOURCE_USER;

@Service
@Slf4j
public class VideoRecommendServiceImpl implements IVideoRecommendService {

    //private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${video.portalMaxSize}")
    private int portalMaxSize;

    @Value("${video.currentAddressDay}")
    private int currentAddressDay;

    @Value("${video.nearAddressDay}")
    private int nearAddressDay;

    @Value("${video.cityDay}")
    private int cityDay;

    @Value("${video.openDay}")
    private int openDay;

    @Value("${video.openRate}")
    private double openRate;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private HaotuVideoRepository haotuVideoRepository;

    @Autowired
    private IAddressService addressService;

    @Autowired
    private IGaodeService gaodeService;

    @Autowired
    private UserBlacklistRepository userBlacklistRepository;

    @Override
    public List<VideoIDSrouce> recommend(PortalRequest request) throws Exception {
        long start = System.currentTimeMillis();
        List<VideoIDSrouce> result = new ArrayList<>();
        Set<String> videoIds = new HashSet<>();
        int total = 0;
        Random random = new Random();
        //黑名单
        Set<String> blacklist = getBlacklist(request.getUid());

        //公开视频备选
        List<VideoIDSrouce> availableOpenList = getAvailableOpenVideos(portalMaxSize, blacklist);

        if (request.getAddress() == null) {
            //推荐公开可见的视频
            total = getOpenVideos(result, videoIds, total, availableOpenList, random, blacklist);
            log.info("recommend request id:" + request.getRequestId() + " num:" + total
                    + " cost time:" + (System.currentTimeMillis() - start) + "ms");
            return result;
        }

        //当前小区
        Long addressId = addressService.checkAndSet(request.getAddress());
        if (addressId == null) {
            throw new Exception("无法获取当前小区的地址ID");
        }
        //当前小区可见的视频
        total = getCurrentAddressVideos(addressId, result, videoIds, total, availableOpenList, random, blacklist);

        //附近小区可见视频
        if (total < portalMaxSize) {
            total = getNearAddressVideos(request, result, videoIds, total, availableOpenList, random, blacklist);
        }

        //所在城市的视频
        if (request.getAddress() != null && StringUtils.isNotEmpty(request.getAddress().getCity())
                && total < portalMaxSize) {
            total = getCityVideos(request, result, videoIds, total, availableOpenList, random, blacklist);
        }

        //公开可见视频
        if (total < portalMaxSize) {
            total = getOpenVideos(result, videoIds, total, availableOpenList, random, blacklist);
        }

        log.info("recommend request id:" + request.getRequestId() + " num:" + total
                + " cost time:" + (System.currentTimeMillis() - start) + "ms");
        return result;
    }

    @Override
    public Set<String> getBlacklist(String uid) {
        Set<String> res = new HashSet<>();
        if (uid == null) {
            return res;
        }
        List<UserBlacklist> userBlacklists = userBlacklistRepository.getAll(uid);
        if (userBlacklists != null && userBlacklists.size() > 0) {
            for (UserBlacklist userBlacklist : userBlacklists) {
                if (userBlacklist.getFromUid().equals(uid)) {
                    res.add(userBlacklist.getToUid());
                } else {
                    res.add(userBlacklist.getFromUid());
                }
            }
        }
        return res;
    }

    @Override
    public List<VideoIDSrouce> addressVideoList(PortalRequest request) {
        long start = System.currentTimeMillis();
        List<VideoIDSrouce> result = new ArrayList<>();
        //黑名单
        Set<String> blacklist = getBlacklist(request.getUid());
        //获取某一个地址最早的一条视频
        Long addressId = addressService.checkAndSet(request.getAddress());
        List<Video> firstVideoList = videoRepository.findFirstVideo(addressId);
        if (firstVideoList == null || firstVideoList.size() == 0) {
            return result;
        }
        Video firstVideo = firstVideoList.get(0);
        //获取第一个视频的人为地主，在该地址发的视频
        List<Video> landOwnerList = videoRepository.landOwnerVideoList(firstVideo.getUid(), addressId);
        addAddressVideoList(result, landOwnerList, blacklist);
        //其他人在该地址发的视频
        List<Video> otherList = videoRepository.landOtherVideoList(addressId, firstVideo.getUid());
        addAddressVideoList(result, otherList, blacklist);

        log.info("recommend request id:" + request.getRequestId() + " num:" + result.size()
                + " cost time:" + (System.currentTimeMillis() - start) + "ms");
        return result;
    }

    private void addAddressVideoList(List<VideoIDSrouce> result, List<Video> videoList, Set<String> blacklist) {
        for (Video video : videoList) {
            if (!blacklist.contains(video.getUid())) {
                VideoIDSrouce videoIDSrouce = new VideoIDSrouce();
                videoIDSrouce.setSource(SOURCE_USER);
                videoIDSrouce.setVideoID(video.getVideoId());
                result.add(videoIDSrouce);
            }
        }
    }

    private int getCityVideos(PortalRequest request, List<VideoIDSrouce> result, Set<String> videoIds, int total,
                              List<VideoIDSrouce> availableOpenList, Random random, Set<String> blacklist) {
        long delta = cityDay * 24L * 3600 * 1000;
        Date startDate = new Date(System.currentTimeMillis() - delta);
        List<Video> cityVideoList = videoRepository.findCityLimit(request.getAddress().getCity(),
                startDate, portalMaxSize);
        if (cityVideoList != null && cityVideoList.size() > 0) {
            return addVideoIdSource(result, videoIds, cityVideoList, total, availableOpenList, random, blacklist);
        }
        return total;
    }

    private int getOpenVideos(List<VideoIDSrouce> result, Set<String> videoIds, int total,
                              List<VideoIDSrouce> availableOpenList, Random random, Set<String> blacklist) {
        return addVideoIdSource(result, videoIds, new ArrayList<>(0), total, availableOpenList, random, blacklist);
    }

    private List<VideoIDSrouce> getAvailableOpenVideos(int openNum, Set<String> blacklist) {
        long delta = openDay * 24L * 3600 * 1000;
        Date startDate = new Date(System.currentTimeMillis() - delta);
        List<Video> openVideoList = videoRepository.findOpenLimitNum(startDate, openNum);
        List<VideoIDSrouce> res = new ArrayList<>();
        if (openVideoList != null && openVideoList.size() > 0) {
            for (Video video : openVideoList) {
                if (!blacklist.contains(video.getUid())) {
                    VideoIDSrouce videoIDSrouce = new VideoIDSrouce();
                    videoIDSrouce.setVideoID(video.getVideoId());
                    videoIDSrouce.setSource(VideoIDSrouce.SOURCE_BACKEND);
                    res.add(videoIDSrouce);
                }
            }
        }
        return res;
    }

    private int getNearAddressVideos(PortalRequest request, List<VideoIDSrouce> result, Set<String> videoIds, int total,
                                     List<VideoIDSrouce> availableOpenList, Random random, Set<String> blacklist) {
        //添加当前位置5公里之内的发布地点视频
        List<Long> addressIdList = gaodeService
                .getNearCommunities(request.getAddress().getLongitude(),
                        request.getAddress().getLatitude());
        if (addressIdList != null && addressIdList.size() > 0) {
            long delta = nearAddressDay * 24L * 3600 * 1000;
            Date startDate = new Date(System.currentTimeMillis() - delta);
            List<Video> nearVideoList = videoRepository.findAllByIdsLimit(addressIdList, startDate, portalMaxSize);
            if (nearVideoList != null && nearVideoList.size() > 0) {
                return addVideoIdSource(result, videoIds, nearVideoList, total, availableOpenList, random, blacklist);
            }
        }
        return total;
    }

    private int getCurrentAddressVideos(Long addressId, List<VideoIDSrouce> result,
                                        Set<String> videoIds, int total,
                                        List<VideoIDSrouce> availableOpenList, Random random, Set<String> blacklist) {
        //计算时间
        long delta = currentAddressDay * 24L * 3600 * 1000;
        Date startDate = new Date(System.currentTimeMillis() - delta);
        List<Video> curVideoList = videoRepository.findByIdLimit(addressId, startDate, portalMaxSize);
        if (curVideoList != null && curVideoList.size() > 0) {
            return addVideoIdSource(result, videoIds, curVideoList, total, availableOpenList, random, blacklist);
        }
        return total;
    }

    private int addVideoIdSource(List<VideoIDSrouce> result, Set<String> videoIds, List<Video> videoList,
                                 int total, List<VideoIDSrouce> availableOpenList, Random random, Set<String> blacklist) {
        int left = portalMaxSize - total;
        int fetchOpen = (int)(openRate * left);
        int fetchCurrent = left - fetchOpen;
        if (videoList.size() == 0) {
            fetchOpen = left;
            fetchCurrent = 0;
        } else if (videoList.size() < fetchCurrent) {
            fetchCurrent = videoList.size();
            fetchOpen = (int)(1.0d * fetchCurrent / (1 - openRate) * openRate);
        }

        int i = 0;
        List<VideoIDSrouce> temp = new ArrayList<>(left);
        while (total < portalMaxSize && i < fetchCurrent) {
            String videoId = videoList.get(i).getVideoId();
            String vUid = videoList.get(i).getUid();
            if (!videoIds.contains(videoId) && !blacklist.contains(vUid)) {
                VideoIDSrouce videoIDSrouce = new VideoIDSrouce();
                videoIDSrouce.setVideoID(videoId);
                videoIDSrouce.setSource(SOURCE_USER);
                temp.add(videoIDSrouce);
                videoIds.add(videoList.get(i).getVideoId());
                total++;
            }
            i++;
        }
        Iterator<VideoIDSrouce> iter = availableOpenList.iterator();
        int j = 0;
        while (iter.hasNext() && j < fetchOpen) {
            temp.add(iter.next());
            j++;
            total++;
            iter.remove();
        }
        Collections.shuffle(temp, random);
        result.addAll(temp);
        return total;
    }
}
