package com.test.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.ArticleContentDao;
import com.test.model.ArticleContent;
import com.test.service.ArticleContentService;

@Service
public class ArticleContentServiceImpl implements ArticleContentService{
	
	@Autowired
	private ArticleContentDao articleContentDao;
	
	@Override
	public ArticleContent getbyDirectoryLink(String directoryLink) throws Exception {
		
		if (directoryLink == null || directoryLink.length() == 0) {
			return null;
		}
		List<ArticleContent> list =  articleContentDao.find("from ArticleContent sa where sa.article_directory_link = ?", directoryLink);
        if (list != null && list.size() > 0) {
        	System.out.println(list.size());
        	return list.get(0);
        }
        return null;
	}
	
	@Override
	public List<ArticleContent> getContentsById(String id) throws Exception {
		if (id == null || id.length() == 0) {
			return new ArrayList<ArticleContent>();
		}
		return articleContentDao.find("from ArticleContent sa where sa.article_id = ?", id);
	}

}
