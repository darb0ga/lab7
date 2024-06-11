package com.darb0ga.common.commands;

import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.DBManager;
import com.darb0ga.common.util.Reply;


import java.util.Scanner;
import java.util.Set;

/**
 * Команда 'show'. Выводит в стандартный поток вывода все элементы коллекции в строковом представлении
 *
 * @author darya
 */
public class Show extends Command {
    public Show() {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении", false);
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile, DBManager manager) throws IllegalParamException {
        Reply reply = new Reply();
        if (!args.isBlank()) throw new IllegalParamException("*ничего*");
        if (manager.getMyLabs().isEmpty()) {
            reply.addResponse("Коллекция пустая");
            return reply;
        }
        reply.addResponse("Элементы коллекции: ");
        for (LabWork s : manager.getMyLabs()) {
            reply.addResponse(s.toString());
        }
        return reply;
    }
}
