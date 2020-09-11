package com.quantumtime.qc.service;

import com.quantumtime.qc.vo.recommend.SquareResponse;

public interface IRecommendService {

    SquareResponse getSquareResponse(int pageNum, int pageSize);
}
