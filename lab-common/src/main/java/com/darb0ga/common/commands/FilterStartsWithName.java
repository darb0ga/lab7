package com.darb0ga.common.commands;

import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.managers.DBManager;
import com.darb0ga.common.util.Reply;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Команда 'filter_starts_with_name'. Выводит элементы, значение поля name которых начинается с заданной подстроки
 *
 * @author darya
 */
public class FilterStartsWithName extends Command {
    public FilterStartsWithName() {
        super("filter_starts_with_name", "вывести элементы, значение поля name которых начинается с заданной подстроки", false);
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile, DBManager manager) {
        Reply reply = new Reply();
        if (args.isBlank()) {
            reply.addResponse(new IllegalParamException("String").toString());
            return reply;
        }
        List<LabWork> coll = new LinkedList<>();
        manager.getCollection().stream()
                .filter(sp -> sp.getName().startsWith(args.trim())).forEach(coll::add);
        for(LabWork el: coll){
            reply.addResponse(el.toString());
        }
        return reply;

    }
}
