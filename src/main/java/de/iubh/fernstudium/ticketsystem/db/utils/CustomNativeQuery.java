package de.iubh.fernstudium.ticketsystem.db.utils;

import java.sql.Timestamp;
import java.util.*;

public class CustomNativeQuery {

    static final String TABLE_NAME = "ticket ";
    static final String SELECT_ALL = "select * ";
    static final String SELECT = "select ";
    static final String FROM = "from ";
    static final String WHERE = "where ";
    static final String AND = "and ";
    static final String OR = "or ";
    static final String IN = " in ";
    static final String BETWEEN = " between ";
    static final String LIKE = " like ";
    static final String MATCH = "match ";
    static final String AGAINST = "against (";
    static final String BOOLEAN_MODE = " in boolean mode) ";
    static final String OP_CB = "(";
    static final String CL_CB = ")";

    private String queryString;
    private List<Object> parameters;

    CustomNativeQuery(QueryBuilder builder) {
        this.queryString = builder.sb.toString();
        this.parameters = builder.parameters;
    }

    public String getQueryString() {
        return queryString;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public static CustomNativeQuery.QueryBuilder builder(){
        return new CustomNativeQuery.QueryBuilder();
    }

    public static class QueryBuilder{

        StringBuilder sb;
        List<Object> parameters;
        int counter = 0;

        private QueryBuilder(){
            sb = new StringBuilder(1000);
            parameters = new LinkedList<>();
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
            sb.append(FROM).append(tableName).append(" ");
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

        public QueryBuilder and(){
            sb.append(" ").append(AND);
            return this;
        }

        public QueryBuilder columnName(String columnName){
            sb.append(columnName);
            return this;
        }

        public QueryBuilder notEmpty(){
            sb.append(" != \"\"");
            return this;
        }

        public QueryBuilder equals(String value){
            sb.append(" = ").append("? ");
            addParam(value);
            return this;
        }

        public QueryBuilder like(String value){
            sb.append(LIKE).append("? ");
            addParam("%" + value + "%");
            return this;
        }

        public QueryBuilder or(){
            sb.append(" ").append(OR);
            return this;
        }

        public QueryBuilder match(String columnName){
            sb.append(MATCH).append(OP_CB).append(columnName).append(CL_CB);
            return this;
        }

        public QueryBuilder against(String value){
            sb.append(AGAINST).append(value);
            return this;
        }

        public QueryBuilder in(Object... values){

            StringJoiner sj = new StringJoiner(",");
            for(Object o : values){
                sj.add("?");
            }
            sb.append(IN).append(OP_CB).append(sj.toString()).append(CL_CB);
            addParam(values);
            return this;
        }

        public QueryBuilder inBooleanMode(){
            sb.append(BOOLEAN_MODE);
            return this;
        }

        public QueryBuilder between(String fromValue, String toValue){
            sb.append(BETWEEN).append(fromValue).append(" ").append(AND).append(toValue).append(" ");
            return this;
        }

        public QueryBuilder between(Timestamp fromValue, Timestamp toValue){
            sb.append(BETWEEN).append("?").append(" ").append(AND).append("? ");
            addParam(fromValue, toValue);
            return this;
        }

        public QueryBuilder openCurlyBraces(){
            sb.append(OP_CB);
            return this;
        }

        public QueryBuilder closeCurlyBraces(){
            sb.append(CL_CB);
            return this;
        }

        public QueryBuilder addTrueStatement(){
            sb.append(" 1 = 1 ");
            return this;
        }

        public QueryBuilder addFalseStatement(){
            sb.append(" 1 = 0 ");
            return this;
        }

        public CustomNativeQuery buildQuery(){
            return new CustomNativeQuery(this);
        }

        private void addParam(Object... values) {
            parameters.addAll(Arrays.asList(values));
        }

    }


}
