package com.springprojects.config;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utils {
    public static final Date convertToDate(String date){
        DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        java.util.Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(date1.getTime());
    }
}
