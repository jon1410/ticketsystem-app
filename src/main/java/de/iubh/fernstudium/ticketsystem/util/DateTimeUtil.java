package de.iubh.fernstudium.ticketsystem.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by ivanj on 16.07.2017.
 */
public class DateTimeUtil {

    public static LocalDateTime sqlTimestampToLocalDate(Timestamp tsp){
        if(tsp == null){
            return null;
        }
        return tsp.toLocalDateTime();
    }

    public static Timestamp localDtToSqlTimestamp(LocalDateTime ldt){
        if(ldt == null){
            return null;
        }
        return Timestamp.valueOf(ldt);
    }

    public static Timestamp now(){
        return Timestamp.valueOf(LocalDateTime.now());
    }

    public static LocalDateTime format(String date){
        String pattern = "EEE MMM dd HH:mm:ss zzz yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.atStartOfDay();
    }

}
