package com.test.dao.helper;

import java.io.InputStream;
import java.util.Properties;

public final class Config {

    public static String url;
    public static String driver;
    public static String username;
    public static String password;

    static {
        Properties props = new Properties();
        InputStream in;
        try {
            in = Config.class.getResourceAsStream("/mysql.properties");
            props.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        url = props.get("db.url").toString();
        driver = props.get("db.dirverClass").toString();
        username = props.get("db.username").toString();
        password = props.get("db.password").toString();
    }
}
