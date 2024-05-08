package com.darb0ga.common.commands;

import com.darb0ga.common.commands.Command;
import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.util.Reply;

import java.util.Scanner;

/**
 * Команда 'exit'. Завершает программу(без сохранения в файл)
 * @author darya
 */
public class Exit extends Command {
    public Exit() {
        super("exit", "завершить программу(без сохранения в файл)");
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile) throws IllegalParamException{
        if (!args.isBlank()) throw new IllegalParamException("*ничего*");
        System.out.println("Выход из программы");
        System.exit(0);
        return new Reply();
    }
}
