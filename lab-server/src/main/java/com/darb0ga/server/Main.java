package com.darb0ga.server;


import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        if (args.length != 0) {
            System.out.println("Необходимо указать имя файла. Невозможно запустить приложение");
            System.exit(1);
            }
        try {
            Server servach = new Server("admin");
            servach.start();
        }catch (SocketException e){
            System.exit(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
