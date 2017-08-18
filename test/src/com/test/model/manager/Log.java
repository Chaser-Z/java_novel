package com.test.model.manager;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.miger.commons.dto.UUIDEntity;

/**
 * 系统日志
 * 
 * @author Victory
 * 
 */
@Entity
@Table(name = "m_log")
public class Log extends UUIDEntity {

	private static final long serialVersionUID = -4446397766562616900L;

	private String userId;// 用户ID

	@Formula("(select u.name from m_user u where u.id=userId)")
	private String userName;// 用户名称

	private String ip;// 用户IP

	private Integer type;// 日志类型：1.系统操作、2.微信操作

	private String content;// 日志内容

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
