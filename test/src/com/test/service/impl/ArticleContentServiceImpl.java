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
	public ArticleContent getbyDirectoryLink(String article_id, String directoryLink) throws Exception {
		
		if (directoryLink == null || directoryLink.length() == 0) {
			return null;
		}
		
		ArticleContent content = articleContentDao.getbyDirectoryLink(article_id, directoryLink);
		
//		List<ArticleContent> list =  articleContentDao.find("from ArticleContent sa where sa.article_id = ? and sa.article_directory_link = ?", article_id, directoryLink);
//        if (list != null && list.size() > 0) {
//        	System.out.println(list.size());
//        	return list.get(0);
//        }
		if (content != null) {
			return content;
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
