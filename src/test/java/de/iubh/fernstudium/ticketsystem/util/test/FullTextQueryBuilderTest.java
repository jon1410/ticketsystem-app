package de.iubh.fernstudium.ticketsystem.util.test;

import org.junit.Test;

public class FullTextQueryBuilderTest {

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
