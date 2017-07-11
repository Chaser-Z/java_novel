package com.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.ArticleHotDao;
import com.test.model.ArticleHot;

@Repository("articleHotDao")
public class ArticleHotDaoImpl extends SimpleDaoImpl<ArticleHot, String> implements ArticleHotDao {

}
