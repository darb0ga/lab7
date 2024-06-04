package com.darb0ga.server;

import com.darb0ga.common.commands.Command;
import com.darb0ga.common.commands.History;
import com.darb0ga.common.exceptions.CommandRuntimeException;
import com.darb0ga.common.util.Header;
import com.darb0ga.common.util.Packet;
import com.darb0ga.common.util.Reply;
import com.darb0ga.common.util.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ClientCommunication {
    private static final int BUFFER_LENGTH = 10_000;
    private final DatagramSocket datagramSocket;
    private ArrayList<String> history = new ArrayList<>();
    private final Serializer serializer = new Serializer();
    public  ClientCommunication(DatagramSocket datagramSocket){
        this.datagramSocket = datagramSocket;
    }
    public Command readMessage(DatagramPacket datagramPacket, byte[] buffer) throws IOException {
        datagramSocket.receive(datagramPacket);
        Packet packet = serializer.deserialize(buffer);
        Header header = packet.getHeader();
        int countOfPieces = header.getBlocksCount();
        ArrayList<Packet> list = new ArrayList<>(countOfPieces);
        list.add(header.getBlockNumber(), packet);
        int k = 1;

        while (k<countOfPieces){
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
        } catch (Exception e){
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
            int countOfPieces = bufferLength/(BUFFER_LENGTH-headerLength);
            if (countOfPieces*(BUFFER_LENGTH-headerLength) < bufferLength){
                countOfPieces += 1;
            }
            for (int i=0; i<countOfPieces; i++){
                header = new Header(countOfPieces, i);
                headerLength = serializer.serialize(header).length + 200;
                Packet packet = new Packet(header, Arrays.copyOfRange(buffer, i*(BUFFER_LENGTH-headerLength), Math.min(bufferLength, (i+1)*(BUFFER_LENGTH-headerLength)) ));
                byte[] array = serializer.serialize(packet);
                DatagramPacket datagramPacket2 = new DatagramPacket(array, array.length, socketAddress);
                datagramSocket.send(datagramPacket2);
                Thread.sleep(100);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Reply commandExecution(Command command, boolean fileReading, Scanner scanner) {
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
                reply = command.execute(command.getAddition(), scanner, fileReading);
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
