package com.darb0ga.common.exceptions;
/**
 * Исключение, которое пробрасывается при отсутствии элементов с заданным id
 *
 * @author darya
 */
public class NoSuchIDException extends RuntimeException{
    public NoSuchIDException(){
        super("Несущствующий id");
    }

    @Override
    public String toString() {
        return "Нет элемента из коллекции с таким id";
    }
}
