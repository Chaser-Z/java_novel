package com.test.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.model.ArticleHot;
import com.test.service.ArticleHotService;
import com.test.utils.JsonMapper;
import com.test.utils.ObjectMapper;
import com.test.webservice.constants.ErrorCode;
import com.test.webservice.dto.ArticleHotDTO;
import com.test.webservice.dto.BaseDTO;


@Controller
@RequestMapping(value = "/article")
public class ArticleHotWebservice {
	
	private static Logger logger = Logger.getLogger(ArticleHotWebservice.class);
	
	@Autowired
	private ArticleHotService articleHotService;
	
	// Get Hot Article
	@RequestMapping(value = "/getHot", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String getHot(@RequestBody BaseDTO baseDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<ArticleHot> articleHots = articleHotService.getHot();
			map.put("data", convert(articleHots));
            map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
			
		} catch(Exception e) {
			map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
			logger.error("Failed to get hot", e);
		}
		JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
		return mapper.toJson(map);
	}
	
	// Get type Article
	@RequestMapping(value = "/getArticleByType", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String getArticleByType(@RequestBody ArticleHotDTO articleHotDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			List<ArticleHot> articleHots = articleHotService.getArticleByType(articleHotDTO.getArticle_type());
			map.put("data", convert(articleHots));
            map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
			
		} catch(Exception e) {
			map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
			logger.error("Failed to get type Article", e);
		}
		
		JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
	}
	
	// Get type Article
	@RequestMapping(value = "/getHomeArticle", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String getHomeArticle(@RequestBody ArticleHotDTO articleHotDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			List<ArticleHot> articleHots = articleHotService.getHome();
			map.put("data", convert(articleHots));
            map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
			
		} catch(Exception e) {
			map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
			logger.error("Failed to get type Article", e);
		}
		
		JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
	}
	
	
	private static ArticleHotDTO convert(ArticleHot articleHot) {
		if (articleHot == null) {
			return null;
		}
		ArticleHotDTO result = ObjectMapper.map(articleHot, ArticleHotDTO.class);
		//result.setArticle_id(articleHot.getId());
		return result;
	}
	private static List<ArticleHotDTO> convert(List<ArticleHot> articleHot) {
		List<ArticleHotDTO> results = new ArrayList<ArticleHotDTO>();
		for (ArticleHot hot : articleHot) {
			ArticleHotDTO dto = convert(hot);
			if (dto != null) {
				results.add(dto);
			}
			
		}
		return results;
	}
	
	
	

}
