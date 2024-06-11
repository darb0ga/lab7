package com.darb0ga.common.commands;


import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.*;
import com.darb0ga.common.util.Reply;

import java.util.Scanner;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 * @author darya
 */
public class Add extends Command{
    public Add(){
        super("add", "добавить новый элемент в коллекцию", true);
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile, DBManager manager) throws IllegalParamException {
        Reply reply = new Reply();
        if (!args.isBlank()) throw new IllegalParamException("*ничего*");
        try {
            LabWork lab0 = getAssertNewLab();
            manager.modificateElement(lab0, getRequestOwner(), false, -1);
            reply.addResponse("Создание объекта LabWork окончено успешно!");
        } catch (Exception e) {
            reply.addResponse(e.getMessage());
        }
        return reply;
    }
}
