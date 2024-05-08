package com.darb0ga.server;


import com.darb0ga.common.commands.Command;
import com.darb0ga.common.exceptions.CommandRuntimeException;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.managers.Commander;
import com.darb0ga.common.util.Reply;
import com.darb0ga.common.util.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Server {
    private final int port = 2227;
    private final DatagramSocket datagramSocket;

    public Server() throws SocketException {
        datagramSocket = new DatagramSocket(port);
    }

    public Command readMessage(byte[] buffer, DatagramPacket datagramPacket) throws IOException {
        Serializer serializer = new Serializer();
        datagramSocket.receive(datagramPacket);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write((byte[]) buffer);
        return (Command) serializer.deserialize(byteStream.toByteArray());
    }

    public void run() throws IOException {
        byte[] bytes = new byte[10_000];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, datagramSocket.getInetAddress(), port);
        CollectionManager manager = new CollectionManager();
        manager.readCollection("1.xml");
        while (true) {
            Command command = readMessage(bytes, packet);
            Reply replyToClient = commandExecution(command, false, null);
            sendMessage(replyToClient, packet.getSocketAddress());
        }
    }

    private void sendMessage(Reply replyToClient, SocketAddress socketAddress) {
        try {
            Serializer serializer = new Serializer();
            byte[] buffer = serializer.serialize(replyToClient);
            byte[] array = serializer.serialize(buffer); //?
            DatagramPacket datagramPacket2 = new DatagramPacket(buffer, buffer.length, socketAddress);
            datagramSocket.send(datagramPacket2);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Reply commandExecution(Command command, boolean fileReading, Scanner scanner) {
        Reply reply = new Reply();
        if (!command.getAddition().isEmpty()) {
            try {
                reply = command.execute(command.getAddition(), scanner, fileReading);
                Commander.history.add(command.getName());
                System.out.println(Commander.getCommandHistory());
                return reply;
            } catch (FileNotFoundException | CommandRuntimeException e) {
                reply.addResponse(e.getMessage());
                return reply;
            }
        } else {
            try {
                reply = command.execute("", scanner, fileReading);
            } catch (FileNotFoundException | CommandRuntimeException ex) {
                reply.addResponse(ex.getMessage());
                return reply;
            }
        }
        return reply;
    }

}

