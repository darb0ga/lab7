package com.darb0ga.server;


import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 0) {
            System.out.println("Необходимо указать имя файла. Невозможно запустить приложение");
            System.exit(1);
            }
        try {
            Class.forName("org.postgresql.Driver");
            Server servach = new Server();
            servach.run();
        }catch (SocketException e){
            System.exit(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            //System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
