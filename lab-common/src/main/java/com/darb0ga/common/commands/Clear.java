package com.darb0ga.common.commands;

import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.managers.DBManager;
import com.darb0ga.common.util.Reply;

import java.util.Scanner;

/**
 * Команда 'clear'. Очищает коллекцию
 * @author darya
 */
public class Clear extends Command{
    public Clear(){
        super("clear", "очистить коллекцию", false);
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile, DBManager manager) throws IllegalParamException{
        if (!args.isBlank()) throw new IllegalParamException("*ничего*");
        Reply reply = new Reply();
        if (manager.getMyLabs().isEmpty()) {
            reply.addResponse("Коллекция и так пуста.");
            return reply;
        } else {
            for (LabWork labs: manager.getMyLabs()){
                if (getRequestOwner().getID()!=(labs.getOwner_id())){
                    manager.remove(labs);
                }
            }
            reply.addResponse("Коллекция очищена. Удалены только элементы, принадлежащие Вам.");
            return reply;
        }
    }
}
