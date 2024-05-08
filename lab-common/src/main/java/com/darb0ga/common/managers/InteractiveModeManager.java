package com.darb0ga.common.managers;


import com.darb0ga.common.exceptions.*;

import java.util.Scanner;


/**
 * Класс обработки пользовательского ввода
 *
 * @author darya
 */
public class InteractiveModeManager {
    private final StandardConsole console;
    private final Commander commander;

    public InteractiveModeManager(StandardConsole console, Commander commandManager) {
        this.console = console;
        this.commander = commandManager;
    }

    public void interactiveMode() {
        Scanner userScanner = new Scanner(System.in);
        while (true) {
            try {
                if (!userScanner.hasNextLine()) {
                    System.exit(0);
                }
                String userCommand = userScanner.nextLine().trim();
                var comm = userCommand.split(" ");
                if (comm.length > 1) {
                    this.launch(comm[0], comm[1], userScanner, false);
                } else {
                    this.launch(comm[0], "", userScanner, false);
                }
                commander.addToHistory(comm[0]);
            } catch (NoSuchCommandException ex) {
                console.printError(ex.toString());
            } catch (IllegalParamException param) {
                console.printError(param);
            } catch (Exception exception) {
                console.printError(exception.getMessage());
            }
        }
    }

    public void launch(String comm, String args, Scanner scan, boolean isFile) throws NoSuchCommandException {
        if (comm.equals("")) return;
        var command = commander.getCommands().get(comm);
        if (command == null) {
            throw new NoSuchCommandException();
        }
        try {
            command.execute(args, scan, isFile);
            console.print("// Команда " + comm + " выполнена //" + '\n');
        } catch (Exception e) {
            console.printError(e);
        }
    }

}
