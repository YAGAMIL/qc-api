package com.quantumtime.qc.repository;
import com.quantumtime.qc.entity.feeds.Article;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Description: 地址业务持久层 Created on 2019/09/16 21:00
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {



}
