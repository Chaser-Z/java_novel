package com.test.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.model.NovelChapter;
import com.test.service.NovelChapterService;
import com.test.utils.JsonMapper;
import com.test.utils.ObjectMapper;
import com.test.webservice.constants.ErrorCode;
import com.test.webservice.dto.NovelListDTO;
import com.test.webservice.dto.NovelChapterDTO;

@Controller
@RequestMapping(value = "/articleInfo")
public class NovelChapterWebservice {
	
	private static Logger logger = Logger.getLogger(NovelChapterWebservice.class);
	
	@Autowired
	private NovelChapterService novelChapterService;
	
	
	@RequestMapping(value = "getLatestArticles", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String getLatestArticles(@RequestBody NovelChapterDTO articleinfoDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("--++++++++---");

		try {
			String lastDate = articleinfoDTO.getLast_update_date();
			String id =  articleinfoDTO.getArticle_id();
			List<NovelChapter> articleInfos = new ArrayList<NovelChapter>();
			
			if (lastDate != null || id != null) {
				articleInfos = novelChapterService.findLatest(id, lastDate);
			}
			List<NovelChapterDTO> articleInfoDTos = new ArrayList<>();
			if(articleInfos != null && articleInfos.size() > 0){
				for(NovelChapter ai : articleInfos){
					NovelChapterDTO a = new NovelChapterDTO();
					BeanUtils.copyProperties(ai, a);
					articleInfoDTos.add(a);
				}
				
			}
			map.put("data", articleInfoDTos);
            map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
			
		} catch (Exception e) {
			 map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
	            logger.error("Failed to getLatestArticles by id", e);
		}
		JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
	}
	
	
	@RequestMapping(value = "getArticlesByArticleId", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String getArticlesBySpecialId(@RequestBody NovelListDTO articleHotDTO) {
		System.out.println("---------");
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
            if (articleHotDTO != null) {
                List<NovelChapter> list = novelChapterService.getByArticleId(articleHotDTO.getArticle_id());
                map.put("data", convertArticleInfo(list));
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
	
	
	private static List<NovelChapterDTO> convertArticleInfo(List<NovelChapter> sas) {
        List<NovelChapterDTO> results = new ArrayList<NovelChapterDTO>();
        for (NovelChapter sa : sas) {
        	NovelChapterDTO dto = convertArticleInfo(sa);
            if (dto != null) {
                results.add(dto);
            }
        }
        return results;
    }

	private static NovelChapterDTO convertArticleInfo(NovelChapter sa) {
        if (sa == null) {
            return null;
        }
        
        NovelChapterDTO result = ObjectMapper.map(sa, NovelChapterDTO.class);
        //result.setArticle_id(sa.getId());
        return result;
    }


}
