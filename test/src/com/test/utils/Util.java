package com.test.utils;

import java.util.Date;

public class Util {

    public static long getCurrentUnixTime() {
        return (long) ((double) (new Date().getTime()) / 1000.0);
    }

}
