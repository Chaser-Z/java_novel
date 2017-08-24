package com.test.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.NovelChapterDao;
import com.test.dao.helper.ConnManager;
import com.test.model.NovelChapter;


@Repository("novelChapterDao")
public class NovelChapterDaoImpl extends SimpleDaoImpl<NovelChapter, Integer> implements NovelChapterDao{

	
    private static Logger logger = Logger.getLogger(NovelChapterDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public Map<String, Object> page(String rows, String current, String title, String article_directory) {
    	
    	StringBuffer sbsql = new StringBuffer("select * from c_article_detail as x   where 1=1 ");
        StringBuffer sbcountsql = new StringBuffer("select count(*) from c_article_detail as x  where 1=1 ");
        Integer currPge = Integer.parseInt(current) <= 0 ? 1 : Integer.parseInt(current);
        
        if(StringUtils.isNotBlank(title)){
        	sbsql.append("    and x.title like '%"+title+"%'    ");
        	sbcountsql.append("    and x.title like '%"+title+"%'    ");
        }
        if(StringUtils.isNotBlank(article_directory)){
        	sbsql.append("    and x.article_directory like '%"+article_directory+"%'    ");
        	sbcountsql.append("    and x.article_directory like '%"+article_directory+"%'    ");
        	
        }
        
        sbsql.append("  order by x.id desc  LIMIT ?,? ");
        List<NovelChapter> list = jdbcTemplate.query(sbsql.toString(), new RowMapper<NovelChapter>() {
            @Override
            public NovelChapter mapRow(ResultSet arg0, int arg1) throws SQLException {
                return convertResultSetToBackNovelChapter(arg0);
            }
        }, new Object[] {
        	(currPge - 1) * Integer.parseInt(rows), Integer.parseInt(rows) 
        });

        Long totalCount = jdbcTemplate.queryForLong(sbcountsql.toString());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", list);
        map.put("total", totalCount);
        return map;
    }
    
    private static NovelChapter convertResultSetToBackNovelChapter(ResultSet rs) throws SQLException {
		NovelChapter novelChapter = new NovelChapter();

		//articleInfo
		novelChapter.setTitle(rs.getString("title"));
		novelChapter.setId(rs.getInt("id"));
		novelChapter.setArticle_id(rs.getString("article_id"));
		novelChapter.setUpdate_status(rs.getString("update_status"));
		novelChapter.setLast_update_date(rs.getString("last_update_date"));
		novelChapter.setLast_update_directory(rs.getString("last_update_directory"));
		novelChapter.setArticle_directory(rs.getString("article_directory"));
		novelChapter.setArticle_directory_link(rs.getString("article_directory_link"));
		
        return novelChapter;
	}
    
    @Override
    public List<NovelChapter> getByArticleId(String articleId) {
    	
        List<NovelChapter> novelChapters = new ArrayList<NovelChapter>();
        String sql = "select id, article_directory, last_update_date, last_update_directory, article_id from c_article_detail where article_id = ?";
        try {
            Connection conn = ConnManager.takeConn();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, articleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            	NovelChapter articleInfo = convertResultSetToNovelChapter(rs);
            	novelChapters.add(articleInfo);
            }
            rs.close();
            stmt.close();
            
            ConnManager.offerConn(conn);
        } catch (Exception e) {
            logger.error("Failed to find latest NovelChapter", e);
        }
        System.out.println(novelChapters);
        return novelChapters;
    }
    
	
	@Override
	public List<NovelChapter> findLatest(String id, String lastDate) {
		
        List<NovelChapter> novelChapters = new ArrayList<NovelChapter>();
        String sql = "select * from c_article_detail where article_id = ? and last_update_date > ? and content is not null";

        try {
            Connection conn = ConnManager.takeConn();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.setString(2, lastDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            	NovelChapter novelChapter = convertResultSetToNovelChapter(rs);
            	novelChapters.add(novelChapter);
            }
            
            rs.close();
            stmt.close();
            
            ConnManager.offerConn(conn);
        } catch (Exception e) {
            logger.error("Failed to find latest NovelChapter", e);
        }

        return novelChapters;
	}
	
	private static NovelChapter convertResultSetToNovelChapter(ResultSet rs) throws SQLException {
		NovelChapter novelChapter = new NovelChapter();

		//articleInfo
		//articleInfo.setTitle(rs.getString("title"));
		novelChapter.setId(rs.getInt("id"));
		novelChapter.setArticle_directory(rs.getString("article_directory"));
		//articleInfo.setArticle_directory_link(rs.getString("article_directory_link"));
		novelChapter.setLast_update_date(rs.getString("last_update_date"));
		novelChapter.setLast_update_directory(rs.getString("last_update_directory"));
		novelChapter.setArticle_id(rs.getString("article_id"));
		
        return novelChapter;
	}
}
