package com.darb0ga.common.collection.Models;


import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.StandardConsole;

import java.util.Scanner;

/**
 * Абстрактный класс чтения объекта
 *
 * @author darya
 */
public abstract class Model<T> {

    protected StandardConsole console = new StandardConsole(false);


    public abstract T build(Scanner scan, boolean isFile);

    public Integer askInteger(String name, Scanner scan, boolean isFile) {
        while (true) {
            if (!isFile) {
                console.println("Введите " + name + ": ");
            }
            String input = scan.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                console.printError(name + " должно быть числом типа Integer!");
            }
        }
    }

    public String askString(String name, Scanner scan, boolean isFile) {
        while (true) {
            if (!isFile) {
                console.println("Введите " + name + ": ");
            }
            String word = scan.nextLine().trim();
            if (word.isBlank()) {
                if (isFile) {
                    throw new IllegalParamException("Введено недопустимое значение.");
                }
                console.println("Строка не может быть пустой! Попробуйте еще раз.");
            } else {
                return word;
            }
        }
    }

    public Boolean askBoolean(String name, Scanner scan, boolean isFile) {
        while (true) {
            if (!isFile) {
                console.println(name + "(true/false): ");
            }
            String input = scan.nextLine().trim();
            try {
                if (input.equals("true")) {
                    return true;
                } else if (input.equals("false")) {
                    return false;
                }
            } catch (NumberFormatException e) {
                console.printError(name + " должно быть полем типа Boolean!");
            }
        }
    }

    public Float askFloat(String name, Boolean MoreThanZero, Scanner scan, boolean isFile) {
        while (true) {
            if (!isFile) {
                console.println("Введите " + name + ": ");
            }
            String input = scan.nextLine().trim();
            try {
                Float num = Float.parseFloat(input);
                if (MoreThanZero) {
                    if (num > 0) return num;
                } else {
                    return num;
                }
            } catch (NumberFormatException e) {
                console.printError(name + " должно быть числом типа Float!");
            }
        }
    }

    public Double askDouble(String name, Scanner scan, boolean isFile) {
        while (true) {
            if (!isFile) {
                console.println("Введите " + name + ": ");
            }
            String input = scan.nextLine().trim();
            if (!input.equals("")) {
                try {
                    double num = Double.parseDouble(input);
                    if (num <= 0) throw new IllegalArgumentException();
                    return num;
                } catch (NumberFormatException e) {
                    console.printError(name + " должно быть числом типа Double!");
                }
            } else {
                return null;
            }
        }
    }

    public <S extends Enum<S>> Enum<S> askEnum(String name, Enum<S>[] values, Scanner scan, boolean isFile) {
        while (true) {
            if (!isFile) {
                console.println("Введите " + name + ": ");
                for (Enum<S> value : values) {
                    console.print(value + " ");
                }
                console.print("\n");
            }
            String str = scan.nextLine().trim();
            try {
                for (Enum<S> value : values) {
                    if (value.toString().equals(str)) {
                        return value;
                    }
                }
                console.printError(str + " - такого значения нет в списке!");
            } catch (IllegalArgumentException e) {
                console.printError(str + " - неверное значение поля!");
            }
        }
    }
}