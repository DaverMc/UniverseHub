package de.daver.unihub.sql;

import de.daver.unihub.util.PrefixLogger;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daver_Mc
 * @version 1.0.0
 * @author 1.0.0
 */
public abstract class Database {

    private final Map<String, Table<?,?>> tables;
    private final PrefixLogger logger;

    /**
     * Create an constructor with an Logger and a Map of Tables
     * @param logger a logger to log information
     */
    protected Database(PrefixLogger logger){
        this.tables = new HashMap<>();
        this.logger = logger;
    }

    /**
     * Creates a connection to the database
     * @return a Sql Connection to database
     *      should return null if connection hasn't been created
     */
    public abstract Connection connect();

    /**
     * Creates a table and adding it to the cached tables
     * @param table a Table with containing many data, representing a table of an database
     */
    public void createTable(Table<?,?> table){
        tables.put(table.getName(), table);
        table.setDatabase(this);
        table.create();
    }

    /**
     * @return The database logger
     */
    public PrefixLogger getLogger(){
        return this.logger;
    }

    /**
     * @param name the name of the table
     * @return a cached Table
     */
    public Table<?,?> getTable(String name){
        return tables.get(name);
    }

}
