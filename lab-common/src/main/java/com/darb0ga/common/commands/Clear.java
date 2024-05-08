package com.darb0ga.common.commands;

import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.util.Reply;

import java.util.Scanner;

/**
 * Команда 'clear'. Очищает коллекцию
 * @author darya
 */
public class Clear extends Command{
    public Clear(){
        super("clear", "очистить коллекцию");
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile) throws IllegalParamException{
        if (!args.isBlank()) throw new IllegalParamException("*ничего*");
        CollectionManager.getCollection().clear();
        return new Reply();
    }
}
