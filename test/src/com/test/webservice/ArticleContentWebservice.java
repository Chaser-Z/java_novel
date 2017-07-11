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

import com.test.model.ArticleContent;
import com.test.service.ArticleContentService;
import com.test.utils.JsonMapper;
import com.test.utils.ObjectMapper;
import com.test.webservice.constants.ErrorCode;
import com.test.webservice.dto.ArticleContentDTO;

@Controller
@RequestMapping(value = "/articleContent")
public class ArticleContentWebservice {

	private static Logger logger = Logger.getLogger(ArticleContentWebservice.class);

	@Autowired
	private ArticleContentService articleContentService;
	
	@RequestMapping(value = "getArticleByDriectoryLink", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String getArticleByDriectoryLink(@RequestBody ArticleContentDTO articleContentDTO) {
		System.out.println("---------");
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
            if (articleContentDTO != null) {
                ArticleContent articleContent = articleContentService.getbyDirectoryLink(articleContentDTO.getArticle_directory_link());
                map.put("data", convertArticleContent(articleContent));
                map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
            } else {
                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
            }
        } catch (Exception e) {
            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
            logger.error("Failed to special articles by special id", e);
        }

        JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
		
	}
	
	@RequestMapping(value = "getArticleContents", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String getArticleContentsById(@RequestBody ArticleContentDTO articleContentDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
            if (articleContentDTO != null) {
                //ArticleContent articleContent = articleContentService.getbyDirectoryLink(articleContentDTO.getArticle_directory_link());
                List<ArticleContent> list = articleContentService.getContentsById(articleContentDTO.getArticle_id());
                map.put("data", convertArticleContents(list));
                map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
            } else {
                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
            }
        } catch (Exception e) {
            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
            logger.error("Failed to special articles by special id", e);
        }

        JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
	}
	
	private static List<ArticleContentDTO> convertArticleContents(List<ArticleContent> sas) {
        List<ArticleContentDTO> results = new ArrayList<ArticleContentDTO>();
        for (ArticleContent sa : sas) {
        	ArticleContentDTO dto = convertArticle(sa);
            if (dto != null) {
                results.add(dto);
            }
        }
        return results;
    }
	
	
	private static ArticleContentDTO convertArticleContent(ArticleContent sas) {
        ArticleContentDTO results = new ArticleContentDTO();
        ArticleContentDTO dto = convertArticle(sas);
        if (dto != null) {
             results = dto;
        }
        return results;
    }

	private static ArticleContentDTO convertArticle(ArticleContent sa) {
        if (sa == null) {
            return null;
        }
        
        ArticleContentDTO result = ObjectMapper.map(sa, ArticleContentDTO.class);
        //result.setArticle_id(sa.getId());
        return result;
    }
	
}
