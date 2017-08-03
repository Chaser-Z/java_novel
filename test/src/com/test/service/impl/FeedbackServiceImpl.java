package com.test.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miger.commons.utils.StringUtil;
import com.test.dao.FeedbackDao;
import com.test.model.Feedback;
import com.test.service.FeedbackService;
import com.vdurmont.emoji.EmojiParser;

@Service("feedbackService")
public class FeedbackServiceImpl implements FeedbackService {
    
    private static final Logger logger = Logger.getLogger(FeedbackService.class);

    @Autowired
    private FeedbackDao feedbackDao;

    @Override
    public void save(Feedback feedback) throws Exception {
        if (feedback == null) {
            return;
        }
        
        // Process emoji.
        String content = feedback.getContent();
        if (content != null && content.length() > 0) {
            String target = EmojiParser.parseToAliases(feedback.getContent());
            feedback.setContent(target);
        }

        feedbackDao.save(feedback);
    }

    @Override
    public Map<String, Object> page(String rows, String curr, String content) {
        StringBuffer sb = new StringBuffer("from Feedback as model where 1=1");
        if (!StringUtil.isNull(content)) {
            sb.append(" and model.content like '%").append(content).append("%'");
        }

        sb.append(" order by model.createdTime desc");
        Map<String, Object> result = feedbackDao.getMapPage(rows, curr, sb.toString());

        try {
            Object obj = result.get("rows");
            if (obj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Feedback> list = (List<Feedback>) obj;
                for (Feedback feedback : list) {
                    if (feedback.getContent() != null) {
                        String target = EmojiParser.parseToUnicode(feedback.getContent());
                        feedback.setContent(target);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to process emoji for feedback", e);
        }

        return result;
    }
}
