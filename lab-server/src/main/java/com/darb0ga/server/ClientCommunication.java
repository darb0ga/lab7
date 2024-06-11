package com.darb0ga.server;

import com.darb0ga.common.commands.Command;
import com.darb0ga.common.commands.History;
import com.darb0ga.common.exceptions.CommandRuntimeException;
import com.darb0ga.common.managers.DBManager;
import com.darb0ga.common.util.Header;
import com.darb0ga.common.util.Packet;
import com.darb0ga.common.util.Reply;
import com.darb0ga.common.util.Serializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class ClientCommunication implements Runnable {
    private static final int BUFFER_LENGTH = 10_000;
    private final DatagramSocket datagramSocket;
    private final String file;
    private final ExecutorService fixedPool;
    private final ExecutorService forkJoinPool;
    private DBManager dataBase;
    private ArrayList<String> history = new ArrayList<>();
    private final Serializer serializer = new Serializer();
    private static final Logger logger = LogManager.getLogger(ClientCommunication.class);

    public ClientCommunication(DatagramSocket datagramSocket, DBManager db, String filewithdata, ExecutorService fixedPool, ForkJoinPool forkJoinPool) throws FileNotFoundException {
        this.datagramSocket = datagramSocket;
        this.file = filewithdata;
        this.fixedPool = fixedPool;
        this.forkJoinPool = forkJoinPool;
        connectToDB();
    }

    public void shutDown() {
        dataBase.closeConnect();
        fixedPool.shutdown();
        forkJoinPool.shutdown();
    }


    public Command readMessage(DatagramPacket datagramPacket, byte[] buffer) throws IOException {
        datagramSocket.receive(datagramPacket);
        Packet packet = serializer.deserialize(buffer);
        Header header = packet.getHeader();
        int countOfPieces = header.getBlocksCount();
        ArrayList<Packet> list = new ArrayList<>(countOfPieces);
        list.add(header.getBlockNumber(), packet);
        int k = 1;

        while (k < countOfPieces) {
            datagramSocket.receive(datagramPacket);
            Packet newPacket = serializer.deserialize(buffer);
            Header newHeader = newPacket.getHeader();
            list.add(newHeader.getBlockNumber(), newPacket);
            k += 1;
        }

        int buffLength = 0;
        for (int i = 0; i < countOfPieces; i++) {
            buffLength += list.get(i).getPieceOfBuffer().length;
        }
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(buffLength)) {
            for (int i = 0; i < countOfPieces; i++) {
                byteStream.write(list.get(i).getPieceOfBuffer());
            }
            return serializer.deserialize(byteStream.toByteArray());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void sendMessage(Reply replyToClient, SocketAddress socketAddress) {
        try {
            Header header = new Header(0, 0);
            int headerLength = serializer.serialize(header).length + 200;

            byte[] buffer = serializer.serialize(replyToClient);
            int bufferLength = buffer.length;
            int countOfPieces = bufferLength / (BUFFER_LENGTH - headerLength);
            if (countOfPieces * (BUFFER_LENGTH - headerLength) < bufferLength) {
                countOfPieces += 1;
            }
            for (int i = 0; i < countOfPieces; i++) {
                header = new Header(countOfPieces, i);
                headerLength = serializer.serialize(header).length + 200;
                Packet packet = new Packet(header, Arrays.copyOfRange(buffer, i * (BUFFER_LENGTH - headerLength), Math.min(bufferLength, (i + 1) * (BUFFER_LENGTH - headerLength))));
                byte[] array = serializer.serialize(packet);
                DatagramPacket datagramPacket2 = new DatagramPacket(array, array.length, socketAddress);
                datagramSocket.send(datagramPacket2);
                Thread.sleep(100);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Reply commandExecution(Command command, boolean fileReading, Scanner scanner, DBManager dbmanager) throws SQLException {
        Reply reply = new Reply();
        history.add(command.getName());
        if (command instanceof History) {
            reply.addResponse("Введенные команды: ");
            for (String element : history.subList(Math.max(0, history.size() - 14), history.size())) {
                reply.addResponse(element);
            }
            return reply;
        }
        if (!command.getAddition().isEmpty()) {
            try {
                reply = command.execute(command.getAddition(), scanner, fileReading, dbmanager);
                return reply;
            } catch (FileNotFoundException | CommandRuntimeException e) {
                reply.addResponse(e.getMessage());
                return reply;
            }
        } else {
            try {
                reply = command.execute("", scanner, fileReading, dbmanager);
            } catch (FileNotFoundException | CommandRuntimeException ex) {
                reply.addResponse(ex.getMessage());
                return reply;
            }
        }
        return reply;
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[BUFFER_LENGTH];
                int port = 2226;
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, datagramSocket.getInetAddress(), port);
                Command command;
                try {
                    command = readMessage(datagramPacket, buffer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                fixedPool.execute(() -> {

                    Reply response;
                    logger.info("Выполнение запроса. " + Thread.currentThread().getName());
                    try {
                        response = commandExecution(command, false, null, dataBase);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    forkJoinPool.execute(() -> {
                        logger.info("Отправка ответа. " + Thread.currentThread().getName());
                        sendMessage(response, datagramPacket.getSocketAddress());
                    });
                });

            } catch (Exception e) {
                logger.info(e);
                logger.error("thread: " + Thread.currentThread().getName() + ":" + e.getMessage());
                return;
            }
        }

    }

    private void connectToDB() {
        String login = null;
        String password = null;
        String URL = null;
        try {
            Scanner signInScanner = new Scanner(new File(file));
            login = signInScanner.nextLine().trim();
            password = signInScanner.nextLine().trim();
            URL = signInScanner.nextLine().trim();
        } catch (FileNotFoundException e) {
            logger.error("Проблема с входными данными для подключения к БД. Ошибка: " + e.getMessage());
            logger.error("Завершение работы");
            System.exit(-1);
        }
        dataBase = new DBManager(login, password, URL);
        dataBase.connectTOdataBase();
        logger.info("Менеджер базы данных создан");
    }
}


