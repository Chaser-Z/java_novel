package com.test.webservice.constants;

public class ErrorCode {

    public static final String KEY = "errorCode";

    // Common
    public static final int SUCCESS = 0;

    // Server Common: 1 ~ 99
    public static final int UNKNOWN_ERROR = 1;
    public static final int DATABASE_ERROR = 2;
    public static final int INVALID_SESSION = 3;
    public static final int INVALID_PARAMS = 4;
    public static final int NOT_IMPLEMENTED = 5;

    // User: 200 ~ 299
    public static final int USER_IDENTITY_EXISTING = 200;
    public static final int USER_NOT_EXIST = 201;
    public static final int ONLY_FOR_EMAIL_USER = 202;
    public static final int USER_OLD_CREDENTIAL_INVALID = 203;
    public static final int USER_CREDENTIAL_INCORRECT = 204;

    // Journal: 300 ~ 399
    public static final int JOURNAL_ID_INVALID = 300;
    public static final int JOURNAL_NOT_FOUND = 301;

    // Article: 400 ~ 499
    public static final int ARTICLE_ID_INVALID = 400;
    public static final int ARTICLE_NOT_FOUND = 401;
    public static final int ARTICLE_PARENT_ID_INVALID = 402;
    public static final int ARTICLE_KEYWORD_INVALID = 403;
    public static final int ARTICLE_TOPIC_INVALID = 404;

    // Article Menu: 500 ~ 599
    public static final int MENU_ID_INVALID = 500;
    public static final int MENU_NOT_FOUND = 501;

    // Special: 600 ~ 699
    public static final int SPECIAL_ID_INVALID = 600;
    public static final int SPECIAL_NOT_FOUND = 601;

    // Language: 700 ~ 799
    public static final int LANGUAGE_ID_INVALID = 700;
    public static final int LANGUAGE_NOT_FOUND = 701;

    // Record: 800 ~ 899
    public static final int RECORD_ID_INVALID = 800;
    public static final int RECORD_NOT_FOUND = 801;

    // Push: 900 ~ 999
    public static final int PUSH_ID_INVALID = 900;
    public static final int PUSH_NOT_FOUND = 901;
    
    // Version: 1000 ~ 1100
    public static final int VERSION_LATEST_NOT_FOUND = 1000;
    public static final int VERSION_PRODUCT_ID_INVALID = 1001;

}
