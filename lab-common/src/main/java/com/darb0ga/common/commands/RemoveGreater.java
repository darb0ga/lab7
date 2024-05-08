package com.darb0ga.common.commands;

import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.collection.Models.AskLabWork;
import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.CollectionManager;
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
        super("remove_greater", "удалить из коллекции все элементы, превышающие заданный");
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile) throws IllegalParamException {
        Reply reply = new Reply();
        if (!args.isBlank()) throw new IllegalParamException("*ничего*");
        try {
            LabWork newElement = new AskLabWork().build(scan, isFile);
            reply.addResponse("Создание заданного объекта для сравнения LabWork окончено успешно!");
            Collection<LabWork> toRemove = null;
            for (LabWork el : CollectionManager.getCollection()) {
                if (newElement.compareTo(el) < 0) {
                    toRemove.add(el);
                }
            }
            CollectionManager.removeElements(toRemove);
            reply.addResponse("Объекты успешно удалены");
        } catch (NumberFormatException e) {
            throw new IllegalParamException("int");
        }
        return reply;
    }
}
