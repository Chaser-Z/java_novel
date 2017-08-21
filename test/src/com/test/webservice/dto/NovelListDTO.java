package com.test.webservice.dto;


public class NovelListDTO extends BaseDTO{
	private String article_id;
	
	private String title;
	
	private String author;
	
	private String article_abstract;
	
	private String link;
	
	private String image_link;
	
	private String status;
	
	private String article_type;


	
	
	public String getArticle_type() {
		return article_type;
	}

	public void setArticle_type(String article_type) {
		this.article_type = article_type;
	}

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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getArticle_abstract() {
		return article_abstract;
	}

	public void setArticle_abstract(String article_abstract) {
		this.article_abstract = article_abstract;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImage_link() {
		return image_link;
	}

	public void setImage_link(String image_link) {
		this.image_link = image_link;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
	
	
}
