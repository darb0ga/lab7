package com.darb0ga.common.commands;


import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.collection.Models.AskLabWork;
import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.exceptions.NoSuchIDException;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.util.Reply;


import java.util.Scanner;

/**
 * Команда 'update_id'. Обновляет значение элемента коллекции, id которого равен заданному
 *
 * @author darya
 */

public class UpdateID extends Command {
    public UpdateID() {
        super("update_id", "обновить значение элемента коллекции, id которого равен заданному");
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile) throws IllegalParamException, NoSuchIDException {
        Reply reply = new Reply();
        try {
            LabWork lab = CollectionManager.findById(Integer.parseInt(args.trim()));
            if (lab != null) {
                CollectionManager.updateById(new AskLabWork().build(scan, isFile), Integer.parseInt(args.trim()));
            }
        } catch (NumberFormatException e) {
            reply.addResponse(new IllegalParamException("int").getMessage());
        }
        return reply;
    }
}
