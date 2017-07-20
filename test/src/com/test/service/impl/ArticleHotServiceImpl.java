package com.test.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.ArticleHotDao;
import com.test.model.ArticleHot;
import com.test.service.ArticleHotService;

@Service("articleHotService")
public class ArticleHotServiceImpl  implements ArticleHotService{
	
	@Autowired
	private ArticleHotDao articleHotDao;
	
	@Override
	public List<ArticleHot> getHot() throws Exception {
		
		List<ArticleHot> list = articleHotDao.find("from ArticleHot a");
		if (list == null) {
			return new  ArrayList<ArticleHot>();
		} else {
			return list;
		}
		
	}
	
	@Override
	public List<ArticleHot> getArticleByType(String type) throws Exception {
		
		if (type == null || type.length() == 0) {
			return new ArrayList<ArticleHot>();
		}
		return articleHotDao.find("from ArticleHot sa where sa.article_type = ?", type);
	}

	@Override
	public List<ArticleHot> getHome() throws Exception {
		List<ArticleHot> list = articleHotDao.getHome();
		if (list == null) {
			return new  ArrayList<ArticleHot>();
		} else {
			return list;
		}
	}
	
	@Override
	public List<ArticleHot> getArticleByKeyword(String keyword) throws Exception {
		List<ArticleHot> list = articleHotDao.getArticleByKeyword(keyword);
		if (list == null) {
			return new  ArrayList<ArticleHot>();
		} else {
			return list;
		}
	}


}
