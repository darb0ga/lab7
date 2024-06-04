package com.darb0ga.common.commands;


import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.Commander;
import com.darb0ga.common.managers.DBManager;
import com.darb0ga.common.util.Reply;

import java.util.Collection;
import java.util.Scanner;

/**
 * Команда 'help'. Выводит справку по доступным командам
 *
 * @author darya
 */
public class Help extends Command {
    public Help() {
        super("help", "вывести справку по доступным командам", false);
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile, DBManager manager) throws IllegalParamException {
        Reply reply = new Reply();
        if (!args.isBlank()) throw new IllegalParamException("*ничего*");
        Commander commander = new Commander();
        Collection<Command> commands = commander.getCommands().values();

        reply.addResponse("Доступны команды:");
        for (Command command : commands) {
            reply.addResponse(command.getName() + ": " + command.getInfo());
        }
        return reply;
    }

}
