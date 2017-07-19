package com.test.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.miger.commons.utils.StringUtil;
import com.test.dao.NovelUserDao;
import com.test.model.NovelUser;
import com.test.service.NovelUserService;
import com.test.webservice.constants.IdentityTypes;
import com.vdurmont.emoji.EmojiParser;

@Service("novelUserService")
public class NovelUserServiceImpl implements NovelUserService {

    private static final Logger logger = Logger.getLogger(NovelUserService.class);

    @Autowired
    private NovelUserDao novelUserDao;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(NovelUser user) throws Exception {
        if (user == null) {
            return;
        }
        
        if (user.getId() != null) {
        	NovelUser current = load(user.getId());
            if (current != null) {
                copyUser(user, current, false);
                novelUserDao.update(current);
            } else {
            	novelUserDao.save(user);
            }
        } else {
        	novelUserDao.save(user);
        }
    }

    @Override
    public NovelUser update(NovelUser user, boolean force) throws Exception {
        if (user == null) {
            return null;
        }

        NovelUser result = null;

        if (user.getId() != null) {
        	NovelUser current = load(user.getId());
            copyUser(user, current, force);
            novelUserDao.update(current);
            
            result = current;
        } else {
            logger.warn("Could not update user, user id is null");
        }
        
        return result;
    }
    
    @Override
    public void updateCredential(NovelUser user) throws Exception {
        if (user == null) {
            return;
        }
        
        novelUserDao.update(user);
    }

    @Override
    public NovelUser load(String id) throws Exception {
        if (id == null || id.length() == 0) {
            return null;
        }

        List<NovelUser> list = novelUserDao.find("from NovelUser as c where c.isDel = 0 and c.id = ?", id);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public NovelUser getByIdentity(String identityType, String identifier) throws Exception {
        if (identityType == null || identifier == null) {
            return null;
        }

        List<NovelUser> list = novelUserDao.find("from NovelUser as c where c.isDel = 0 and c.identityType = ? and c.identifier = ?", identityType, identifier);
        return list.size() > 0 ? list.get(0) : null;
    }

    private static void copyUser(NovelUser source, NovelUser target, boolean force) {
        /*
        target.setIdentityType(source.getIdentityType());
        target.setIdentifier(source.getIdentifier());
        */
        
        if (!source.getIdentityType().equals(IdentityTypes.EMAIL)) {
            target.setCredential(source.getCredential());
        }
        
        if (source.getExpiresIn() != null) {
            target.setExpiresIn(source.getExpiresIn());
        } else {
            target.setExpiresIn(0);
        }

        if (source.getRefreshToken() != null) {
            target.setRefreshToken(source.getRefreshToken());
        }

        if (source.getUnionid() != null) {
            target.setUnionid(source.getUnionid());
        }

        target.setVerified(source.getVerified());

        if (source.getEmail() != null) {
            if (force || target.getEmail() == null) {
                target.setEmail(source.getEmail());
            }
        }

        if (source.getNickname() != null) {
            if (force || target.getNickname() == null) {
                target.setNickname(source.getNickname());
            }
        }

        if (source.getAvatar() != null) {
            if (force || target.getAvatar() == null) {
                target.setAvatar(source.getAvatar());
            }
        }

        if (source.getNationalityId() != null) {
            if (force || target.getNationalityId() == null) {
                target.setNationalityId(source.getNationalityId());
            }
        }

        if (source.getHskId() != null) {
            if (force || target.getHskId() == null) {
                target.setHskId(source.getHskId());
            }
        }

        if (source.getInterestedLangs() != null) {
            if (force || target.getInterestedLangs() != null) {
                target.setInterestedLangs(source.getInterestedLangs());
            }
        }

        if (source.getUsername() != null) {
            if (force || target.getUsername() == null) {
                target.setUsername(source.getUsername());
            }
        }

        if (source.getGender() != null) {
            if (target.getGender() == null) {
                target.setGender(source.getGender());
            }
        } else if (target.getGender() == null) {
            target.setGender(0);
        }

        if (source.getPhone() != null) {
            if (force || target.getPhone() == null) {
                target.setPhone(source.getPhone());
            }
        }

        if (source.getDescription() != null) {
            if (force || target.getDescription() == null) {
                target.setDescription(source.getDescription());
            }
        }

        if (source.getAddress() != null) {
            if (force || target.getAddress() == null) {
                target.setAddress(source.getAddress());
            }
        }

        if (source.getZipCode() != null) {
            if (force || target.getZipCode() == null) {
                target.setZipCode(source.getZipCode());
            }
        }

        if (source.getCity() != null) {
            if (force || target.getCity() == null) {
                target.setCity(source.getCity());
            }
        }

        if (source.getProvince() != null) {
            if (force || target.getProvince() == null) {
                target.setProvince(source.getProvince());
            }
        }

        if (source.getCountry() != null) {
            if (force || target.getCountry() == null) {
                target.setCountry(source.getCountry());
            }
        }

        /*
        target.setPwdQuestion(source.getPwdQuestion());
        target.setPwdAnswer(source.getPwdAnswer());
        target.setRandomCode(source.getRandomCode());
        target.setCreateTime(source.getCreateTime());
        */
        target.setLastUpdateTime(source.getLastUpdateTime());
        /*
        target.setIsDel(source.getIsDel());
        target.setStatus(source.getStatus());
        */
    }

    public Map<String, Object> page(String rows, String curr, String name, String identityType) {
        StringBuffer sb = new StringBuffer("from NovelUser as model where model.isDel=0");
        if (!StringUtil.isNull(name)) {
            sb.append(" and model.nickname like '%").append(name).append("%'");
        }
        
        if (!StringUtil.isNull(identityType)) {
            sb.append(" and model.identityType='").append(identityType).append("'");
        }
        
        sb.append(" order by model.createdTime desc");
        Map<String, Object> result = novelUserDao.getMapPage(rows, curr, sb.toString());
        
        try {
            Object obj = result.get("rows");
            if (obj instanceof List) {
                @SuppressWarnings("unchecked")
                List<NovelUser> list = (List<NovelUser>)obj;
                for (NovelUser user : list) {
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
        } catch (Exception e) {
            logger.warn("Failed to process emoji for cim user", e);
        }
        
        return result;
    }

    @Override
    public Map<String, Object> statistics() {
        Map<String, Object> map = new HashMap<String, Object>();
        String sql = "select identity_type as regway,count(identity_type) as value from t_user group by identity_type";
        List<Map<String, Object>> regWay = jdbcTemplate.queryForList(sql);
        map.put("regWay", regWay);
        return map;
    }

}
