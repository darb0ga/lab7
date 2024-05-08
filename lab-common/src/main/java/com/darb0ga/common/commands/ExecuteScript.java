package com.darb0ga.common.commands;



import com.darb0ga.common.managers.Commander;
import com.darb0ga.common.managers.ScannerManager;
import com.darb0ga.common.util.Reply;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Команда 'execute_script'. Считывает и исполняет скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
 *
 * @author darya
 */
public class ExecuteScript extends Command {

    public ExecuteScript() {
        super("execute_script", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
    }
    @Override
    public Reply execute(String file, Scanner scan, boolean isFile) throws FileNotFoundException {
        try {
            if (!(new File(file).isFile())) {
                throw new IOException("/dev/null");
            }
            ScannerManager.setFileScanner(file);
            Scanner scanner = ScannerManager.getScanners().getLast();
            String current_line;

            while (!(current_line = scanner.nextLine()).isBlank()) {
                String[] command = current_line.split(" ");
                if (command[0].equals(getName())) {
                    if (ScannerManager.recurse(command[1])) {
                        throw new RuntimeException("Найдена рекурсия! Повторно вызывается файл " + command[1]);
                    }
                }

                try {
                    Commander.history.add(command[0]);
                    if (command.length > 1) {
                        Commander.execute(command[0], command[1], scanner, true);
                    } else {
                        Commander.execute(command[0], "", scanner, true);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            ScannerManager.getScanners().removeLast();
            ScannerManager.getPathFiles().removeLast();
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Reply();
    }
}
