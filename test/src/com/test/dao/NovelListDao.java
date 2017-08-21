package com.test.dao;

import java.util.List;

import com.miger.commons.hibernate.SimpleDao;
import com.test.model.NovelList;

public interface NovelListDao extends SimpleDao<NovelList, String>{

	List<NovelList> getHome();
	
	List<NovelList> getArticleByKeyword(String keyword);

}
