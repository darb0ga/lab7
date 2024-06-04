package com.darb0ga.common.managers;

import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.exceptions.NoSuchIDException;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;

public class DBManager {

    private final String url;
    private final String login;
    private final String password;
    private static final Logger logger = LogManager.getLogger(DBManager.class);

    private Connection connection;
    @Getter
    private HashSet<LabWork> collection = new HashSet<>();

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
            logger.warn("Соединение не установлено. Ошибка: " + e.getMessage());
        }
    }

    public Collections readCollection() throws SQLException {
        //чтение данных из коллеккции
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM labwork");
        //return resultSet.getArray(1);
        return null;
    }

    public void saveCollection() throws SQLException, FileNotFoundException {
        Statement statement = connection.createStatement();
        Scanner scan = new Scanner(new File("CollectionToBD"));
        String query = "";
        while (scan.hasNextLine()){
            var line = scan.nextLine();
            query += line + "\n";
        }
        boolean resultSet = statement.execute(query);
//        ResultSet coord = statement.executeQuery("SELECT * FROM coordinates");
//        while (coord.next()) {
//            System.out.println(coord.getObject(2));
//            // получение и обработка данных
//        }
//        coord.close();
        statement.close();
        //надо ли ту  закрывать коннектион потому что потом то мы и так рабоаем с ним
        connection.close();
    }

    public void addElement(LabWork laba){
        collection.add(laba);
    }

    public void clearCollection() {
        collection.clear();
    }

    public boolean removeById(int id) {
        try{
            return collection.remove(findById(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LabWork findById(long id) throws NoSuchIDException {
        for (LabWork lab : collection) {
            if (lab.getId() == id) {
                return lab;
            }
        }
        return null;
    }

    public void removeElements(Collection<LabWork> toRemove) {
        collection.removeAll(toRemove);
    }

    public void updateById(LabWork newLab, int id) {
        LabWork lab = findById(id);
        lab.setMinimalPoint(newLab.getMinimalPoint());
        lab.setCreationDate(new Date());
        lab.setAuthor(newLab.getAuthor());
        lab.setName(newLab.getName());
        lab.setCoordinates(newLab.getCoordinates());
        lab.setDifficulty(newLab.getDifficulty());

    }

    public int findUser(String login){
        try {
            PreparedStatement ps = connection.prepareStatement("select * from users where (users.username =?);");
            ps.setString(1, login);
            ResultSet res = ps.executeQuery();
            if (res.next()){
                return res.getInt("id");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return -1;
    }
}
