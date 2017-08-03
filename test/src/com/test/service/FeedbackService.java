package com.test.service;

import java.util.Map;

import com.test.model.Feedback;


public interface FeedbackService {

    void save(Feedback feedback) throws Exception;

	Map<String, Object> page(String rows, String curr, String content);

}