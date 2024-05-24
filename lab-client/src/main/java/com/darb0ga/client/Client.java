package com.darb0ga.client;

import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.collection.Models.AskLabWork;
import com.darb0ga.common.commands.*;
import com.darb0ga.common.managers.Commander;
import com.darb0ga.common.util.Reply;
import com.darb0ga.common.util.Serializer;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class Client {
    private Commander commander = new Commander();
    private ScriptManager scriptExecution;
    private final DatagramChannel channel;
    private final InetSocketAddress serverAddress;
    private final Selector selector;

    public Client() throws IOException {
        scriptExecution = new ScriptManager(this);
        channel = DatagramChannel.open();
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
        Scanner scan = new Scanner(System.in);
        while (true) {
            String comm = scan.nextLine();
            Command command = CommandBuilder(comm);
            if(command == null){
                continue;
            }
            try {
                if (command instanceof Add || command instanceof AddIfMin || command instanceof UpdateID || command instanceof RemoveGreater) {
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
                    scriptExecution.executeFile(comm.trim().split(" ")[1]);
                    //continue;
                } else if (command != null) {
                    sendCommand(command);
                }

                TimeUnit.MILLISECONDS.sleep(20);
                Reply answer = receive(buffer);
                for (String element : answer.getResponse()) {
                    System.out.println(element);
                }
            }catch(ArrayIndexOutOfBoundsException ex){
                System.out.println("Произошли ошибки при работе с аргументами");
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                buffer = ByteBuffer.allocate(10_000);
            }
        }


    }
    public void sendCommand(Command command) throws IOException {
        Serializer serializer = new Serializer();
        byte[] buffer = serializer.serialize(command);
        channel.send(ByteBuffer.wrap(buffer), serverAddress);
    }

    public Reply receive(ByteBuffer buffer) {
        Serializer serializer = new Serializer();
        Reply reply = new Reply();
        buffer.clear();
        try {
            channel.receive(buffer);
            return serializer.deserialize(buffer.array());
        } catch (Exception e) {
            return reply;
        }
    }
}
