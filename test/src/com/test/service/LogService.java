package com.test.service;

import java.util.Map;

import com.test.model.manager.Log;

/**
 * @author Victory
 * 
 */
public interface LogService {

	public void save(Log info) throws Exception;

	public void insert(Integer type, String userId, String content) ;

	public void update(Log info) throws Exception;

	public void delete(String id) throws Exception;

	public Log load(String id) throws Exception;

	public Map<String, Object> page(String rows, String current, String username, String content);

}
