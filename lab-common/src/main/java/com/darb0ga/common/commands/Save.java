package com.darb0ga.common.commands;


import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.util.Reply;

import java.util.Scanner;

/**
 * Команда 'save'. Сохраняет коллекцию в файл
 * @author darya
 */
public class Save extends Command{
    public Save(){
        super("save", "сохранить коллекцию в файл");
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile) {
        try {
            CollectionManager.saveCollection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new Reply();
    }
}
