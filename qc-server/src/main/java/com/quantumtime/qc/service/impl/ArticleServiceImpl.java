package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.entity.feeds.Article;
import com.quantumtime.qc.repository.ArticleRepository;
import com.quantumtime.qc.service.IArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<Article> findAllByIds(List<Long> ids) {


        return articleRepository.findAllById(ids);
    }
}
