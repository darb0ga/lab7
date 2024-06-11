package com.darb0ga.common.commands;

import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.managers.DBManager;
import com.darb0ga.common.util.Reply;

import java.util.Scanner;

/**
 * Команда 'info'. Выводит в стандартный поток вывода информацию о коллекции
 * @author darya
 */
public class Info extends Command {
    public Info() {
        super("info", "вывести в стандартный поток вывода информацию о коллекции", false);
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile, DBManager manager) throws IllegalParamException{
        Reply reply = new Reply();
        if (!args.isBlank()) throw new IllegalParamException("*ничего*");
        if (manager.getMyLabs().isEmpty()){
            reply.addResponse("Коллекция пока что пуста. Тип коллекции: " + CollectionManager.getCollection().getClass());
        }
        else {
            reply.addResponse("Тип коллекции: " + manager.getMyLabs().getClass());
            reply.addResponse("Количество элементов: " + manager.getMyLabs().size());
        }
        return reply;
    }
}
