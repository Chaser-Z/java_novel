package com.test.model.manager;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.miger.commons.dto.UUIDEntity;

/**
 * 菜单
 * 
 * @author Victory
 * 
 */
@Entity
@Table(name = "m_menu")
public class Menu extends UUIDEntity {

	private static final long serialVersionUID = 7358641083031756162L;

	private String pid;// 父节点ID

	private String name;// 名称

	private String url;// 地址

	private String iconCls;// 图标

	private Integer status;// 状态:1.显示、2.删除

	private Integer sequence;// 排序

	@OneToMany(mappedBy = "pid", fetch = FetchType.EAGER)
	@Where(clause = "status=1")
	@OrderBy("sequence")
	private List<Menu> childList;// 子树列表

	public List<Menu> getChildList() {
		return childList;
	}

	public void setChildList(List<Menu> childList) {
		this.childList = childList;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

}
