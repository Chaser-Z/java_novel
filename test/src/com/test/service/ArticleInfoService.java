package com.test.service;

import java.util.List;

import com.test.model.ArticleInfo;


public interface ArticleInfoService {

    List<ArticleInfo> getByArticleId(String articleId) throws Exception;
    
    List<ArticleInfo> findLatest(String id, String lastDate) throws Exception;
	
}
