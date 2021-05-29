package com.codeforgeyt.wschatapplication.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringUtils {

    private static final String TIME_FORMATTER= "HH:mm:ss";

    public static String getCurrentFormattedTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMATTER);
        return simpleDateFormat.format(time);
    }
}
