package com.darb0ga.common.exceptions;
/**
 * Исключение, которое пробрасывается при получении недопустимого значения
 *
 * @author darya
 */
public class IllegalParamException extends RuntimeException{
    public IllegalParamException(String type){
        super(type);
    }
    @Override
    public String toString() {
        return "Неверный формат введенного значения, необходимый тип: " + this.getMessage();
    }
}
