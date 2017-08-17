package com.test.webservice;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.model.Feedback;
import com.test.service.FeedbackService;
import com.test.utils.JsonMapper;
import com.test.utils.ObjectMapper;
import com.test.utils.Util;
import com.test.webservice.constants.ErrorCode;
import com.test.webservice.dto.FeedbackDTO;

@Controller
@RequestMapping(value = "/feedback")
public class FeedbackWebService {

    private static final Logger logger = Logger.getLogger(FeedbackWebService.class);

    @Autowired
    private FeedbackService feedbackService;
    
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String add(@RequestBody FeedbackDTO dto) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            if (dto != null) {
                Feedback feedback = convert(dto);
                feedbackService.save(feedback);
                map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
            } else {
                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
            }
        } catch (Exception e) {
            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
            logger.error("Failed to add record", e);
        }

        JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
    }

    private static Feedback convert(FeedbackDTO dto) {
        if (dto == null) {
            return null;
        }

        Feedback feedback = ObjectMapper.map(dto, Feedback.class);
        feedback.setCreateTime(Util.getCurrentUnixTime());
        return feedback;
    }

}
