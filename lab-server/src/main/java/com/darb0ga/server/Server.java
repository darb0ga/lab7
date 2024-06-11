package com.darb0ga.server;

import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.managers.DBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Server {
    private final int port = 2226;
    private final ExecutorService executorService;
    private DBManager dbManager;
    private String file;
    private final DatagramSocket datagramSocket;
    private static final Logger logger = LogManager.getLogger(Server.class);

    private final int amountOfClients = 1;

    public Server(String fileWithData) throws IOException, SQLException, ClassNotFoundException {
        datagramSocket = new DatagramSocket(port);
        file = fileWithData;
        executorService = Executors.newFixedThreadPool(amountOfClients);
    }

    private void connectToDB() {
        String login = null, password = null, url = null;
        try {
            Scanner signInScanner = new Scanner(new File("admin"));
            url = signInScanner.nextLine().trim();
            login = signInScanner.nextLine().trim();
            password = signInScanner.nextLine().trim();
        } catch (FileNotFoundException e) {
            logger.error("Проблема с входными данными для подключения к БД. Ошибка: " + e.getMessage());
            logger.error("Завершение работы.");
            System.exit(-1);
        }

        logger.info("Создание менеджера базы данных.");
        dbManager = new DBManager(url, login, password);
    }

    public void start() throws FileNotFoundException {
        connectToDB();
        logger.info("Запуск сервера.");

        CollectionManager.getCollection().clear();
        dbManager.connectTOdataBase();
        if (dbManager.getConnection()==null){
            logger.info("Невозможно получить соединение с базой данных");
            System.exit(-1);
        }

        try {
            dbManager.readCollection();
            logger.info("Коллекция успешно загружена.");
        } catch (SQLException e) {
            logger.error("Коллекция не загружена. Ошибка: " + e.getMessage());
            System.exit(-1);
        }

            ArrayList<ClientCommunication> CHList = new ArrayList<>(amountOfClients);

            ExecutorService fixedPool = Executors.newFixedThreadPool(10);
            ForkJoinPool forkJoinPool = new ForkJoinPool();

            for (int i = 0; i < amountOfClients; i++) {
                CHList.add(new ClientCommunication(datagramSocket, dbManager, file, fixedPool, forkJoinPool));
                executorService.submit(CHList.get(i));
            }


            Thread closingThreads = new Thread(() ->
            {
                for (ClientCommunication client : CHList) {
                    client.shutDown();
                }
                System.out.println("Все потоки закрыты");
            });

            Runtime.getRuntime().addShutdownHook(closingThreads);
        }
    }




