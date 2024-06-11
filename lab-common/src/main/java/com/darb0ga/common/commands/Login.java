package com.darb0ga.common.commands;

import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.DBManager;
import com.darb0ga.common.util.Reply;

import java.util.Scanner;

public class Login extends Command{
    public Login() {
        super("login", "войти в аккаунт", false);
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile, DBManager man) throws IllegalParamException {
        if (!args.isBlank()) throw new IllegalParamException("UserName & password");
        Reply reply = new Reply();
        try {
            if(man.checkPassword(getRequestOwner())){
                reply.addResponse("Выполнен вход в аккаунт");
                return reply;
            }
       } catch (Exception e) {
            Reply response1 = man.reconnect(reply, 1);
            response1.addResponse(e.getMessage());
            if (response1==null){
                return execute(args, scan, isFile, man);
            } else {
                return response1;
            }
        }
        reply.addResponse("Пароль не соответствует паролю, указанному при регистрации.");

        return reply;
    }
}
