package com.darb0ga.common.commands;

import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.util.Reply;

import java.util.Scanner;

/**
 * Команда 'min_by_creation_date'. Выводит любой объект из коллекции, значение поля creationDate которого является минимальным
 *
 * @author darya
 */
public class MinByCreationDate extends Command {
    public MinByCreationDate() {
        super("min_by_creation_date", "вывести любой объект из коллекции, значение поля creationDate которого является минимальным");
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile) throws IllegalParamException {
        Reply reply = new Reply();
        if (!args.isBlank()) throw new IllegalParamException("*ничего*");
        if (CollectionManager.getCollection().isEmpty()) {
            reply.addResponse("Коллекция пуста.");
        } else {
            LabWork minDate = null;
            for (LabWork lab : CollectionManager.getCollection()) {
                if ((minDate == null) || (lab.getCreationDate().compareTo(minDate.getCreationDate())) < 0) {
                    minDate = lab;
                }
            }
            if (minDate == null) {
                reply.addResponse("Минимальной даты не обнаружено.");
            } else {
                reply.addResponse("Минимальная дата: " + minDate.getCreationDate() + ". Элемент: " + minDate);
            }
        }
        return reply;
    }
}
