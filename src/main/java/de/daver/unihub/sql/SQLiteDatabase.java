package de.daver.unihub.sql;

import de.daver.unihub.io.FileUtils;
import de.daver.unihub.util.PrefixLogger;
import de.daver.unihub.util.Reflections;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase extends Database {

    private final String url;

    public SQLiteDatabase(PrefixLogger logger, File dir, String name){
        super(logger);
        File dbFile = FileUtils.createFile(new File(dir, name + ".db"));
        url = "jdbc:sqlite:" + dir.getAbsolutePath() + File.separator + name + ".db";
    }

    @Override
    public Connection connect() {
        Reflections.loadClass("org.sqlite.JDBC");
        try {
            return DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
