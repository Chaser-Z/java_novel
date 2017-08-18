package com.test.model.manager;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.miger.commons.dto.UUIDEntity;

@Entity
@Table(name = "m_limit")
public class Limit extends UUIDEntity {

	private static final long serialVersionUID = 825654637163626857L;

	private String name;// 名称

	private String limitIds;// 权限ID

	private String limitNames;// 权限名称

	private Integer status;// 状态：1.正常、2.删除

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLimitIds() {
		return limitIds;
	}

	public void setLimitIds(String limitIds) {
		this.limitIds = limitIds;
	}

	public String getLimitNames() {
		return limitNames;
	}

	public void setLimitNames(String limitNames) {
		this.limitNames = limitNames;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
