package de.daver.unihub.sql;

import de.daver.unihub.util.Useful;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public abstract class Table<K,V> {

    private final Column<?>[] columns;
    private final Map<K, V> cachedValues;
    private final String name;
    private Database database;

    protected Table(String name, int columnSize){
        this.columns = new Column[columnSize];
        this.name = name;
        this.cachedValues = new HashMap<>();
    }

    public abstract void cacheValues();

    protected void setColumn(int position, Column<?> column){
        this.columns[position] = column;
    }

    protected void setDatabase(Database database){
        this.database = database;
    }

    public String getName(){
        return this.name;
    }

    public void create(){
        String sqlCommand = Commands.CREATE_TABLE.replaceArgs(this.name, getColumnNameType(), columns[0].getName());
        database.getLogger().info("Start Creating Table " + this.name + "...");
        if(executeSqlCommand(sqlCommand)){
            database.getLogger().info("Finished Creating Table " + this.name + "!");
        }else{
            database.getLogger().warn("Failed Creating Table " + this.name + "!");
        }

    }

    protected void insert(Object...objects){
        String sqlCommand = Commands.INSERT_INTO.replaceArgs(this.name, getColumnNames(), getValues(objects));
        database.getLogger().info("Inserting value to Table " + this.name);
        if(executeSqlCommand(sqlCommand)){
            database.getLogger().info("Finished inserting values!");
        }else{
            database.getLogger().warn("Failed inserting values!");
        }

    }

    protected void update(Object...objects) {
        String sqlCommand = Commands.UPDATE.replaceArgs(this.name, this.getColumnNameValue(objects), this.columns[0].getName(), objects[0]);
        database.getLogger().info("Updating value to Table " + this.name);
        if (executeSqlCommand(sqlCommand)) {
            database.getLogger().info("Finished updating!");
        }else{
            database.getLogger().warn("Failed updating!");
        }
    }

    protected HashMap<Object, Object[]> getValues(){
        HashMap<Object, Object[]> map = new HashMap<>();
        String sqlCommand = Commands.SELECT_ALL.replaceArgs(this.name);
        database.getLogger().info("Try getting all values of Table " + this.name);
        try(Connection connection = this.database.connect();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(sqlCommand)){
            Object[] tmpValues;
            while (set.next()){
                tmpValues = getRow(set);
                map.put(tmpValues[0], tmpValues);
            }
            database.getLogger().info("Finished caching all values!");
        }catch (SQLException e){
            e.printStackTrace();
            database.getLogger().warn("Failed caching values!");
        }
        return map;
    }

    public Map<K, V> getCache(){
        return this.cachedValues;
    }

    private Object[] getRow(ResultSet set) throws SQLException{
        Object[] values = new Object[this.columns.length];
        for(int i = 0; i < values.length; i++){
            values[i] = set.getObject(this.columns[i].getName());
        }
        return values;
    }

    private String getColumnNames(){
        StringBuilder builder = new StringBuilder();
        for(Column<?> column : columns){
            builder.append(column.getName()).append(", ");
        }
        return Useful.trimString(builder,2);
    }

    private String getColumnNameType(){
        StringBuilder builder = new StringBuilder();
        for(Column<?> column : columns){
            builder.append(column.getName()).append(" ").append(column.getSqlType()).append(", ");
        }
        return Useful.trimString(builder,1);
    }

    private String getColumnNameValue(Object...obj){
        StringBuilder builder = new StringBuilder();
        for(Column<?> column : columns){
            builder.append(column.getName()).append(" = ").append(obj).append(", ");
        }
        return Useful.trimString(builder,2);
    }

    private String getValues(Object[] args){
        StringBuilder builder = new StringBuilder("'");
        for(Object obj : args){
            builder.append(obj).append("','");
        }
        return Useful.trimString(builder, 2);
    }

    private boolean executeSqlCommand(String sqlCommand){
        try(Connection connection = this.database.connect();
            Statement statement = connection.createStatement();){
            statement.execute(sqlCommand);
            return true;
        }catch (SQLException e){
            database.getLogger().warn("Failed executing command:\" " + sqlCommand + "\"!");
            e.printStackTrace();
            return false;
        }
    }

    protected int transFormBoolean(boolean b){
        if(b) return 1;
        return 0;
    }

    protected boolean transformInteger(int i){
        return i == 1;
    }

    protected String[] transformString(String s){
        return s.split(",");
    }

    protected String transformStringArray(String[] array){
        StringBuilder builder = new StringBuilder();
        for(String string : array){
            builder.append(string).append(",");
        }
        return Useful.trimString(builder, 1);
    }

    protected String transformArray(Object...array){
        if(array == null) return null;
        StringBuilder builder = new StringBuilder();
        for(Object obj : array){
            builder.append(obj).append(",");
        }
        return Useful.trimString(builder, 1);
    }

}
