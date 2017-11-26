package de.iubh.fernstudium.ticketsystem.util.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.StringJoiner;

public class FullTextQueryBuilderTest {

    @Test
    public void testSplit(){
        String s = "in Bearbeitung durch Student,in Bearbeitung durch IUBH,neu";
        String[] sA = StringUtils.split(s, ",");
        for (String aS: sA ) {
            System.out.println(aS);
        }
    }

    @Test
    public void testStringJoiner(){
        StringJoiner sj = new StringJoiner(", ");

        String[] test = {"abc", "xy", "zzz"};

        for(String s : test){
            sj.add(s);
        }
        System.out.println(sj.toString());
    }

    @Test
    public void testFullTextQuery(){

        System.out.println(buildFullTextQuery("DESCRIPTION"));
    }


    private String buildFullTextQuery(String tableColumn) {

        tableColumn = "(" + tableColumn + ")";
        StringBuilder sb = new StringBuilder();
        sb.append(QueryParameter.SELECT_ALL).append(QueryParameter.FROM).append(QueryParameter.TABLE_NAME)
                .append(QueryParameter.WHERE).append(QueryParameter.MATCH).append(tableColumn).append(" ")
                .append(QueryParameter.AGAINST).append("?").append(QueryParameter.BOOLEAN_MODE);
        return sb.toString();
    }

    class QueryParameter{

        static final String TABLE_NAME = "ticket ";
        static final String SELECT_ALL = "select * ";
        static final String FROM = "from ";
        static final String WHERE = "where ";
        static final String MATCH = "match";
        static final String AGAINST = "against(";
        static final String BOOLEAN_MODE = " in boolean mode);";
    }
}
