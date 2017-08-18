package com.test.service;

import java.util.List;

import com.miger.commons.dto.Page;
import com.test.model.manager.Limit;

public interface LimitService {

	public void save(Limit info) throws Exception;

	public void update(Limit info) throws Exception;

	public void delete(String id) throws Exception;

	public Limit load(String id) throws Exception;

	public Limit loadLimits(String id) throws Exception;

	public List<Limit> getAllList() throws Exception;

	public int pageCount(String name);

	public List<Limit> pageList(String name, Page<Limit> page);

}