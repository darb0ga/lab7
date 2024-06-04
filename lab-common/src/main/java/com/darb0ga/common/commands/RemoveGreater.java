package com.darb0ga.common.commands;

import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.managers.DBManager;
import com.darb0ga.common.util.Reply;

import java.util.Collection;
import java.util.Scanner;

/**
 * Команда 'remove_greater'. Удаляет элементы из коллекции, превосходящие данный
 *
 * @author darya
 */
public class RemoveGreater extends Command {
    public RemoveGreater() {
        super("remove_greater", "удалить из коллекции все элементы, превышающие заданный", true);
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile, DBManager manager) throws IllegalParamException {
        Reply reply = new Reply();
        if (!args.isBlank()) throw new IllegalParamException("*ничего*");
        try {
            LabWork newElement = getAssertNewLab();
            reply.addResponse("Создание заданного объекта для сравнения LabWork окончено успешно!");
            Collection<LabWork> toRemove = null;
            for (LabWork el : manager.getCollection()) {
                if (newElement.compareTo(el) < 0) {
                    toRemove.add(el);
                }
            }
            if (toRemove != null) {
                manager.removeElements(toRemove);
                reply.addResponse("Объекты успешно удалены");
            } else {
                reply.addResponse("Нет объектов, удовлетворяющих фильтру");
            }
        } catch (NumberFormatException e) {
            throw new IllegalParamException("int");
        }
        return reply;
    }
}
