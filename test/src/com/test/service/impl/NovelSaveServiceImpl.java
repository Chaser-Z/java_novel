package com.test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.dao.NovelSaveDao;
import com.test.model.NovelSave;
import com.test.service.NovelSaveService;

@Service("novelSaveService")
public class NovelSaveServiceImpl implements NovelSaveService{
	
	@Autowired
    private NovelSaveDao novelSaveDao;

    @Override
    @Transactional
    public NovelSave findUnique(String id) {
        String hql = "from NovelSave x where x.article_id = ?";
        List<NovelSave> list = novelSaveDao.find(hql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    public void save(NovelSave info) {
    	novelSaveDao.saveOrUpdate(info);
    }

    @Override
    @Transactional
    public void delete(String id) {
        List<NovelSave> list = novelSaveDao.find("from NovelSave as j where  j.article_id = ?", id);
        if (list != null && list.size() > 0) {
        	NovelSave j = list.get(0);
        	j.setArticle_id(id);
            //j.setIsDel(1);
        	novelSaveDao.delete(j);
            //novelSaveDao.update(j);
        }
    }

}
