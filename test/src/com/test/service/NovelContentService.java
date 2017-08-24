package com.test.service;

import java.util.List;

import com.test.model.NovelContent;

public interface NovelContentService {

	NovelContent getbyDirectoryLink(Integer id) throws Exception;
	
	List<NovelContent> getContentsById(String article_id, Integer id) throws Exception;
}
