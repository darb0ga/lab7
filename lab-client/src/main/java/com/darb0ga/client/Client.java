package com.darb0ga.client;

import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.collection.Models.AskLabWork;
import com.darb0ga.common.commands.*;
import com.darb0ga.common.exceptions.CommandRuntimeException;
import com.darb0ga.common.managers.Commander;
import com.darb0ga.common.util.Reply;
import com.darb0ga.common.util.Serializer;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class Client {
    private final UUID clientID;
    private Commander commander = new Commander();
    private ScriptManager scriptExecution;
    private final DatagramChannel channel;
    private final InetSocketAddress serverAddress;
    private final Selector selector;

    public Client() throws IOException {
        scriptExecution = new ScriptManager(this);
        channel = DatagramChannel.open();
        clientID = UUID.randomUUID();
        serverAddress = new InetSocketAddress(InetAddress.getLocalHost(), 2227);
        channel.configureBlocking(false);
        selector = Selector.open();
    }

    public Command CommandBuilder(String args) throws IOException {

        String name = args.split(" ")[0];
        String addition = "";
        if (args.split(" ").length > 1) {
            addition = args.split(" ")[1];
        }
        try {
            Command comm = commander.getCommands().get(name);
            comm.setAddition(addition);
            return comm;
        } catch (Exception e) {
            System.err.println("Произошли ошибки при работе с введенной командой: " + name);
        }
        return null;
    }


    public void run() throws IOException, InterruptedException {
        channel.socket().bind(null);
        channel.register(selector, SelectionKey.OP_READ);
        ByteBuffer buffer = ByteBuffer.allocate(10_000);
        Reply answer = null;
        Scanner scan = new Scanner(System.in);
        String comm = null;
        while (true) {
            comm = scan.next();
            try {
                Command command = CommandBuilder(comm);
                if (command instanceof Add || command instanceof AddIfMin || command instanceof UpdateID || command instanceof RemoveByID) {
                    AskLabWork newLaba = new AskLabWork();
                    try {
                        LabWork laba = newLaba.build(scan, false);
                        command.setAssertNewLab(laba);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                if (command instanceof Exit) {
                    command.execute("", null, false);

                }
                if (command instanceof ExecuteScript) {
                    assert comm != null;
                    scriptExecution.executeFile(comm.trim().split(" ")[1]);
                    //continue;

                } else if (command != null) {
                    sendCommand(command);
                }

            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                System.out.println(e.getMessage());
                continue;
            }
            TimeUnit.SECONDS.sleep(1);
            //урааа вроде как починили дело в задержке было сука как знала
            answer = receive(buffer);
            for (String element : answer.getResponse()) {
                System.out.println(element);
            }
            // ну тут надо как то очисить буффер после пред смс а то плохо как то
            // ну и убрать вывод с клиента и сервера
        }


    }
    public void sendCommand(Command command) throws IOException {
        Serializer serializer = new Serializer();
        byte[] buffer = serializer.serialize(command);
        channel.send(ByteBuffer.wrap(buffer), serverAddress);
        System.out.println("отправили" + command);
    }

    public Reply receive(ByteBuffer buffer) {
        Serializer serializer = new Serializer();
        Reply reply = new Reply();
        buffer.clear();
        try {
            SocketAddress address = channel.receive(buffer);
            Reply message = serializer.deserialize(buffer.array());
            return message;
        } catch (Exception e) {
            return reply;
        }
    }
}
