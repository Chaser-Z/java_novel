package com.test.service;

import java.util.List;
import java.util.Map;

import com.test.model.NovelChapter;


public interface NovelChapterService {

	NovelChapter load(Integer id) throws Exception;
	
    List<NovelChapter> getByArticleId(String articleId) throws Exception;
    
    List<NovelChapter> findLatest(String id, String lastDate) throws Exception;
	
    Map<String, Object> page(String rows, String curr, String title, String article_directory);
}
