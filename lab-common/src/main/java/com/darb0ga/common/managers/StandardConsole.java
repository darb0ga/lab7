package com.darb0ga.common.managers;

import lombok.Getter;
import lombok.Setter;

import java.util.Scanner;

/**
 * Класс с методами консоли и указателем на режим аботы программмы
 * @author darya
 */
public class StandardConsole {
    @Getter
    @Setter
    private boolean fileMode;
    private Scanner scanner;

    public void print(Object obj) {
        System.out.print(obj);
    }

    public void println(Object obj) {
        System.out.println(obj);
    }

    public void printError(Object obj) {
        System.err.println("Error: " + obj);
    }

    public StandardConsole(Boolean fileMode){
        this.scanner = new Scanner(System.in);
        this.fileMode = fileMode;
    }
    public StandardConsole(Scanner scanner, Boolean fileMode){
        this.scanner = scanner;
        this.fileMode = fileMode;
    }

    public String next() {
        try {
            return scanner.next();
        } catch (Exception e) {
            printError("Ну все пиздец в консоли че то плохо со след строкой");
            return null;
            //isClosed = true;
            //throw new InterationClosedException();
        }
    }


}
