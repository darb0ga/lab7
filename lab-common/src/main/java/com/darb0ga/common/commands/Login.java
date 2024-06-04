package com.darb0ga.common.commands;

import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.DBManager;
import com.darb0ga.common.util.Reply;

import java.io.Serializable;
import java.util.Scanner;

public class Login extends Command{
    public Login() {
        super("login", "войти в аккаунт", false);
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile, DBManager man) throws IllegalParamException {
        if (args.isBlank()) throw new IllegalParamException("UserName & password");

        return new Reply();
    }
}
