package com.darb0ga.common.exceptions;

import java.io.IOException;

/**
 * Класс исключения для ошибки во время выполнения команды
 *
 * @author darya
 */
public class CommandRuntimeException extends IOException {
    public CommandRuntimeException(String message){
        super("Ошибка в работе с введенной командой");
    }
}