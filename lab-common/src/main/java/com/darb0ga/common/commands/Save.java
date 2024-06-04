package com.darb0ga.common.commands;


import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.managers.DBManager;
import com.darb0ga.common.util.Reply;

import java.util.Scanner;

/**
 * Команда 'save'. Сохраняет коллекцию в файл
 * @author darya
 */
public class Save extends Command{
    public Save(){
        super("save", "сохранить коллекцию в файл", false);
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile, DBManager manager) {
        return null;
    }
}
