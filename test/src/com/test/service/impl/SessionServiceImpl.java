package com.test.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.SessionDao;
import com.test.model.Session;
import com.test.service.SessionService;



@Service("sessionService")
public class SessionServiceImpl implements SessionService {

    private static final Logger logger = Logger.getLogger(SessionService.class);

    @Autowired
    private SessionDao sessionDao;

    @Override
    public void save(Session session) throws Exception {
        if (session == null) {
            return;
        }

        if (session.getId() != null) {
            Session current = load(session.getId());
            if (current != null) {
                copySession(session, current);
                sessionDao.update(current);
            } else {
                sessionDao.save(session);
            }
        } else {
            sessionDao.save(session);
        }
    }

    @Override
    public void update(Session session) throws Exception {
        if (session == null) {
            return;
        }

        if (session.getId() != null) {
            Session current = load(session.getId());
            copySession(session, current);
            sessionDao.update(current);
        } else {
            logger.warn("Could not update session, session id is null");
        }
    }

    @Override
    public void delete(Session session) throws Exception {
        if (session == null) {
            return;
        }

        session.setIsDel(1);
        update(session);
    }

    @Override
    public void deleteByUserId(String userId) throws Exception {
        if (userId == null || userId.length() == 0) {
            return;
        }

        List<Session> list = sessionDao.find("from Session as s where s.isDel = 0 and s.userId = ?", userId);
        if (list != null && list.size() > 0) {
            for (Session session : list) {
                session.setIsDel(1);
                sessionDao.update(session);
            }
        }
    }

    @Override
    public Session load(String id) throws Exception {
        if (id == null || id.length() == 0) {
            return null;
        }

        List<Session> list = sessionDao.find("from Session as s where s.isDel = 0 and s.id = ?", id);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<Session> getByUserId(String userId) throws Exception {
        if (userId == null || userId.length() == 0) {
            return new ArrayList<Session>();
        }

        return sessionDao.find("from Session as s where s.isDel = 0 and s.userId = ?", userId);
    }
    
    @Override
    public boolean check(String userId, String sessionId) throws Exception {
        boolean result = false;
        Session session = load(sessionId);
        if (session != null && session.getUserId().equals(userId)) {
            result = true;
        }

        return result;
    }

    private static void copySession(Session source, Session target) {
        target.setUserId(source.getUserId());
        target.setLoginDate(source.getLoginDate());
        target.setOsType(source.getOsType());
        target.setBrand(source.getBrand());
        target.setModel(source.getModel());
        target.setSdkVersion(source.getSdkVersion());
        target.setReleaseVersion(source.getReleaseVersion());
        target.setDeviceToken(source.getDeviceToken());
        target.setUniqueId(source.getUniqueId());
        target.setStatus(source.getStatus());
        target.setIsDel(source.getIsDel());
    }

}
