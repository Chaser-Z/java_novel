package com.test.service;

import java.util.Map;

import com.test.model.NovelUser;


public interface NovelUserService {

    void save(NovelUser user) throws Exception;

    NovelUser update(NovelUser user, boolean force) throws Exception;
    
    void updateCredential(NovelUser user) throws Exception;

    NovelUser load(String id) throws Exception;

    NovelUser getByIdentity(String identityType, String identifier) throws Exception;

	Map<String, Object> page(String rows, String curr, String name, String identityType);

	Map<String, Object> statistics();
}
