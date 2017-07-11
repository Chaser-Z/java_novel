package com.test.dao.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnManager.class);

    private static BlockingQueue<Connection> connPool = new LinkedBlockingQueue<Connection>();

    public static synchronized Connection takeConn() {
        Connection conn = null;

        if (connPool.size() > 0) {
            // Get the connection from pool
            try {
                conn = connPool.take();
            } catch (InterruptedException e) {
                logger.error("Failed to take connection from pool", e);
                conn = newConn();
            }
        } else {
            // Create a new connection
            conn = newConn();
        }

        return conn;
    }

    public static synchronized void offerConn(Connection conn) {
        if (conn != null) {
            connPool.offer(conn);
        }
    }

    public static synchronized void closeAllConn() {
        if (connPool.size() > 0) {
            for (Connection conn : connPool) {
                try {
                    conn.close();
                } catch (Exception e) {
                    logger.error("Failed to close connection", e);
                }
            }
        }
    }

    private static Connection newConn() {
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(Config.url, Config.username, Config.password);
        } catch (Exception e) {
            logger.info("Failed to create new connection", e);
        }

        return conn;
    }
}
