package com.test.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miger.commons.utils.StringUtil;
import com.test.dao.LogDao;
import com.test.model.manager.Log;
import com.test.service.LogService;

@Service("logService")
public class LogServiceImpl implements LogService {

	@Autowired
	private LogDao dao;

	@Autowired
	private HttpServletRequest request;

	public void save(Log info) throws Exception {
		if (info.getId() != null)
			dao.update(info);
		else
			dao.save(info);
	}

	/**
	 * 获取访问IP
	 * 
	 * @param request
	 * @return
	 */
	private String getRemortIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}

	public void insert(Integer type, String userId, String content) {
		Log info = new Log();
		info.setType(type);
		info.setIp(getRemortIP(request));
		info.setUserId(userId);
		info.setContent(content);
		dao.save(info);
	}

	public void update(Log info) throws Exception {
		dao.update(info);
	}

	public void delete(String id) throws Exception {
		dao.delete(load(id));
	}

	public Log load(String id) throws Exception {
		return dao.get(id);
	}

	public Map<String, Object> page(String rows, String current, String username, String content) {
		StringBuffer sb = new StringBuffer("from Log as model where 1 = 1 ");
		if (!StringUtil.isNull(username)) {
			sb.append(" and model.userName like '%").append(username).append("%'");
		}
		if (!StringUtil.isNull(content)) {
			sb.append(" and model.content like '%").append(content).append("%'");
		}
		sb.append(" order by model.createdTime desc");
		return dao.getMapPage(rows, current, sb.toString());
	}

}
