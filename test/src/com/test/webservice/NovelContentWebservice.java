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

import com.test.model.NovelContent;
import com.test.service.NovelContentService;
import com.test.utils.JsonMapper;
import com.test.utils.ObjectMapper;
import com.test.webservice.constants.ErrorCode;
import com.test.webservice.dto.NovelContentDTO;

@Controller
@RequestMapping(value = "/articleContent")
public class NovelContentWebservice {

	private static Logger logger = Logger.getLogger(NovelContentWebservice.class);

	@Autowired
	private NovelContentService novelContentService;
	
	@RequestMapping(value = "getArticleByDriectoryLink", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String getArticleByDriectoryLink(@RequestBody NovelContentDTO articleContentDTO) {
		System.out.println("---------");
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
            if (articleContentDTO != null) {
                NovelContent articleContent = novelContentService.getbyDirectoryLink(articleContentDTO.getId());
                map.put("data", convertArticleContent(articleContent));
                map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
            } else {
                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
            }
        } catch (Exception e) {
            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
            logger.error("Failed to getArticleByDriectoryLink", e);
        }

        JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
		
	}
	
	@RequestMapping(value = "getArticleContents", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String getArticleContentsById(@RequestBody NovelContentDTO articleContentDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
            if (articleContentDTO != null) {
                //ArticleContent articleContent = articleContentService.getbyDirectoryLink(articleContentDTO.getArticle_directory_link());
                List<NovelContent> list = novelContentService.getContentsById(articleContentDTO.getArticle_id(), articleContentDTO.getId());
                map.put("data", convertArticleContents(list));
                map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
            } else {
                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
            }
        } catch (Exception e) {
            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
            logger.error("Failed to getArticleContents", e);
        }

        JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
	}
	
	private static List<NovelContentDTO> convertArticleContents(List<NovelContent> sas) {
        List<NovelContentDTO> results = new ArrayList<NovelContentDTO>();
        for (NovelContent sa : sas) {
        	NovelContentDTO dto = convertArticle(sa);
            if (dto != null) {
                results.add(dto);
            }
        }
        return results;
    }
	
	
	private static NovelContentDTO convertArticleContent(NovelContent sas) {
        NovelContentDTO results = new NovelContentDTO();
        NovelContentDTO dto = convertArticle(sas);
        if (dto != null) {
             results = dto;
        }
        return results;
    }

	private static NovelContentDTO convertArticle(NovelContent sa) {
        if (sa == null) {
            return null;
        }
        
        NovelContentDTO result = ObjectMapper.map(sa, NovelContentDTO.class);
        //result.setArticle_id(sa.getId());
        return result;
    }
	
}
