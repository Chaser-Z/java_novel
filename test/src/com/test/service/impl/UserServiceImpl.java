package com.test.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miger.commons.dto.Page;
import com.miger.commons.utils.Md5Util;
import com.miger.commons.utils.StringUtil;
import com.test.dao.UserDao;
import com.test.model.manager.User;
import com.test.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao dao;

	public void save(User info) throws Exception {
		if (info.getId() != null)
			dao.update(info);
		else
			dao.save(info);
	}

	public void update(User info) throws Exception {
		dao.update(info);
	}

	public void delete(String id) throws Exception {
		User info = load(id);
		if (info != null) {
			info.setStatus(2);
			dao.update(info);
		}
	}

	public User load(String id) throws Exception {
		return dao.get(id);
	}

	public List<User> getAllList() throws Exception {
		return dao.loadAll();
	}

	public List<User> getList(String name, String userId) {
		StringBuffer sb = new StringBuffer("from User as model where model.status = 1 ");
		if (!StringUtil.isNull(name)) {
			sb.append(" and model.name like '%").append(name).append("%'");
		}
		if (!StringUtil.isNull(userId)) {
			sb.append(" and model.id = ").append(userId);
		}
		sb.append(" order by model.createdTime desc");
		return dao.find(sb.toString());
	}

	public User loadByName(String username) throws Exception {
		if (StringUtil.isNull(username)) {
			return null;
		}
		StringBuffer sb = new StringBuffer("from User as model where model.status = 1 ");
		if (!StringUtil.isNull(username)) {
			sb.append(" and model.name = '").append(username).append("'");
		}
		if (dao.find(sb.toString()) != null && dao.find(sb.toString()).size() > 0)
			return dao.find(sb.toString()).get(0);
		return null;
	}

	public User loadByEmail(String email) throws Exception {
		if (StringUtil.isNull(email)) {
			return null;
		}
		StringBuffer sb = new StringBuffer("from User as model where model.status = 1 ");
		if (!StringUtil.isNull(email)) {
			sb.append(" and model.email = '").append(email).append("'");
		}
		if (dao.find(sb.toString()).size() > 0)
			return dao.find(sb.toString()).get(0);
		return null;
	}

	public User valid(String username, String password) throws Exception {
		User info = loadByName(username);
		if (info != null && !StringUtil.isNull(info.getPassword()) && !StringUtil.isNull(password)) {
			if (Md5Util.checkpassword(password, info.getPassword())) {
				info.setLastTime(new Date());
				if (info.getFrequency() != null) {
					info.setFrequency(info.getFrequency() + 1);
				} else {
					info.setFrequency(1);
				}
				dao.update(info);
				return info;
			}
		}
		return null;
	}

	public User validEmailAndPassword(String email, String password) throws Exception {
		User info = loadByEmail(email);
		if (info != null && !StringUtil.isNull(info.getPassword()) && !StringUtil.isNull(password)) {
			if (Md5Util.checkpassword(password, info.getPassword())) {
				info.setLastTime(new Date());
				if (info.getFrequency() != null) {
					info.setFrequency(info.getFrequency() + 1);
				} else {
					info.setFrequency(1);
				}
				dao.update(info);
				return info;
			}
		}
		return null;
	}

	public int pageCount(String name) {
		StringBuffer sb = new StringBuffer("select count(*) from User as model where model.status = 1");
		if (!StringUtil.isNull(name)) {
			sb.append(" and model.name like '%").append(name).append("%'");
		}
		return dao.getPageCount(sb.toString());
	}

	public List<User> pageList(String name, Page<User> page) {
		StringBuffer sb = new StringBuffer("from User as model where model.status = 1 ");
		if (!StringUtil.isNull(name)) {
			sb.append(" and model.name like '%").append(name).append("%'");
		}
		sb.append(" order by model.createdTime desc");
		return dao.getPageList(sb.toString(), page);
	}

	public Map<String, Object> page(String rows, String current, String name) {
		StringBuffer sb = new StringBuffer("from User as model where model.status = 1 ");
		if (!StringUtil.isNull(name)) {
			sb.append(" and model.name like '%").append(name).append("%'");
		}
		sb.append(" order by model.createdTime desc");
		return dao.getMapPage(rows, current, sb.toString());
	}

	public List<User> getUserListByCode(String usercodes) {
		if (StringUtils.isBlank(usercodes)) {
			return null;
		}
		StringBuffer sb = new StringBuffer("from User as model where model.status = 1 ");
		if (!StringUtil.isNull(usercodes)) {
			sb.append(" and model.code in (").append(usercodes).append(")");
		}
		sb.append(" order by model.createdTime desc");
		return dao.find(sb.toString());
	}

}