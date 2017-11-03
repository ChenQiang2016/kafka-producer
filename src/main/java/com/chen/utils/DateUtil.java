package com.chen.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ChenQiang on 2017/10/12.
 */
public class DateUtil {

    public static String formatDate(Date date, String pattern){
        return new SimpleDateFormat(pattern).format(date);
    }
}
