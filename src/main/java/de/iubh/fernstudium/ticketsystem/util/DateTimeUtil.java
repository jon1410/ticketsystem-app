package de.iubh.fernstudium.ticketsystem.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Hilfsklasse zur Datumskonvertierung zwischen {@link LocalDateTime} und {@link Timestamp}
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

    /**
     * Formattiert eine {@link LocalDateTime} als String für eine UI-Representation
     * @param localDateTime
     * @return
     */
    public static String formatForUI(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return formatter.format(localDateTime);
    }

}
