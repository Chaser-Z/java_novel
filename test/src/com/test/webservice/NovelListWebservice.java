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

import com.test.model.NovelList;
import com.test.service.NovelListService;
import com.test.utils.JsonMapper;
import com.test.utils.ObjectMapper;
import com.test.webservice.constants.ErrorCode;
import com.test.webservice.dto.NovelListDTO;
import com.test.webservice.dto.BaseDTO;
import com.test.webservice.param.ArticleSearchParam;


@Controller
@RequestMapping(value = "/article")
public class NovelListWebservice {
	
	private static Logger logger = Logger.getLogger(NovelListWebservice.class);
	
	@Autowired
	private NovelListService novelListService;
	
	// Get Hot Article
	@RequestMapping(value = "/getHot", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String getHot(@RequestBody BaseDTO baseDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<NovelList> novelLists = novelListService.getHot();
			map.put("data", convert(novelLists));
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
	public @ResponseBody String getArticleByType(@RequestBody NovelListDTO articleHotDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			List<NovelList> novelLists = novelListService.getArticleByType(articleHotDTO.getArticle_type());
			map.put("data", convert(novelLists));
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
	public @ResponseBody String getHomeArticle(@RequestBody NovelListDTO articleHotDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			List<NovelList> novelLists = novelListService.getHome();
			map.put("data", convert(novelLists));
            map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
			
		} catch(Exception e) {
			map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
			logger.error("Failed to get type Article", e);
		}
		
		JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
	}
	
	// Get Article by keyword
	@RequestMapping(value = "/getArticleByKeyword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String getArticleByKeyword(@RequestBody ArticleSearchParam param) {
		System.out.println("getArticleByKeyword");
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
            if (param != null) {
                String keyword = param.getKeyword();
                if (keyword != null && keyword.length() > 0) {
                    List<NovelList> articles = novelListService.getArticleByKeyword(keyword);
                    map.put("data", convert(articles));
                    map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
                } else {
                    map.put(ErrorCode.KEY, ErrorCode.ARTICLE_KEYWORD_INVALID);
                }
            } else {
                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
            }
        } catch (Exception e) {
            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
            logger.error("Failed to get search articles by keyword", e);
        }
		
		JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
	}
	
	
	private static NovelListDTO convert(NovelList articleHot) {
		if (articleHot == null) {
			return null;
		}
		NovelListDTO result = ObjectMapper.map(articleHot, NovelListDTO.class);
		//result.setArticle_id(articleHot.getId());
		return result;
	}
	private static List<NovelListDTO> convert(List<NovelList> articleHot) {
		List<NovelListDTO> results = new ArrayList<NovelListDTO>();
		for (NovelList hot : articleHot) {
			NovelListDTO dto = convert(hot);
			if (dto != null) {
				results.add(dto);
			}
			
		}
		return results;
	}
	
	
	

}
