package com.test.dao;

import java.util.List;

import com.miger.commons.hibernate.SimpleDao;
import com.test.model.ArticleInfo;

public interface ArticleInfoDao extends SimpleDao<ArticleInfo, String>{

	 List<ArticleInfo> findLatest(String id, String lastDate);
}
