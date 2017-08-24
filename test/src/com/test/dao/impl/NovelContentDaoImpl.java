package com.test.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.NovelContentDao;
import com.test.dao.helper.ConnManager;
import com.test.model.NovelContent;

@Repository("novelContentDao")
public class NovelContentDaoImpl extends SimpleDaoImpl<NovelContent, Integer> implements NovelContentDao{

	@Override
	public NovelContent getbyDirectoryLink(Integer id) {
		
		
        //String sql = "select * from c_article_detail where article_id = ? and last_update_date > ? and content is not null";
		
		List<NovelContent> novelContents = new ArrayList<NovelContent>();
		String sql = "select * from c_article_detail where id = ?";
		
		 try {
	            Connection conn = ConnManager.takeConn();
	            PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setInt(1,id);
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	            	NovelContent novelContent = convertResultSetToArticle(rs);
	            	novelContents.add(novelContent);
	            }
	            
	            rs.close();
	            stmt.close();
	            
	            ConnManager.offerConn(conn);
	        } catch (Exception e) {
	            logger.error("Failed to getbyDirectoryLink", e);
	        }

	        return novelContents.get(0);
	}
	
	@Override
	public List<NovelContent> getContentsById(String article_id, Integer id) throws Exception {
		if (article_id == null || article_id.length() == 0) {
			return new ArrayList<NovelContent>();
		}
		
		List<NovelContent> list = new ArrayList<NovelContent>();
		
		String sql = "";
		
		if (id == null) {
			sql = "select * from c_article_detail where article_id = ?";
		} else {
			sql = "select * from c_article_detail where article_id = ? and id > ?";
		}		
		
		 try {
	            Connection conn = ConnManager.takeConn();
	            PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setString(1,article_id);
	            if (id != null) {
	            	stmt.setInt(2,id);
	            }
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	            	NovelContent novelContent = convertResultSetToArticle(rs);
	            	list.add(novelContent);
	            }
	            
	            rs.close();
	            stmt.close();
	            
	            ConnManager.offerConn(conn);
	        } catch (Exception e) {
	            logger.error("Failed to getbyDirectoryLink", e);
	        }
		 
		 return list;
		//return new ArrayList<ArticleContent>();
	}

	private static NovelContent convertResultSetToArticle(ResultSet rs) throws SQLException {
		NovelContent novelContent = new NovelContent();
		
		novelContent.setId(rs.getInt("id"));
		novelContent.setArticle_id(rs.getString("article_id"));
		novelContent.setContent(rs.getString("content"));
		novelContent.setArticle_directory(rs.getString("article_directory"));
		novelContent.setArticle_directory_link(rs.getString("article_directory_link"));
		
		
        return novelContent;
	}
	
}
