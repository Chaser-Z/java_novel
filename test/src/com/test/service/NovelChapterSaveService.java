package com.test.service;

import com.test.model.NovelChapterSave;

public interface NovelChapterSaveService {

	public NovelChapterSave findUnique(String id);

	public void save(NovelChapterSave info);

	public void delete(String id);
}
