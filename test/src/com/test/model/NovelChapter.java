package com.test.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.miger.commons.dto.IdEntity;


@Entity
@Table(name = "c_article_detail")
public class NovelChapter extends IdEntity{
	
	private static final long serialVersionUID = 4152559748898004323L;

	@Column(name = "article_id")
	private String article_id;
	
	@Override
	public String toString() {
		return "NovelChapter [article_id=" + article_id + ", title=" + title + ", update_status=" + update_status
				+ ", last_update_date=" + last_update_date + ", last_update_directory=" + last_update_directory
				+ ", article_directory=" + article_directory + ", article_directory_link=" + article_directory_link
				+ ", id=" + id + "]";
	}

	@Column(name = "title")
	private String title;
			
	@Column(name = "update_status")
	private String update_status;
	
	@Column(name = "last_update_date")
	private String last_update_date;
	
	@Column(name = "last_update_directory")
	private String last_update_directory;
	
	@Column(name = "article_directory")
	private String article_directory;
	
	@Column(name = "article_directory_link")
	private String article_directory_link;
	
	//@Column(name = "content")
	//private String content;

	public String getArticle_id() {
		return article_id;
	}

	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUpdate_status() {
		return update_status;
	}

	public void setUpdate_status(String update_status) {
		this.update_status = update_status;
	}

	public String getLast_update_date() {
		return last_update_date;
	}

	public void setLast_update_date(String last_update_date) {
		this.last_update_date = last_update_date;
	}

	public String getLast_update_directory() {
		return last_update_directory;
	}

	public void setLast_update_directory(String last_update_directory) {
		this.last_update_directory = last_update_directory;
	}

	public String getArticle_directory() {
		return article_directory;
	}

	public void setArticle_directory(String article_directory) {
		this.article_directory = article_directory;
	}

	public String getArticle_directory_link() {
		return article_directory_link;
	}

	public void setArticle_directory_link(String article_directory_link) {
		this.article_directory_link = article_directory_link;
	}

//	public String getContent() {
//		return content;
//	}
//
//	public void setContent(String content) {
//		this.content = content;
//	}

}
