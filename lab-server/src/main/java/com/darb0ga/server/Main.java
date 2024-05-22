package com.darb0ga.server;


import java.io.IOException;
import java.net.SocketException;

public class Main {
    public static void main(String[] args){
        if (args.length != 0) {
            System.out.println("Необходимо указать имя файла. Невозможно запустить приложение");
            System.exit(1);
            // так же надо переделать filter_starts_with_name and add_if_min and execute_script and remove_greater
            //in add_if_min почему то говорит что обьект сохранен после след команды
            //remove_greater - почему то при попытке удалитт набор подходящий сервер ложиться
            //filter_starts_with_name - ну тут просто без коммов
            //execute_script - .... вот и помер дед максииим

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
