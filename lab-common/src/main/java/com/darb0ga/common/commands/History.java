package com.darb0ga.common.commands;

import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.Commander;
import com.darb0ga.common.util.Reply;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Команда 'history'. Выводит последние 14 команд(без их аргументов)
 * @author darya
 */
public class History extends Command {
    public History(){
        super("history", "вывести последние 14 команд(без их аргументов)");
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile) throws IllegalParamException{
        Reply reply = new Reply();
        if (!args.isBlank()) throw new IllegalParamException("*ничего*");
        ArrayList<String> history = Commander.getCommandHistory();
        if (!history.isEmpty()){
            reply.addResponse("Введенные команды: ");
            for (String command: history.subList(Math.max(0, history.size() - 14), history.size())){
                reply.addResponse(command);
            }
        } else {
            reply.addResponse("Вы еще не ввели ни одной команды!");
        }
        return reply;
    }
}
