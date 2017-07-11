package com.test.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.ArticleInfoDao;
import com.test.dao.helper.ConnManager;
import com.test.model.ArticleInfo;


@Repository("articleInfoDao")
public class ArticleInfoDapImpl extends SimpleDaoImpl<ArticleInfo, String> implements ArticleInfoDao{

	
    private static Logger logger = Logger.getLogger(ArticleInfoDao.class);

	
	@Override
	public List<ArticleInfo> findLatest(String id, String lastDate) {
		
        List<ArticleInfo> articleInfos = new ArrayList<ArticleInfo>();
        String sql = "select * from c_article_detail where article_id = ? and last_update_date > ? and content is not null";

        try {
            Connection conn = ConnManager.takeConn();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.setString(2, lastDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            	ArticleInfo articleInfo = convertResultSetToJournal(rs);
                articleInfos.add(articleInfo);
            }
            
            rs.close();
            stmt.close();
            
            ConnManager.offerConn(conn);
        } catch (Exception e) {
            logger.error("Failed to find latest ActicleInfo", e);
        }

        return articleInfos;
	}
	
	private static ArticleInfo convertResultSetToJournal(ResultSet rs) throws SQLException {
		ArticleInfo articleInfo = new ArticleInfo();
		
		articleInfo.setId(rs.getString("id"));
		articleInfo.setTitle(rs.getString("title"));
		articleInfo.setArticle_directory(rs.getString("article_directory"));
		articleInfo.setArticle_directory_link(rs.getString("article_directory_link"));
		articleInfo.setLast_update_date(rs.getString("last_update_date"));
		articleInfo.setLast_update_directory(rs.getString("last_update_directory"));
		articleInfo.setUpdate_status(rs.getString("update_status"));
		articleInfo.setArticle_id(rs.getString("article_id"));
		
        return articleInfo;
	}
}
