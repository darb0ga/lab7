package com.darb0ga.common.commands;

import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.util.Reply;

import java.util.Objects;
import java.util.Scanner;

/**
 * Команда 'add_if_min'. Добавляет новый элемент коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
 *
 * @author darya
 */
public class AddIfMin extends Command {
    public AddIfMin() {
        super("add_if_min", "добавить новый элемент коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile) throws IllegalParamException {
        Reply reply = new Reply();
        if (!args.isBlank()) throw new IllegalParamException("*ничего*");
        try {
            LabWork newElement = getAssertNewLab();
            reply.addResponse("Создание объекта LabWork окончено успешно!");
            if (newElement.compareTo(Objects.requireNonNull(CollectionManager.getCollection().stream()
                    .filter(Objects::nonNull)
                    .min(LabWork::compareTo)
                    .orElse(null))) <= 0) {
                CollectionManager.addElement(newElement);
                reply.addResponse("Объект успешно добавлен");
            } else {
                reply.addResponse("Элемент больше минимального! Невозможно добавить(");
            }
        } catch (Exception invalidForm) {
            System.out.println(invalidForm.getMessage());
            reply.addResponse("Поля объекта не валидны! Объект не создан!");
        }
        return reply;
    }
}
