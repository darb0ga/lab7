package com.darb0ga.server;


import java.io.IOException;
import java.net.SocketException;

public class Main {
    public static void main(String[] args){
        if (args.length != 0) {
            System.out.println("Необходимо указать имя файла. Невозможно запустить приложение");
            System.exit(1);
            }
        try {
            Server servach = new Server();
            servach.run();
        }catch (SocketException e){
            System.exit(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
