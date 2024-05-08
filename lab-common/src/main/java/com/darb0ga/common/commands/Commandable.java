package com.darb0ga.common.commands;


import com.darb0ga.common.exceptions.*;
import com.darb0ga.common.util.Reply;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Интерфейс всех команд
 *
 * @author darya
 */
public interface Commandable {
    String getName();
    Reply execute(String arg, Scanner scan, boolean isFile) throws CommandRuntimeException, FileNotFoundException;
    String getInfo();
}
