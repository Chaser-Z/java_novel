package com.test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.dao.NovelListSaveDao;
import com.test.model.NovelListSave;
import com.test.service.NovelListSaveService;

@Service("novelListSaveService")
public class NovelListSaveServiceImpl implements NovelListSaveService{
	
	@Autowired
    private NovelListSaveDao novelSaveDao;

    @Override
    @Transactional
    public NovelListSave findUnique(String id) {
        String hql = "from NovelListSave x where x.article_id = ?";
        List<NovelListSave> list = novelSaveDao.find(hql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    public void save(NovelListSave info) {
    	novelSaveDao.saveOrUpdate(info);
    }

    @Override
    @Transactional
    public void delete(String id) {
        List<NovelListSave> list = novelSaveDao.find("from NovelListSave as j where  j.article_id = ?", id);
        if (list != null && list.size() > 0) {
        	NovelListSave j = list.get(0);
        	j.setArticle_id(id);
            //j.setIsDel(1);
        	novelSaveDao.delete(j);
            //novelSaveDao.update(j);
        }
    }

}
