package com.test.service;

import java.util.List;
import java.util.Map;

import com.miger.commons.dto.Page;
import com.test.model.manager.User;

public interface UserService {

	public void save(User info) throws Exception;

	public void update(User info) throws Exception;

	public void delete(String id) throws Exception;

	public User load(String id) throws Exception;

	public User loadByName(String username) throws Exception;

	public User loadByEmail(String email) throws Exception;

	public User valid(String username, String password) throws Exception;

	public User validEmailAndPassword(String email, String password) throws Exception;

	public List<User> getAllList() throws Exception;

	public List<User> getList(String name, String userId);

	public int pageCount(String name);

	public List<User> pageList(String name, Page<User> page);

	public Map<String, Object> page(String rows, String current, String name);

}
