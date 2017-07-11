package com.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.ArticleContentDao;
import com.test.model.ArticleContent;

@Repository("articleContentDao")
public class ArticleContentDaoImpl extends SimpleDaoImpl<ArticleContent, String> implements ArticleContentDao{

}
