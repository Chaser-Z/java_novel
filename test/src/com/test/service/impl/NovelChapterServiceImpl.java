package com.test.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.NovelChapterDao;
import com.test.model.NovelChapter;
import com.test.service.NovelChapterService;

@Service("novelChapterService")
public class NovelChapterServiceImpl implements NovelChapterService{

	
	@Autowired
	private NovelChapterDao novelChapterDao;
	
	@Override
	public NovelChapter load(Integer id) throws Exception {
		
		NovelChapter novelList = new NovelChapter();
		List<NovelChapter> list = novelChapterDao.find("from NovelChapter a where a.id = ?", id);
		novelList = list.size() > 0 ? list.get(0) : null;
		return novelList;
	}
	
	@Override
    public List<NovelChapter> getByArticleId(String articleId) throws Exception {
		if (articleId == null || articleId.length() == 0) {
            return new ArrayList<NovelChapter>();
		}
		//return articleInfoDao.find("from ArticleInfo sa where sa.article_id = ?", articleId);
		return novelChapterDao.getByArticleId(articleId);
	}
	
	@Override
	public List<NovelChapter> findLatest(String id, String lastDate) throws Exception {
		if (id == null || lastDate == null || id.length() == 0 || lastDate.length() == 0) {
			return null;
		}
		return novelChapterDao.findLatest(id, lastDate);
	}
	
	@Override
	public Map<String, Object> page(String rows, String current, String title, String article_directory) {
		//StringBuffer sb = new StringBuffer("from NovelChapter as model");
		
		return novelChapterDao.page(rows, current, title, article_directory);
		//return novelChapterDao.getMapPage(rows, current, title, article_directory, sb.toString());
		//return novelChapterDao.getMapPage(rows, current, sb.toString());
		//return articleDao.getMapPage(rows, curr, pid, programa, journal_Name,articleTitle);
	}
	
}
