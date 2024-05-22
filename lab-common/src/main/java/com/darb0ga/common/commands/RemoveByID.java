package com.darb0ga.common.commands;

import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.util.Reply;

import java.util.NoSuchElementException;
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
            if(CollectionManager.removeById(id)){
                reply.addResponse("Элемент с id " + args.trim() + " удален");
            }else{
                reply.addResponse("Элемент с id " + args.trim() + " отсутствует в коллекции");
            }
        } catch (NumberFormatException | NoSuchElementException e) {
            reply.addResponse("Ошибка при чтении id");
        }
        return reply;
    }
}
