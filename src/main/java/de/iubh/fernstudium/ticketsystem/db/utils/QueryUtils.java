package de.iubh.fernstudium.ticketsystem.db.utils;

public class QueryUtils {

    public static String buildFullTextQuery(String tableColumn) {

        tableColumn = "(" + tableColumn + ")";
        StringBuilder sb = new StringBuilder();
        sb.append(CustomNativeQuery.SELECT_ALL).append(CustomNativeQuery.FROM).append(CustomNativeQuery.TABLE_NAME)
                .append(CustomNativeQuery.WHERE).append(CustomNativeQuery.MATCH).append(tableColumn).append(" ")
                .append(CustomNativeQuery.AGAINST).append("?").append(CustomNativeQuery.BOOLEAN_MODE);
        return sb.toString();
    }

    public static String buildSearchDetailsQuery(){

        StringBuilder sb = new StringBuilder();
        sb.append(CustomNativeQuery.SELECT_ALL).append(CustomNativeQuery.FROM).append("TICKET")
                .append(CustomNativeQuery.WHERE).append("CREA_TSP").append("between").append("?")
                .append(CustomNativeQuery.AND).append("?").append(CustomNativeQuery.AND).append("?").append("!=")
                .append("\"\"").append(CustomNativeQuery.OR);
        return null;
    }


}
