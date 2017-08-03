package com.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.FeedbackDao;
import com.test.model.Feedback;

@Repository("feedbackDao")
public class FeedbackDaoImpl extends SimpleDaoImpl<Feedback, Integer> implements FeedbackDao {

}