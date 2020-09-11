package com.quantumtime.qc.service;

import com.quantumtime.qc.entity.feeds.Article;


import java.util.List;

public interface IArticleService {
    /**
     * 批量查询文章
     */
    List<Article> findAllByIds(List<Long> ids);

}
