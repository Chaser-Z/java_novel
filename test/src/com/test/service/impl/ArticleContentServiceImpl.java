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
	public ArticleContent getbyDirectoryLink(Integer id) throws Exception {
		
		if (id == null) {
			return null;
		}
		
		ArticleContent content = articleContentDao.getbyDirectoryLink(id);
		
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
	public List<ArticleContent> getContentsById(String article_id, Integer id) throws Exception {
		if (article_id == null || article_id.length() == 0) {
			return new ArrayList<ArticleContent>();
		}
		
		List<ArticleContent> list = articleContentDao.getContentsById(article_id, id);
		System.out.println(list);
		return list;
		//return articleContentDao.find("from ArticleContent sa where sa.article_id = ?", id);
	}

}
