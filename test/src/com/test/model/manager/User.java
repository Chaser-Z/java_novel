package com.test.model.manager;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.miger.commons.dto.UUIDEntity;
import com.miger.commons.utils.DateUtil;

@Entity
@Table(name = "m_user")
public class User extends UUIDEntity {

	private static final long serialVersionUID = 825804637162426831L;

	private String name;// 名称

	private String password;// 密码

	private String email;// 邮箱

	private Integer sex;// 性别:1.男、2.女

	private String mobile;// 手机

	private Integer status;// 状态：1.正常、2.删除

	private Integer frequency;// 登录次数

	private String limitsId;// 权限ID

	@Formula("(select u.name from m_limit u where u.id=limitsId and u.status=1)")
	private String limitsName;// 权限名称

	private Date lastTime;// 最后登录时间

	@Transient
	private String lastTime_;// 创建时间

	public String getLastTime_() {
		if (lastTime != null) {
			lastTime_ = DateUtil.dateToStr(lastTime, "yyyy-MM-dd HH:mm");
		}
		return lastTime_;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public String getLimitsId() {
		if (this.limitsName == null) {
			return null;
		}
		return limitsId;
	}

	public void setLimitsId(String limitsId) {
		this.limitsId = limitsId;
	}

	public String getLimitsName() {
		return limitsName;
	}

	public void setLimitsName(String limitsName) {
		this.limitsName = limitsName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

}