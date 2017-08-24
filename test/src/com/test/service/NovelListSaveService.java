package com.test.service;

import com.test.model.NovelListSave;

public interface NovelListSaveService {

	public NovelListSave findUnique(String id);

	public void save(NovelListSave info);

	public void delete(String id);
	
}
