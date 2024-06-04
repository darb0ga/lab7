package com.darb0ga.server;


import com.darb0ga.common.commands.Command;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.managers.DBManager;
import com.darb0ga.common.util.Reply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.sql.*;

public class Server {
    private final int port = 2226;
    private final DatagramSocket datagramSocket;
    private static final Logger logger = LogManager.getLogger(Server.class);

    public Server() throws IOException, SQLException, ClassNotFoundException {
        datagramSocket = new DatagramSocket(port);
        CollectionManager.readCollection("1.xml");
        connectToDB();
    }

    private void connectToDB() throws SQLException {
        String url = "jdbc:postgresql://localhost:8080/studs";
        String login = "s408308";
        String password = "Gmw1sU78KDUUtgIl";
        DBManager dataBase = new DBManager(url, login, password);
        Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM labwork");
        while (resultSet.next()) {
            System.out.println(resultSet.getObject(2));
            // получение и обработка данных
        }
        resultSet.close();
        statement.close();
        connection.close();
    }


    public void run() throws IOException {
        logger.info("Сервер запущен");
        byte[] bytes = new byte[10_000];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, datagramSocket.getInetAddress(), port);
        ClientCommunication newClient = new ClientCommunication(datagramSocket);
        while (true) {
            Command command = newClient.readMessage(packet, bytes);
            logger.info("Чтение команды");
            if (command != null) {
                logger.info("Выполнение команды");
                Reply replyToClient = newClient.commandExecution(command, false, null);
                logger.info("Отправка ответа");
                newClient.sendMessage(replyToClient, packet.getSocketAddress());
            }
        }
    }



}

