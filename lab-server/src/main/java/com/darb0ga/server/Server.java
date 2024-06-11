package com.darb0ga.server;


//короче че сказать просто из сервера надо все почти задачи сновить в клиент хуйню
//и тут в старте сделать tread потоки какие то там
// ну вот а уже в клиенте перезаписать метод ран и там непосредственно подкючать к бд
//а основная ошибка то вот в чем - у меня передавалась null dbmanager кто так сделал урод да
// короче вот так делаешь и уже в команде логина и региста все будет чики пуки

//а я с вашего позволения пойду  спать потому что это уже не возможно хохо


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



//    public void run() throws IOException, SQLException {
//        logger.info("Сервер запущен");
//        byte[] bytes = new byte[10_000];
//        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, datagramSocket.getInetAddress(), port);
//        connectToDB();
//        ClientCommunication newClient = new ClientCommunication(datagramSocket, );
//        while (true) {
//            Command command = newClient.readMessage(packet, bytes);
//            logger.info("Чтение команды");
//            if (command != null) {
//                logger.info("Выполнение команды");
//                Reply replyToClient = newClient.commandExecution(command, false, null);
//                logger.info("Отправка ответа");
//                newClient.sendMessage(replyToClient, packet.getSocketAddress());
//            }
//        }
//    }




