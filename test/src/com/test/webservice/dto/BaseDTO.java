package com.test.webservice.dto;

public class BaseDTO {

    protected String userId;
    protected String sessionId;
    protected Integer id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

	public Integer getId() {
		
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    
}
