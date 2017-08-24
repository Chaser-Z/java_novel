package com.test.webservice.dto;

public class NovelContentDTO extends BaseDTO{

	private String article_directory_link;

	private String content;

	private String author;

	private String article_directory;

	private String article_id;

	private Integer id;
	
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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
