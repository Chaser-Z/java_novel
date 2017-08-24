package com.test.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_article_list")
public class NovelListSave implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id")
    private String id;
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	@Column(name = "article_id")
	private String article_id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "author")
	private String author;
	
	@Column(name = "article_abstract")
	private String article_abstract;
	
	@Column(name = "link")
	private String link;
	
	@Column(name = "image_link")
	private String image_link;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "article_type")
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
	
	@Override
    public String toString() {
        return String.format("Novel: <%s>, <%s>, <%s>, <%s>", id, article_id, link, title);
    }
	
}
