package de.iubh.fernstudium.ticketsystem.util;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
}
