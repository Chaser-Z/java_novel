package com.test.dao;

import java.util.List;

import com.miger.commons.hibernate.SimpleDao;
import com.test.model.ArticleInfo;

public interface ArticleInfoDao extends SimpleDao<ArticleInfo, Integer>{

	 List<ArticleInfo> findLatest(String id, String lastDate);
	 
	 List<ArticleInfo> getByArticleId(String articleId);
}
