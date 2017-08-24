package com.test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.dao.NovelChapterSaveDao;
import com.test.model.NovelChapterSave;
import com.test.service.NovelChapterSaveService;

@Service("novelChapterSaveService")
public class NovelChapterSaveServiceImpl implements NovelChapterSaveService{

	@Autowired
    private NovelChapterSaveDao novelChapterSaveDao;
	
	@Override
    @Transactional
	public NovelChapterSave findUnique(String id) {
		String hql = "from NovelChapterSave x where x.article_id = ?";
        List<NovelChapterSave> list = novelChapterSaveDao.find(hql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
	}

	@Override
    @Transactional
	public void save(NovelChapterSave info) {
		novelChapterSaveDao.saveOrUpdate(info);
	}

	@Override
    @Transactional
	public void delete(String id) {
		List<NovelChapterSave> list = novelChapterSaveDao.find("from NovelChapterSave as j where  j.id = ?", id);
        if (list != null && list.size() > 0) {
        	NovelChapterSave j = list.get(0);
        	j.setId(id);
        	novelChapterSaveDao.delete(j);
        }
	}
}
