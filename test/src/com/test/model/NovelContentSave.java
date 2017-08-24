package com.test.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_article_detail")
public class NovelContentSave implements java.io.Serializable{

	private static final long serialVersionUID = 3L;

	
	@Id
    @Column(name = "id")
    private String id;
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	@Column(name = "article_directory_link")
	private String article_directory_link;
	
	@Column(name = "content")
	private String content;

	@Column(name = "author")
	private String author;
	
	@Column(name = "article_directory")
	private String article_directory;

	@Column(name = "article_id")
	private String article_id;

	public String getArticle_id() {
		return article_id;
	}

	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}

	public String getArticle_directory() {
		return article_directory;
	}

	public void setArticle_directory(String article_directory) {
		this.article_directory = article_directory;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getArticle_directory_link() {
		return article_directory_link;
	}

	public void setArticle_directory_link(String article_directory_link) {
		this.article_directory_link = article_directory_link;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
