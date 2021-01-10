package de.daver.unihub.sql;

public class Column <T> {

    private String name;
    private String sqlType;

    public Column(String name, String sqlType){
        this.name = name;
        this.sqlType = sqlType;
    }

    public String getName(){
        return this.name;
    }

    public T transformObject(Object obj){
        return (T) obj;
    }

    public String getSqlType(){
        return sqlType;
    }
}
