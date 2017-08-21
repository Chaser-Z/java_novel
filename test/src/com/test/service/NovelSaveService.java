package com.test.service;

import com.test.model.NovelSave;

public interface NovelSaveService {

	public NovelSave findUnique(String id);

	public void save(NovelSave info);

	public void delete(String id);
	
}
