package com.darb0ga.common.commands;

import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.DBManager;
import com.darb0ga.common.util.Reply;

import java.util.Scanner;

public class Register extends Command {
    public Register() {
        super("register", "зарегестироваться", false);
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile, DBManager man) throws IllegalParamException {
        if (!args.isBlank()) throw new IllegalParamException("UserName & password");
        Reply response = new Reply();
        if (man.findUser(getRequestOwner().getName().trim())>0){
            response.addResponse("Такой пользователь уже существует. Воспользуйтесь командой login");
            return response;
        }
        try {
            man.addUser(getRequestOwner());
        } catch (Exception e) {
            Reply response1 = man.reconnect(response, 1);
            if (response1==null){
                return execute(args, scan, isFile, man);
            } else {
                return response1;
            }
        }
        response.addResponse("Создан новый аккаунт.");
        response.addResponse("Выполнен вход в аккаунт.");

        return response;
    }
}
