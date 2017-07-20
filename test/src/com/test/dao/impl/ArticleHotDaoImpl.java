package com.test.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.ArticleHotDao;
import com.test.dao.helper.ConnManager;
import com.test.model.ArticleHot;

@Repository("articleHotDao")
public class ArticleHotDaoImpl extends SimpleDaoImpl<ArticleHot, String> implements ArticleHotDao {

	
	
	@Override
	public List<ArticleHot> getArticleByKeyword(String keyword) {
		System.out.println(keyword);
		List<ArticleHot> articleHots = new ArrayList<ArticleHot>();
		String sql = "select * from c_article_list where title like ?";
		
		 try {
	            Connection conn = ConnManager.takeConn();
	            PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setString(1, "%" + keyword + "%");
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	            	ArticleHot articleHot = convertResultSetToArticle(rs);
	            	articleHots.add(articleHot);
	            }
	            
	            rs.close();
	            stmt.close();
	            
	            ConnManager.offerConn(conn);
	        } catch (Exception e) {
	            logger.error("Failed to getArticleByKeyword", e);
	        }

	        return articleHots;
	}
	
	
	@Override
	public List<ArticleHot> getHome() {
		
        List<ArticleHot> articleHots = new ArrayList<ArticleHot>();
        String sql = "SELECT * from (SELECT * FROM c_article_list  x WHERE x.article_type = '热门'  ORDER BY id desc  LIMIT 3) as a  " 
        			 + "  UNION   " 
        		     + "  SELECT * from (SELECT * FROM c_article_list  x WHERE x.article_type = '玄幻' ORDER BY id DESC LIMIT 6) as b  "
        		     + "  UNION   "
        		     + "  SELECT * from (SELECT * FROM c_article_list  x WHERE x.article_type = '修真' ORDER BY id DESC LIMIT 6) as c  "
        		     + "  UNION   "
        		     + "  SELECT * from (SELECT * FROM c_article_list  x WHERE x.article_type = '都市' ORDER BY id DESC LIMIT 6) as d  "
        		     + "  UNION   "
        		     + "  SELECT * from (SELECT * FROM c_article_list  x WHERE x.article_type = '历史' ORDER BY id DESC LIMIT 6) as e  "
        		     + "  UNION   "
        		     + "  SELECT * from (SELECT * FROM c_article_list  x WHERE x.article_type = '网游' ORDER BY id DESC LIMIT 6) as f  "
        		     + "  UNION   "
        		     + "  SELECT * from (SELECT * FROM c_article_list  x WHERE x.article_type = '科幻' ORDER BY id DESC LIMIT 6) as g  "
        		     + "  UNION   "
        		     + "  SELECT * from (SELECT * FROM c_article_list  x WHERE x.article_type = '恐怖' ORDER BY id DESC LIMIT 6) as h  "
        		     + "  UNION   "
        		     + "  SELECT * from (SELECT * FROM c_article_list  x WHERE x.article_type = '全本' ORDER BY id DESC LIMIT 6) as i  ";

        try {
            Connection conn = ConnManager.takeConn();
            PreparedStatement stmt = conn.prepareStatement(sql);
            //stmt.setString(2, lastDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            	ArticleHot articleHot = convertResultSetToArticle(rs);
            	articleHots.add(articleHot);
            }
            
            rs.close();
            stmt.close();
            
            ConnManager.offerConn(conn);
        } catch (Exception e) {
            logger.error("Failed to getHome ActicleInfo", e);
        }

        return articleHots;
	}
	
	private static ArticleHot convertResultSetToArticle(ResultSet rs) throws SQLException {
		ArticleHot articleInfo = new ArticleHot();
		
		articleInfo.setId(rs.getString("id"));
		articleInfo.setTitle(rs.getString("title"));
		articleInfo.setArticle_id(rs.getString("article_id"));
		articleInfo.setArticle_abstract(rs.getString("article_abstract"));
		articleInfo.setArticle_type(rs.getString("article_type"));
		articleInfo.setAuthor(rs.getString("author"));
		articleInfo.setLink(rs.getString("link"));
		articleInfo.setImage_link(rs.getString("image_link"));
		
        return articleInfo;
	}
	
}
