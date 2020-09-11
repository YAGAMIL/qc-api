package com.quantumtime.qc.service;

import com.quantumtime.qc.vo.video.PortalRequest;
import com.quantumtime.qc.vo.video.VideoIDSrouce;

import java.util.List;
import java.util.Set;

public interface IVideoRecommendService {
    List<VideoIDSrouce> recommend(PortalRequest request) throws Exception;

    List<VideoIDSrouce> addressVideoList(PortalRequest portalRequest);

    public Set<String> getBlacklist(String uid);
}
