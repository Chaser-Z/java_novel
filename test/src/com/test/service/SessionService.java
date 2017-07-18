package com.test.service;

import java.util.List;

import com.test.model.Session;


public interface SessionService {

    void save(Session session) throws Exception;

    void update(Session session) throws Exception;

    void delete(Session session) throws Exception;

    void deleteByUserId(String userId) throws Exception;

    Session load(String id) throws Exception;

    List<Session> getByUserId(String userId) throws Exception;
    
    boolean check(String userId, String sessionId) throws Exception;

}
