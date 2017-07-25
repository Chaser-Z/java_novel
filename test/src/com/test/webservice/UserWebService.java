package com.test.webservice;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.test.model.NovelUser;
import com.test.model.Session;
import com.test.service.NovelUserService;
import com.test.service.SessionService;
import com.test.utils.DigestUtils;
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
	
	 @Autowired
	private JavaMailSenderImpl mailSender;
	
	// Register
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String register(@RequestBody RegisterParam param) {
        Map<String, Object> map = new HashMap<String, Object>();
        System.out.println("注册");
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
    
    // Forget password
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String resetPassword(@RequestBody LoginParam param) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            if (param != null) {
                String identityType = param.getIdentityType();
                String identifier = param.getIdentifier();

                if (identityType != null && identityType.equals(IdentityTypes.EMAIL)) {
                    NovelUser novelUser = novelUserService.getByIdentity(identityType, identifier);
                    if (novelUser != null) {
                        // Generate a new credential
                        String newCredential = generateCredential();
                        novelUser.setCredential(DigestUtils.md5Hex(newCredential));
                        
                        // Save the user
                        novelUserService.updateCredential(novelUser);
                        
                        // Send out the email
                        sendEmail(novelUser, newCredential);

                        // Report success
                        map.put("data", true);
                        map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
                    } else {
                        map.put(ErrorCode.KEY, ErrorCode.USER_NOT_EXIST);
                    }
                } else {
                    map.put(ErrorCode.KEY, ErrorCode.ONLY_FOR_EMAIL_USER);
                }
            } else {
                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
            }
        } catch (Exception e) {
            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
            logger.error("Failed to reset password", e);
        }

        JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
    }
    
    // Generate a password with 8 random characters
    private static String generateCredential() {
        char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer credentialBuilder = new StringBuffer();
        Random r = new Random();
        int pos = -1;
        for (int j = 0; j < 6; j++) {
            pos = Math.abs(r.nextInt(36));
            credentialBuilder.append(str[pos]);
        }

        return credentialBuilder.toString();
    }
    
    // Send out the Email
    private void sendEmail(NovelUser novelUser, String credential) {
        try {
            String from = mailSender.getUsername();
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(novelUser.getIdentifier());
            mailMessage.setSubject("免阅APP重置密码");
            mailMessage.setText("您的新密码是: " + credential + ", please change your password after logon to the App(登录后请更改密码)。");
            mailSender.send(mailMessage);
        } catch (Exception e) {
        	System.out.println(e);
            logger.error("Failed to send reset password email for user: " + novelUser.getId(), e);
        }
    }

    // Change password
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String changePassword(@RequestBody LoginParam param) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            if (param != null) {
                // Check if session is valid
                String userId = param.getUserId();
                String sessionId = param.getSessionId();
                
                if (sessionService.check(userId, sessionId)) {
                    String identityType = param.getIdentityType();
                    String identifier = param.getIdentifier();
                    String credential = param.getCredential();
                    String credentialOld = param.getCredentialOld();

                    NovelUser existing = novelUserService.getByIdentity(identityType, identifier);
                    if (existing != null) {
                        if (existing.getCredential().equals(credentialOld)) {
                            existing.setCredential(credential);
                            novelUserService.updateCredential(existing);
                            map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
                        } else {
                            map.put(ErrorCode.KEY, ErrorCode.USER_OLD_CREDENTIAL_INVALID);
                        }
                    } else {
                        map.put(ErrorCode.KEY, ErrorCode.USER_NOT_EXIST);
                    }
                } else {
                    map.put(ErrorCode.KEY, ErrorCode.INVALID_SESSION);
                }
            } else {
                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
            }
        } catch (Exception e) {
            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
            logger.error("Failed to change password", e);
        }

        JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
    }

    // Update user info
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String update(@RequestBody NovelUserDTO dto) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            if (dto != null) {
                // Check if session is valid
                String userId = dto.getUserId();
                String sessionId = dto.getSessionId();
                
                if (sessionService.check(userId, sessionId)) {
                    NovelUser user = convert(dto);
                    user.setLastUpdateTime(Util.getCurrentUnixTime());
                    novelUserService.update(user, true);
                    
                    NovelUser result = novelUserService.load(userId);
                    NovelUserDTO resultDTO = convert(result);
                    resultDTO.setSessionId(sessionId);
                    map.put("data", resultDTO);
                    map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
                } else {
                    map.put(ErrorCode.KEY, ErrorCode.INVALID_SESSION);
                }
            } else {
                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
            }
        } catch (Exception e) {
            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
            logger.error("Failed to update", e);
        }

        JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
        return mapper.toJson(map);
    }

    // Update user avatar
    @RequestMapping(value = "/updateAvatar", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String updateAvatar(HttpServletRequest request, @RequestParam("userId") String userId, @RequestParam("sessionId") String sessionId, MultipartFile file) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (userId != null && sessionId != null && file != null) {
                // Check if session is valid
                if (sessionService.check(userId, sessionId)) {
                    // Save the image to local
                    String root = request.getSession().getServletContext().getRealPath("/");
                    long random = new Date().getTime();
                    String name = String.format("%s_%d.jpg", userId, random);
                    String relativePath = File.separator + "images" + File.separator + "avatar" + File.separator + name;
                    String avatarPath = root + relativePath;
                    
                    Path path = Paths.get(avatarPath);
                    if (Files.exists(path)) {
                        Files.delete(path);
                    }
                    
                    String testPath = root + File.separator + "images" + File.separator + "avatar" + File.separator;
                    Path testpath = Paths.get(testPath);
                    System.out.println(testpath);
                    if (Files.exists(testpath)) { 
                    	System.out.println("存在");
                    } else {
                    	System.out.println("不存在");
                    }
                    
                    
                    InputStream stream = file.getInputStream();
                    Files.copy(stream, path);
                    stream.close();
                    
                    // Update the user.avatar
                    NovelUser user = novelUserService.load(userId);
                    user.setAvatar(relativePath);
                    novelUserService.update(user, true);
                    
                    map.put("data", relativePath);
                    map.put(ErrorCode.KEY, ErrorCode.SUCCESS);
                } else {
                    map.put(ErrorCode.KEY, ErrorCode.INVALID_SESSION);
                }
            } else {
                map.put(ErrorCode.KEY, ErrorCode.INVALID_PARAMS);
            }
        } catch (Exception e) {
            map.put(ErrorCode.KEY, ErrorCode.UNKNOWN_ERROR);
            logger.error("Failed to update avatar", e);
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
