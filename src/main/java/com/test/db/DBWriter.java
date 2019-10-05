package com.test.db;

import com.test.exception.CriticalAppException;

import java.sql.*;

public class DBWriter {

    private DBWriter(){}

    public static void saveEvent(EventItem eventItem) throws CriticalAppException {

        try(Connection connection = DriverManager.getConnection(
                DBProperties.URL, DBProperties.LOGIN, DBProperties.PASSWORD);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Event (ID, DURATIONINMS, HOST, TYPE, ALERT) VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1,eventItem.getId());
            statement.setLong(2, eventItem.getDuration());
            statement.setString(3, eventItem.getHost());
            statement.setString(4, eventItem.getType());
            statement.setBoolean(5, eventItem.isAlert());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new CriticalAppException("Problem with insertion into table caused by " + e.toString());
        }
    }
}
