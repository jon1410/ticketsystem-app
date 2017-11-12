package de.iubh.fernstudium.ticketsystem.db.util.test;

import de.iubh.fernstudium.ticketsystem.db.utils.QueryUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryUtilsTest {

    @Test
    public void testQueryUtils(){
        String expected = "select * from ticket where match (TEST) against (? in boolean mode) ";
        assertEquals(expected, QueryUtils.buildFullTextQuery("TEST"));
    }
}
