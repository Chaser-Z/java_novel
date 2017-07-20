package com.test.webservice.param;

import com.test.webservice.dto.BaseDTO;

public class ArticleSearchParam extends BaseDTO {

    private String articleId;


    private String keyword;

    
    public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

   
}
