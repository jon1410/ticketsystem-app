package de.iubh.fernstudium.ticketsystem.util;

import javax.ejb.Local;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by ivanj on 16.07.2017.
 */
public class DateTimeUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

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
        return LocalDateTime.parse(date, formatter);
    }
}
