package com.darb0ga.common.commands;

import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.CollectionManager;
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
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile) throws IllegalParamException {
        Reply reply = new Reply();
        if (!args.isBlank()) throw new IllegalParamException("*ничего*");
        Set<LabWork> collection = CollectionManager.getCollection();
        if (collection.isEmpty()) {
            reply.addResponse("Коллекция пустая");
            return reply;
        }
        reply.addResponse("Элементы коллекции: ");
        for (LabWork s : collection) {
            try {
                reply.addResponse(s.toString());
            } catch (Exception e) {
                reply.addResponse(e.getMessage());
            }
        }
        return reply;
    }
}
