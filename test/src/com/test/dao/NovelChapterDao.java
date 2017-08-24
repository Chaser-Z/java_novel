package com.test.dao;

import java.util.List;
import java.util.Map;

import com.miger.commons.hibernate.SimpleDao;
import com.test.model.NovelChapter;

public interface NovelChapterDao extends SimpleDao<NovelChapter, Integer>{

	 List<NovelChapter> findLatest(String id, String lastDate);
	 
	 List<NovelChapter> getByArticleId(String articleId);
	 
	 Map<String, Object> page(String rows, String current, String title, String article_directory);
}
