package de.iubh.fernstudium.ticketsystem.db.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

public class CustomNativeQuery {

    static final String TABLE_NAME = "ticket ";
    static final String SELECT_ALL = "select * ";
    static final String SELECT = "select ";
    static final String FROM = "from ";
    static final String WHERE = "where ";
    static final String AND = "and ";
    static final String MATCH = "match";
    static final String AGAINST = "against(";
    static final String BOOLEAN_MODE = " in boolean mode);";

    private String queryString;

    CustomNativeQuery(QueryBuilder builder) {
        this.queryString = builder.sb.toString();
    }

    public String getQueryString() {
        return queryString;
    }

    public static CustomNativeQuery.QueryBuilder builder(){
        return new CustomNativeQuery.QueryBuilder();
    }

    public static class QueryBuilder{

        StringBuilder sb;
        Map<Integer, String> parameterValues;
        int counter = 0;

        private QueryBuilder(){
            sb = new StringBuilder(1000);
            parameterValues = new LinkedHashMap<>();
        }

        public QueryBuilder selectAll(){
            sb.append(SELECT_ALL);
            return this;
        }

        public QueryBuilder select(String... fields){
            StringJoiner sj = new StringJoiner(", ");
            for(String f : fields){
                sj.add(f);
            }
            sb.append(SELECT).append(sj.toString());
            return this;
        }

        public QueryBuilder from(String tableName){
            sb.append(FROM).append(tableName);
            return this;
        }

        public QueryBuilder where(String columnName){
            sb.append(WHERE).append(columnName);
            return this;
        }

        public QueryBuilder and(String columnName){
            sb.append(AND).append(columnName);
            return this;
        }

        public QueryBuilder equals(String value){
            sb.append("=").append("?");
            addParam(value);
            return this;
        }

        public CustomNativeQuery buildQuery(){
            return new CustomNativeQuery(this);
        }

        private void addParam(String value) {
            parameterValues.put(counter, value);
            counter++;
        }

    }


}
