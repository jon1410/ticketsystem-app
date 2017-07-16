package de.iubh.fernstudium.ticketsystem.util.test;

import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;
import org.junit.Test;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by ivanj on 16.07.2017.
 */
public class DateTimeUtilTest {

    @Test
    public void sqlTimestampToLocalDateTest(){
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp ts = DateTimeUtil.localDtToSqlTimestamp(localDateTime);
        assertNotNull(ts);
    }

    @Test
    public void sqlTimestampToLocalDateWithNullValueTest(){
        Timestamp ts = DateTimeUtil.localDtToSqlTimestamp(null);
        assertNull(ts);
    }

    @Test
    public void localDtToSqlTimestampTest(){
        Timestamp ts = new Timestamp(Calendar.getInstance().getTime().getTime());
        LocalDateTime ldt = DateTimeUtil.sqlTimestampToLocalDate(ts);
        assertNotNull(ldt);
    }

    @Test
    public void localDtToSqlTimestampNullValueTest(){
        LocalDateTime ldt = DateTimeUtil.sqlTimestampToLocalDate(null);
        assertNull(ldt);
    }
}
