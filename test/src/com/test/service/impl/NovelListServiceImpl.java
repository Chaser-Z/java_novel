package com.test.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.NovelListDao;
import com.test.model.NovelList;
import com.test.service.NovelListService;

@Service("novelListService")
public class NovelListServiceImpl  implements NovelListService{
	
	@Autowired
	private NovelListDao novelListDao;
	
	@Override
	public NovelList load(String id) throws Exception {
		
		NovelList articleHot = new NovelList();
		List<NovelList> list = novelListDao.find("from NovelList a where a.article_id = ?", id);
		articleHot = list.size() > 0 ? list.get(0) : null;
		return articleHot;
	}
	
	@Override
	public List<NovelList> getHot() throws Exception {
		
		List<NovelList> list = novelListDao.find("from NovelList a");
		if (list == null) {
			return new  ArrayList<NovelList>();
		} else {
			return list;
		}
		
	}
	
	@Override
	public List<NovelList> getArticleByType(String type) throws Exception {
		
		if (type == null || type.length() == 0) {
			return new ArrayList<NovelList>();
		}
		return novelListDao.find("from NovelList sa where sa.article_type = ?", type);
	}

	@Override
	public List<NovelList> getHome() throws Exception {
		List<NovelList> list = novelListDao.getHome();
		if (list == null) {
			return new  ArrayList<NovelList>();
		} else {
			return list;
		}
	}
	
	@Override
	public List<NovelList> getArticleByKeyword(String keyword) throws Exception {
		List<NovelList> list = novelListDao.getArticleByKeyword(keyword);
		if (list == null) {
			return new  ArrayList<NovelList>();
		} else {
			return list;
		}
	}
	
	@Override
	public Map<String, Object> page(String rows, String current) {
		StringBuffer sb = new StringBuffer("from NovelList as model");
		return novelListDao.getMapPage(rows, current, sb.toString());
	}


}
