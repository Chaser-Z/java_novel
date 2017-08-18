package com.test.service;

import java.util.List;
import java.util.Map;

import com.miger.commons.dto.Page;
import com.test.model.manager.Menu;

public interface MenuService {

	public void save(Menu info) throws Exception;

	public void update(Menu info) throws Exception;

	public void delete(String id) throws Exception;

	public Menu load(String id) throws Exception;

	public List<Menu> getAllList() throws Exception;

	public List<Menu> getLimitsByUserid(String userid);

	public List<Menu> pageList(String pid, Page<Menu> page);

	public Map<String, Object> page(String rows, String current, String pid);

}
