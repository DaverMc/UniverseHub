package de.daver.unihub.sql;

public enum Commands {

    CREATE_TABLE("CREATE TABLE IF NOT EXISTS arg0(arg1 PRIMARY KEY(arg2));"),
    DROP_TABLE("DROP TABLE arg0"),
    INSERT_INTO("INSERT OR IGNORE INTO arg0 (arg1) VALUES(arg2);"),
    UPDATE("UPDATE arg0 SET arg1 WHERE arg2 = 'arg3'"),
    SELECT_ALL("SELECT * FROM arg0");

    private final String cmd;

    Commands(String command){
        this.cmd = command;
    }

    public String replaceArgs(Object...args){
        String tmp = cmd;
        for(int i = 0; i < args.length; i++){
            tmp = tmp.replace("arg" + i, String.valueOf(args[i]));
        }
        return tmp;
    }

}
