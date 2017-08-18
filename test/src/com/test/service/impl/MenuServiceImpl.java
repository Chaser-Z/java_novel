package com.test.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.miger.commons.dto.Page;
import com.miger.commons.utils.StringUtil;
import com.test.dao.MenuDao;
import com.test.model.manager.Menu;
import com.test.service.MenuService;

@Service("menuService")
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuDao dao;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 根据用户ID获取权限菜单ID
	 * 
	 * @param userid
	 * @return
	 */
	private String getLimitIdsByUserid(String userid) {
		StringBuffer sb = new StringBuffer();
		sb.append("select ul.limitIds from m_user u,m_limit ul where u.limitsId=ul.id and u.id ='" + userid + "'");
		String str = (String) jdbcTemplate.queryForObject(sb.toString(), String.class);
		return "'" + str.replace(",", "','") + "'";
	}

	public void save(Menu info) throws Exception {
		if (info.getId() != null)
			dao.update(info);
		else
			dao.save(info);
	}

	public void update(Menu info) throws Exception {
		dao.update(info);
	}

	public void delete(String id) throws Exception {
		Menu info = load(id);
		if (info != null) {
			info.setStatus(2);
			dao.update(info);
		}
	}

	public Menu load(String id) throws Exception {
		return dao.get(id);
	}

	public List<Menu> getAllList() throws Exception {
		return dao.loadAll();
	}

	public List<Menu> getLimitsByUserid(String userid) {
		String limitIds = getLimitIdsByUserid(userid);
		StringBuffer sb = new StringBuffer("from Menu as model where model.status = 1");
		sb.append(" and model.id in (").append(limitIds).append(")");
		sb.append(" order by model.sequence asc");
		return dao.find(sb.toString());
	}

	public List<Menu> pageList(String pid, Page<Menu> page) {
		StringBuffer sb = new StringBuffer("from Menu as model where model.status = 1");
		if (!StringUtil.isNull(pid)) {
			sb.append(" and model.pid = '").append(pid).append("'");
		} else {
			sb.append(" and ( model.pid is null or model.pid='' )");
		}
		sb.append(" order by model.sequence asc");
		return dao.getPageList(sb.toString(), page);
	}

	public Map<String, Object> page(String rows, String current, String pid) {
		StringBuffer sb = new StringBuffer("from Menu as model where model.status = 1");
		if (!StringUtil.isNull(pid)) {
			sb.append(" and model.pid = '").append(pid).append("'");
		} else {
			sb.append(" and ( model.pid is null or model.pid='' )");
		}
		sb.append(" order by model.sequence asc");
		return dao.getMapPage(rows, current, sb.toString());
	}

}

