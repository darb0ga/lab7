package com.darb0ga.client;

import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.collection.Models.AskLabWork;
import com.darb0ga.common.commands.*;
import com.darb0ga.common.managers.ScannerManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ScriptManager {
    private Client client;

    public ScriptManager(Client client) {
        this.client = client;
    }

    public void executeFile(String file) {
        try {
            if (!(new File(file)).isFile()) {
                throw new IOException("Проблемы с чтением файла " + file);
            }
            ScannerManager.setFileScanner(file);
            Scanner scanner = new Scanner(new FileReader(file));
            String current_line;

            while (!(current_line = scanner.nextLine()).isEmpty()) {
                String[] command = current_line.split(" ");
                if (command[0].equals("execute_script")) {
                    if (ScannerManager.recurse(command[1])) {
                        throw new RuntimeException("Рекурсия! Повторно вызывается файл " + command[1]);
                    }
                }
                try {
                    Command currentCommand = client.CommandBuilder(current_line);
                    if (currentCommand instanceof Add || currentCommand instanceof AddIfMin || currentCommand instanceof UpdateID || currentCommand instanceof RemoveByID) {
                        AskLabWork newLaba = new AskLabWork();
                        try {
                            LabWork laba = newLaba.build(scanner, true);
                            currentCommand.setAssertNewLab(laba);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    if (currentCommand instanceof Exit) {
                        currentCommand.execute(null, null, true);

                    }
                    if (currentCommand instanceof ExecuteScript) {
                        executeFile(currentCommand.getAddition());
                    } else {
                        client.sendCommand(currentCommand);
//
//                        Response response = client.receive(ByteBuffer.allocate(10000));
//                        for (String element: response.getResponse()){
//                            System.out.println(element);

                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
