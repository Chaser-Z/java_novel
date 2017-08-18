package com.test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miger.commons.dto.Page;
import com.miger.commons.utils.StringUtil;
import com.test.dao.LimitDao;
import com.test.model.manager.Limit;
import com.test.service.LimitService;

@Service("limitService")
public class LimitServiceImpl implements LimitService {

	@Autowired
	private LimitDao dao;

	public void save(Limit info) throws Exception {
		if (info.getId() != null)
			dao.update(info);
		else
			dao.save(info);
	}

	public void update(Limit info) throws Exception {
		dao.update(info);
	}

	public void delete(String id) throws Exception {
		Limit info = load(id);
		if (info != null) {
			info.setStatus(2);
			dao.update(info);
		}
	}

	public Limit load(String id) throws Exception {
		return dao.get(id);
	}

	public Limit loadLimits(String id) throws Exception {
		Limit info = load(id);
		if (info != null && info.getStatus() == 1) {
			return info;
		}
		return null;
	}

	public List<Limit> getAllList() throws Exception {
		StringBuffer sb = new StringBuffer("from Limit as model where status=1");
		return dao.find(sb.toString());
	}

	public int pageCount(String name) {
		StringBuffer sb = new StringBuffer("select count(*) from Limit as model where 1=1");
		if (!StringUtil.isNull(name)) {
			sb.append(" and model.name like '%").append(name).append("%'");
		}
		sb.append(" and model.status = 1");
		return dao.getPageCount(sb.toString());
	}

	public List<Limit> pageList(String name, Page<Limit> page) {
		StringBuffer sb = new StringBuffer("from Limit as model where 1=1 ");
		if (!StringUtil.isNull(name)) {
			sb.append(" and model.name like '%").append(name).append("%'");
		}
		sb.append(" and model.status = 1 and model.id<>'1'");
		sb.append(" order by model.createdTime desc");
		return dao.getPageList(sb.toString(), page);
	}

}
