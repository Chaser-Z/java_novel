package com.test.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.ArticleInfoDao;
import com.test.model.ArticleInfo;
import com.test.service.ArticleInfoService;

@Service
public class ArticleInfoServiceImpl implements ArticleInfoService{

	
	@Autowired
	private ArticleInfoDao articleInfoDao;
	
	
	@Override
    public List<ArticleInfo> getByArticleId(String articleId) throws Exception {
		if (articleId == null || articleId.length() == 0) {
            return new ArrayList<ArticleInfo>();
		}
		//return articleInfoDao.find("from ArticleInfo sa where sa.article_id = ?", articleId);
		return articleInfoDao.getByArticleId(articleId);
	}
	
	@Override
	public List<ArticleInfo> findLatest(String id, String lastDate) throws Exception {
		if (id == null || lastDate == null || id.length() == 0 || lastDate.length() == 0) {
			return null;
		}
		return articleInfoDao.findLatest(id, lastDate);
	}

	
}
