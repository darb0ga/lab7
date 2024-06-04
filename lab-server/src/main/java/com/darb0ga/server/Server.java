package com.darb0ga.server;


import com.darb0ga.common.commands.Command;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.managers.DBManager;
import com.darb0ga.common.util.Reply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.Scanner;

public class Server {
    private final int port = 2226;
    private final DatagramSocket datagramSocket;
    private static final Logger logger = LogManager.getLogger(Server.class);

    public Server() throws IOException, SQLException, ClassNotFoundException {
        datagramSocket = new DatagramSocket(port);
        CollectionManager.readCollection("1.xml");
    }

    private void connectToDB() {
        String login = null, password = null, url = null;
        try {
            Scanner signInScanner = new Scanner(new File("admin"));
            login = signInScanner.nextLine().trim();
            password = signInScanner.nextLine().trim();
            url = signInScanner.nextLine().trim();
        } catch (FileNotFoundException e) {
            logger.error("Проблема с входными данными для подключения к БД. Ошибка: " + e.getMessage());
            logger.error("Завершение работы.");
            System.exit(-1);
        }

        logger.info("Создание менеджера базы данных.");
        DBManager dbManager = new DBManager(url, login, password);
        dbManager.connectTOBD();
    }


    public void run() throws IOException, SQLException {
        logger.info("Сервер запущен");
        byte[] bytes = new byte[10_000];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, datagramSocket.getInetAddress(), port);
        connectToDB();
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

