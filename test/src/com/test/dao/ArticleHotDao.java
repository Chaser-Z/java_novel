package com.test.dao;

import java.util.List;

import com.miger.commons.hibernate.SimpleDao;
import com.test.model.ArticleHot;

public interface ArticleHotDao extends SimpleDao<ArticleHot, String>{

	List<ArticleHot> getHome();
}
