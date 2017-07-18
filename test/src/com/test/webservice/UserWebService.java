package com.test.webservice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.model.NovelUser;
import com.test.model.Session;
import com.test.service.NovelUserService;
import com.test.service.SessionService;
import com.test.utils.JsonMapper;
import com.test.utils.ObjectMapper;
import com.test.utils.Util;
import com.test.webservice.constants.ErrorCode;
import com.test.webservice.constants.IdentityTypes;
import com.test.webservice.constants.UserStatus;
import com.test.webservice.constants.UserVerified;
import com.test.webservice.dto.BaseDTO;
import com.test.webservice.dto.NovelUserDTO;
import com.test.webservice.param.LoginParam;
import com.test.webservice.param.RegisterParam;
import com.vdurmont.emoji.EmojiParser;


@Controller
@RequestMapping(value = "/user")
public class UserWebService {

	private static Logger logger = Logger.getLogger(UserWebService.class);
	
	@Autowired
    private NovelUserService novelUserService;
	
	@Autowired
    private SessionService sessionService;
	
	
	// Register
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String register(@RequestBody RegisterParam param) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            if (param != null) {
                // Extract user info
            	NovelUser user = param.getUser();
                user.setVerified(UserVerified.VERIFIED);
                user.setCreateTime(Util.getCurrentUnixTime());
                user.setLastUpdateTime(Util.getCurrentUnixTime());
                user.setExpiresIn(0);
                user.setIsDel(0);
                user.setStatus(UserStatus.ACTIVE);

                NovelUser existing = novelUserService.getByIdentity(user.getIdentityType(), user.getIdentifier());
                if (existing == null) {
                    // Save user
                	novelUserService.save(user);
                    
                    // Add Session
                    Session session = param.getSession();
                    session.setUserId(user.getId());
                    session.setLoginDate(new Date().getTime());
                    session.setStatus(1);
                    session.setIsDel(0);

                    // Delete existing session by user id and unique id
                    sessionService.deleteByUserId(user.getId());

                    // Save current session
                    sessionService.save(session);

                    // Create return value
                    NovelUserDTO result = convert(user);
                    result.setSessionId(session.getId());

                    map.put("data", result);
                    map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
                } else {
                    map.put(ErrorCode.KEY, ErrorCode.USER_IDENTITY_EXISTING);
                }
            } else {
                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
            }
        } catch (Exception e) {
            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
            logger.error("Failed to register", e);
        }

        JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
    }

    // Login
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String login(@RequestBody LoginParam param) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            if (param != null) {
            	NovelUser user = param.getUser();

            	NovelUser existing = novelUserService.getByIdentity(user.getIdentityType(), user.getIdentifier());
                boolean success = false;
                
                // Email user
                if (user.getIdentityType().equals(IdentityTypes.EMAIL)) {
                    if (existing == null) { // No user found
                        map.put(ErrorCode.KEY, ErrorCode.USER_NOT_EXIST);
                    } else if (user.getCredential().equals(existing.getCredential())) { // User exists and credential matches
                        user = existing;
                        success = true;
                    } else {
                        map.put(ErrorCode.KEY, ErrorCode.USER_CREDENTIAL_INCORRECT);
                    }
                } else { // Third party user
                    success = true;
                    
                    if (existing == null) { // First login
                        // Save user
                        user.setVerified(UserVerified.VERIFIED);
                        user.setCreateTime(Util.getCurrentUnixTime());
                        user.setLastUpdateTime(Util.getCurrentUnixTime());
                        user.setExpiresIn(0);
                        user.setIsDel(0);
                        user.setStatus(UserStatus.ACTIVE);
                        novelUserService.save(user);
                    } else {
                        // Update existing user
                        user.setId(existing.getId());
                        user.setLastUpdateTime(Util.getCurrentUnixTime());
                        user.setVerified(UserVerified.VERIFIED);
                        user.setExpiresIn(0);
                        NovelUser result = novelUserService.update(user, false);
                        user = result;
                    }
                }
                
                if (success) {
                    Session session = param.getSession();
                    session.setUserId(user.getId());
                    session.setLoginDate(new Date().getTime());
                    session.setStatus(1);
                    session.setIsDel(0);

                    // Delete existing session by user id and unique id
                    sessionService.deleteByUserId(user.getId());

                    // Save current session
                    sessionService.save(session);

                    // Create return value
                    NovelUserDTO result = convert(user);
                    result.setSessionId(session.getId());

                    map.put("data", result);
                    map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
                }
            } else {
                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
            }
        } catch (Exception e) {
            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
            logger.error("Failed to login", e);
        }

        JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
    }

    // Logout
    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String logout(@RequestBody BaseDTO param) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            if (param != null) {
                // Check if session is valid
                String userId = param.getUserId();
                String sessionId = param.getSessionId();
                if (sessionService.check(userId, sessionId)) {
                    sessionService.deleteByUserId(userId);
                    map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
                } else {
                    map.put(ErrorCode.KEY, ErrorCode.INVALID_SESSION);
                }
            } else {
                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
            }
        } catch (Exception e) {
            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
            logger.error("Failed to logout", e);
        }

        JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
    }

    // Check Session
    @RequestMapping(value = "/checkSession", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String checkSession(@RequestBody BaseDTO param) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            if (param != null) {
                String userId = param.getUserId();
                String sessionId = param.getSessionId();
                boolean result = sessionService.check(userId, sessionId);
                map.put(ErrorCode.KEY, result ? ErrorCode.SUCCESS : ErrorCode.INVALID_SESSION);
            } else {
                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
            }
        } catch (Exception e) {
            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
            logger.error("Failed to logout", e);
        }

        JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
    }
    
    
    private static NovelUserDTO convert(NovelUser user) {
        if (user == null) {
            return null;
        }
        
        NovelUserDTO result = ObjectMapper.map(user, NovelUserDTO.class);
        result.setUserId(user.getId());
        if (user.getIdentityType().equals(IdentityTypes.EMAIL)) {
            result.setCredential(null);
        }
        
        if (result != null) {
            emojiAliasToUnicode(result);
        }
        
        return result;
    }

    private static NovelUser convert(NovelUserDTO dto) {
        if (dto == null) {
            return null;
        }
        
        NovelUser user = ObjectMapper.map(dto, NovelUser.class);
        user.setId(dto.getUserId());
        user.setGender(dto.getGender() == null ? 0 : dto.getGender());
        return user;
    }
    
    private static void emojiAliasToUnicode(NovelUserDTO user) {
        if (user != null) {
            if (user.getNickname() != null) {
                String nickname = EmojiParser.parseToUnicode(user.getNickname());
                user.setNickname(nickname);
            }

            if (user.getUsername() != null) {
                String username = EmojiParser.parseToUnicode(user.getUsername());
                user.setUsername(username);
            }

            if (user.getDescription() != null) {
                String description = EmojiParser.parseToUnicode(user.getDescription());
                user.setDescription(description);
            }
        }
    }
    
}
