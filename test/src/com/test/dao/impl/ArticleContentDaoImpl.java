package com.test.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.ArticleContentDao;
import com.test.dao.helper.ConnManager;
import com.test.model.ArticleContent;

@Repository("articleContentDao")
public class ArticleContentDaoImpl extends SimpleDaoImpl<ArticleContent, Integer> implements ArticleContentDao{

	@Override
	public ArticleContent getbyDirectoryLink(Integer id) {
		
		
        //String sql = "select * from c_article_detail where article_id = ? and last_update_date > ? and content is not null";
		
		List<ArticleContent> articleHots = new ArrayList<ArticleContent>();
		String sql = "select * from c_article_detail where id = ?";
		
		 try {
	            Connection conn = ConnManager.takeConn();
	            PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setInt(1,id);
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	            	ArticleContent articleHot = convertResultSetToArticle(rs);
	            	articleHots.add(articleHot);
	            }
	            
	            rs.close();
	            stmt.close();
	            
	            ConnManager.offerConn(conn);
	        } catch (Exception e) {
	            logger.error("Failed to getbyDirectoryLink", e);
	        }

	        return articleHots.get(0);
	}

	private static ArticleContent convertResultSetToArticle(ResultSet rs) throws SQLException {
		ArticleContent articleInfo = new ArticleContent();
		
		articleInfo.setId(rs.getInt("id"));
		articleInfo.setArticle_id(rs.getString("article_id"));
		articleInfo.setContent(rs.getString("content"));
		articleInfo.setArticle_directory(rs.getString("article_directory"));
		articleInfo.setArticle_directory_link(rs.getString("article_directory_link"));
		
		
        return articleInfo;
	}
	
}
