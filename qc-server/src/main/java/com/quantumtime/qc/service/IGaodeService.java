package com.quantumtime.qc.service;

import java.util.List;

public interface IGaodeService {
    List<Long> getNearCommunities(String longitude, String latitude);

}
