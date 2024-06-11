package com.darb0ga.common.managers;

import com.darb0ga.common.collection.*;
import com.darb0ga.common.exceptions.NoSuchIDException;
import com.darb0ga.common.util.Reply;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.ConnectException;
import java.nio.file.AccessDeniedException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static java.sql.Types.INTEGER;

public class DBManager {

    private final String url;
    private final String login;
    private final String password;
    @Getter
    @Setter
    private boolean isConnected;
    private static final Logger logger = LogManager.getLogger(DBManager.class);

    @Getter
    private Connection connection;

    private static HashSet<LabWork> myLabs = new HashSet<>();

    public DBManager(String url, String user, String pswd) {
        this.url = url;
        this.login = user;
        this.password = pswd;
        this.isConnected = false;
    }

    public HashSet<LabWork> getMyLabs() {
        return myLabs;
    }

    public void connectTOdataBase() {
        try {
            connection = DriverManager.getConnection(url, login, password);
            setConnected(true);
            logger.info("Подключение к Базе Данных");
            readCollection();

        } catch (SQLException e) {
            logger.warn("Соединение не установлено. Ошибка: " + e.getMessage());
        }
    }

    public void readCollection() throws SQLException {
        logger.info("Обновление коллекции");
        String join = "select * from labwork " +
                "left join coordinates on (labwork.coordinate = coordinates.id) " +
                "left join person on (labwork.person = person.id) " +
                "left join location on (person.location = location.id);";
        PreparedStatement joinStatement = connection.prepareStatement(join);
        ResultSet result = joinStatement.executeQuery();
        myLabs.clear();
        while (result.next()) {
            LabWork laba = getDataFromDB(result);
            myLabs.add(laba);
        }
    }

    private LabWork getDataFromDB(ResultSet result) throws SQLException {
        Location loc = null;
        if ((result.getString("location")) != null) {
            String locname = result.getString("loc_name");
            Float loc_y = result.getFloat("loc_y");
            Integer loc_x = result.getInt("loc_x");
            loc = new Location(loc_x, loc_y, locname);
        }
        Coordinates coord = null;
        if (result.getString("coordinate") != null) {
            Float x = result.getFloat("x");
            Integer y = result.getInt("y");
            coord = new Coordinates(x, y);
        }

        Person person = null;
        if (result.getString("person") != null) {
            String personName = result.getString("person_name");
            if (result.getString("person_height") != null) {
                Double height = result.getDouble("person_height");
                person = new Person(personName, height, loc);
            } else {
                person = new Person(personName, loc);
            }

        }

        LabWork newLaba = new LabWork(result.getInt("id"), result.getString("name"), coord, result.getDate("creation_date"), result.getFloat("minimal_point"), Difficulty.valueOf(result.getString("difficulty")), person, result.getInt("owner_id"));
        return newLaba;
    }

    public void saveCollection() throws SQLException, AccessDeniedException, ConnectException {
        for (LabWork labWork : myLabs) {
            modificateElement(labWork, new AuthorsRight("dasha", "1611"), false, -1);
        }
    }

    public int modificateElement(LabWork laba, AuthorsRight author, boolean needToUpdate, int id) throws ConnectException, AccessDeniedException, SQLException {
        logger.info("Создание нового элемента");
        int userID = findUser(author.getName());
        if (userID == -1) {
            if (!isConnected()) {
                throw new ConnectException("Проблемы с подключением к БД.");
            } else {
                throw new AccessDeniedException("Отказано в доступе.");
            }
        }

        PreparedStatement statement;
        int coordsID;
        int personID;
        if (needToUpdate) {

            statement = connection.prepareStatement("update labwork set(name, coordinate, creation_date, minimal_point, difficulty, person, owner_id)=(?, ?, ?, ?, ?, ?, ?) where id=? returning id;");
            statement.setInt(8, id);

            PreparedStatement ps = connection.prepareStatement("select coordinate, person from labwork where id=?;");
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            res.next();

            coordsID = modificatedCoordinates(laba.getCoordinates(), true, res.getInt("coordinate"));

            if (laba.getAuthor() != null) {
                personID = modificatePerson(laba.getAuthor(), true, res.getInt("person"));
                statement.setInt(6, personID);
            } else {
                statement.setNull(6, INTEGER);
            }
            return res.getInt("id");
        } else {
            statement = connection.prepareStatement("insert into labwork (name, coordinate, creation_date, minimal_point, difficulty, person, owner_id) values (?, ?, ?, ?, ?, ?, ?) returning id;");
            coordsID = modificatedCoordinates(laba.getCoordinates(), false, -1);
            if (laba.getAuthor() != null) {
                personID = modificatePerson(laba.getAuthor(), false, -1);
                statement.setInt(6, personID);
            } else {
                statement.setNull(6, INTEGER);
            }
        }
        statement.setString(1, laba.getName());
        statement.setInt(2, coordsID);
        statement.setDate(3, laba.getCreationDate());
        statement.setDouble(4, laba.getMinimalPoint());
        statement.setString(5, String.valueOf(laba.getDifficulty()));
        statement.setInt(7, userID);
        ResultSet res = statement.executeQuery();
        res.next();
        if (needToUpdate) {
            logger.info("Обновлен элемент с  id = " + id);
        } else {
            logger.info("Элемент добавлен в базу данных");
        }
        return res.getInt("id");

    }

