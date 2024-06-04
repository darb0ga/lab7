package com.darb0ga.common.managers;

import org.apache.logging.log4j.LogManager;

import java.sql.*;
import java.util.logging.Logger;

public class DBManager {

    private final String url;
    private final String login;
    private final String password;
    private static final Logger logger = (Logger) LogManager.getLogger(DBManager.class);

    private Connection connection;

    public DBManager(String url, String user, String pswd) {
        this.url = url;
        this.login = user;
        this.password = pswd;
    }

    public void connectTOBD(){
        try {
            connection = DriverManager.getConnection(url, login, password);
            logger.info("Подключение к Базе Данных");

        } catch (SQLException e) {
            logger.warning("Соединение не установлено. Ошибка: " + e.getMessage());
        }
    }

    public Array readCollection() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM labwork");
        return resultSet.getArray(1);
    }

    public void saveCollection() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery();

    }
}
