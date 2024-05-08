package com.darb0ga.common.commands;

import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.util.Reply;

import java.util.Scanner;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции по его id
 * @author darya
 */
public class RemoveByID extends Command{
    public RemoveByID(){
        super("remove_by_id", "удалить элемент из коллекции по его id");
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile) throws IllegalParamException {
        Reply reply = new Reply();
        try {
            int id = Integer.parseInt(args.trim());
            CollectionManager.removeById(id);
        } catch (NumberFormatException e) {
            throw new IllegalParamException("int");
        }
        return reply;
    }
}