    private int modificatePerson(Person author, boolean neednessToUpdate, int person) throws SQLException {
        PreparedStatement statement;
        int locID;
        if (neednessToUpdate) {

            statement = connection.prepareStatement("update person set(person_name, person_height, location)=(?, ?, ?) where id=? returning id;");
            statement.setInt(4, person);

            PreparedStatement ps = connection.prepareStatement("select location from person where id=?;");
            ps.setInt(1, person);
            ResultSet res = ps.executeQuery();
            res.next();

            if (author.getLocation() != null) {
                locID = modificateLocation(author.getLocation(), true, res.getInt("location"));
                statement.setInt(3, locID);
            } else {
                statement.setNull(3, INTEGER);
            }
        } else {
            statement = connection.prepareStatement("insert into person(person_name, person_height, location) values (?, ?, ?) returning id;");

            if (author.getLocation() != null) {
                locID = modificateLocation(author.getLocation(), false, -1);
                statement.setInt(3, locID);
            } else {
                statement.setNull(3, INTEGER);
            }
            statement.setString(1, author.getName());
            if (author.getHeight() != null) {
                statement.setDouble(2, author.getHeight());
            } else {
                statement.setNull(2, INTEGER);
            }
        }

        ResultSet res = statement.executeQuery();
        res.next();
        return res.getInt("id");
    }

    private int modificateLocation(Location location, boolean needToUpdate, int loc) throws SQLException {
        PreparedStatement statement;
        if (needToUpdate) {
            statement = connection.prepareStatement("update location set(loc_x, loc_y, loc_name)=(?, ?, ?) where id=? returning id;");
            statement.setInt(4, loc);
        } else {
            statement = connection.prepareStatement("insert into location(loc_x, loc_y, loc_name) values (?, ?, ?) returning id;");
        }
        if (location.getName() != null) {
            statement.setString(3, location.getName());
        } else {
            statement.setNull(3, INTEGER);
        }

        statement.setInt(1, location.getX());
        statement.setFloat(2, location.getY());
        ResultSet res = statement.executeQuery();
        res.next();
        return res.getInt("id");
    }

    private int modificatedCoordinates(Coordinates coordinates, boolean needToUpdate, int coordinate) throws SQLException {
        PreparedStatement statement;
        if (needToUpdate) {
            statement = connection.prepareStatement("update coordinates set(x, y)=(?, ?) where id=? returning id;");
            statement.setInt(3, coordinate);
        } else {
            statement = connection.prepareStatement("insert into coordinates(x, y) values (?, ?) returning id;");
        }
        statement.setDouble(1, coordinates.getX());
        statement.setInt(2, coordinates.getY());
        ResultSet res = statement.executeQuery();
        res.next();
        return res.getInt("id");
    }


    public boolean removeById(int id) {
        try {
            PreparedStatement statement;
            statement = connection.prepareStatement("delete from labwork where id=? returning id;");
            statement.setInt(1, id);
            ResultSet res = statement.executeQuery();
            res.next();
            return myLabs.remove(findById(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LabWork findById(long id) throws NoSuchIDException {
        for (LabWork lab : myLabs) {
            if (lab.getId() == id) {
                return lab;
            }
        }
        return null;
    }

    public void removeElements(Collection<LabWork> toRemove) {
        myLabs.removeAll(toRemove);
    }

    public void updateById(LabWork newLab, int id) {
        LabWork lab = findById(id);
        lab.setMinimalPoint(newLab.getMinimalPoint());
        lab.setCreationDate(new Date(System.currentTimeMillis()));
        lab.setAuthor(newLab.getAuthor());
        lab.setName(newLab.getName());
        lab.setCoordinates(newLab.getCoordinates());
        lab.setDifficulty(newLab.getDifficulty());

    }

    public int findUser(String login) {
        try {
            PreparedStatement ps = connection.prepareStatement("select * from users where username =? ;");
            ps.setString(1, login);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                return res.getInt("id");
            }else return -1;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return -1;
    }

    public void addUser(AuthorsRight client) throws SQLException {
        if (!isConnected()) {
            throw new SQLException("Проблемы с подключением к БД.");
        }
        PreparedStatement addStatement = connection.prepareStatement("insert into users(username, password_hash) values (?, ?) returning id;");
        addStatement.setString(1, client.getName());
        addStatement.setString(2, client.getPassword());
        ResultSet res = addStatement.executeQuery();
        client.setID(res.getInt("user_id"));
        logger.info(client.getID());
        res.next();
//        return res.getInt("user_id");
    }

    public boolean checkPassword(AuthorsRight author) {
        try {
            PreparedStatement checking = connection.prepareStatement("select password_hash from users where (users.username = ?);");
            checking.setString(1, author.getName().trim());
            ResultSet res = checking.executeQuery();
            res.next();
            return author.getPassword().equals(res.getString("password_hash"));
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
//        PreparedStatement checkPassword = connection.prepareStatement("select password_hash from users where username = ?;");
//        checkPassword.setString(1, author.getName());
//        //проблема где то тутт......
//        ResultSet res = checkPassword.executeQuery();
//        res.next();
//        return author.getPassword().equals(res.getString("password_hash"));
    }

    public Reply reconnect(Reply reply, int count) {
        reply.addResponse("Попытка переподключения к БД " + count);
        connectTOdataBase();
        if (count == 3) {
            reply.addResponse("База данных не отвечает");
            return reply;
        }
        if (isConnected()) {
            return null;
        } else {
            return reconnect(reply, count + 1);
        }
        //не понимаю пока что ту  надо мне сделать
    }

    public void closeConnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void remove(LabWork i) {
        myLabs.remove(i);
    }
}
