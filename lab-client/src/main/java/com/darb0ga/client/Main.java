package com.darb0ga.client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client("127.0.0.1"); //change ip
            client.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
