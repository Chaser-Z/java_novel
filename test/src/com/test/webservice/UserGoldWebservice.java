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

import com.test.model.UserGold;
import com.test.service.UserGoldService;
import com.test.utils.JsonMapper;
import com.test.utils.ObjectMapper;
import com.test.webservice.constants.ErrorCode;
import com.test.webservice.dto.UserGoldDTO;

@Controller
@RequestMapping(value = "/usergold")
public class UserGoldWebservice {

	private static Logger logger = Logger.getLogger(UserGoldWebservice.class);

	@Autowired
	//private UserGoldService userGoldService;
	
//	// 用户每日任务签到增加虚拟币
//	@RequestMapping(value = "/getHot", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	public @ResponseBody String signIn(@RequestBody UserGoldDTO useGoldDTO) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		
//		System.out.println("---------");
//		
//		try {
//            if (useGoldDTO != null) {
//                List<UserGold> list = userGoldService.getByArticleId(UserGoldDTO.getArticle_id());
//                map.put("data", convertUserGold(list));
//                map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
//            } else {
//                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
//            }
//        } catch (Exception e) {
//            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
//            logger.error("Failed to special articles by special id", e);
//        }
//
//        JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
//        return mapper.toJson(map);
//		
//		
//	}
	
	
	private static List<UserGoldDTO> convertUserGold(List<UserGold> sas) {
        List<UserGoldDTO> results = new ArrayList<UserGoldDTO>();
        for (UserGold sa : sas) {
        	UserGoldDTO dto = convertUserGold(sa);
            if (dto != null) {
                results.add(dto);
            }
        }
        return results;
    }

	private static UserGoldDTO convertUserGold(UserGold sa) {
        if (sa == null) {
            return null;
        }
        
        UserGoldDTO result = ObjectMapper.map(sa, UserGoldDTO.class);
        //result.setArticle_id(sa.getId());
        return result;
    }
}
