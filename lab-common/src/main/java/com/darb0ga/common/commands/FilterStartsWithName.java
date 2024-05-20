package com.darb0ga.common.commands;

import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.util.Reply;

import java.util.Scanner;

/**
 * Команда 'filter_starts_with_name'. Выводит элементы, значение поля name которых начинается с заданной подстроки
 *
 * @author darya
 */
public class FilterStartsWithName extends Command {
    public FilterStartsWithName() {
        super("filter_starts_with_name", "вывести элементы, значение поля name которых начинается с заданной подстроки");
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile) {
        Reply reply = new Reply();
        if (args.isBlank()) throw new IllegalParamException("String");
        reply.addResponse(CollectionManager.getCollection().stream()
                .filter(sp -> sp.getName().startsWith(args.trim())).toString());
        return reply;

    }
}
