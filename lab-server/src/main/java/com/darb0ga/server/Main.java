package com.darb0ga.server;

import com.darb0ga.common.managers.CollectionManager;

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
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
