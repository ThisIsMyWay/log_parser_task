package com.test.db;

import com.test.exception.CriticalAppException;
import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl;

import java.io.IOException;
import java.sql.*;

public class DBManager {


    Server server;

    public void initializeDB() throws CriticalAppException {

        startServer();
        createTable();

    }

    public void stopDB(){
        if (server != null) {
            server.shutdown();
        }
    }

    private void startServer() throws CriticalAppException {
        try {
            HsqlProperties p = new HsqlProperties();
            p.setProperty("server.database.0", "file:db_in_file/hsqldb");
            p.setProperty("server.dbname.0", "mydb");
            p.setProperty("server.port", "9001");
            server = new Server();
            server.setProperties(p);
            server.setLogWriter(null); // can use custom writer
            server.setErrWriter(null); // can use custom writer
            server.start();

        } catch (ServerAcl.AclFormatException | IOException e) {
            throw new CriticalAppException("Error during startring server " + e.toString());
        }

    }

    private void createTable() throws CriticalAppException {
        try(Connection connection = DriverManager.getConnection(
                DBProperties.URL, DBProperties.LOGIN, DBProperties.PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement
                    .executeQuery(" CREATE TABLE IF NOT EXISTS Event (\n" +
                            "    id varchar(12) NOT NULL,\n" +
                            "    durationInMs  bigint NOT NULL,\n" +
                            "    host varchar(5),\n" +
                            "    type varchar(15),\n" +
                            "    alert boolean NOT NULL," +
                            "CONSTRAINT PK PRIMARY KEY (id), );")) {
        } catch (SQLException e) {
            throw new CriticalAppException("Problem with table creation caused by " + e.toString());
        }
    }
}
