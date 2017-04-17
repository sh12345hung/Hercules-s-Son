package com.example.trungnguyen.newsapp.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Trung Nguyen on 4/14/2017.
 */

public class CalculateTimesAgo {
    public static String calculate(String time) {
        long Time;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date past = format.parse(time);
            Date now = new Date();
            Time = now.getTime() - past.getTime();
            if (TimeUnit.MILLISECONDS.toMinutes(Time) <= 60)
                return TimeUnit.MILLISECONDS.toMinutes(Time) + " minutes ago";
            else if (TimeUnit.MILLISECONDS.toHours(Time) <= 24)
                return TimeUnit.MILLISECONDS.toHours(Time) + " hours ago";
            else return TimeUnit.MILLISECONDS.toDays(Time) + " days ago";
//            System.out.println(TimeUnit.MILLISECONDS.toMillis(now.getTime() - past.getTime()) + " milliseconds ago");
//            System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
//            System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
//            System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Chưa xác định";
    }
}
