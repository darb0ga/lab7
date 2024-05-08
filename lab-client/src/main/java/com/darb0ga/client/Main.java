package com.darb0ga.client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.run();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
