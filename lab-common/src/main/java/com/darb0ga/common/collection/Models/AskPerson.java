package com.darb0ga.common.collection.Models;


import com.darb0ga.common.collection.Location;
import com.darb0ga.common.collection.Person;

import java.util.Scanner;

/**
 * Класс для создания человека
 *
 * @author darya
 */

public class AskPerson extends Model<Person> {

    @Override
    public Person build(Scanner scan, boolean isFile) {
        if (!isFile) {
            console.println("Введите имя человека: ");
        }
        String name = scan.nextLine().trim();
        double h = 0;
        Location loc = null;
        Boolean ask_height = askBoolean("Хотите ли вводить рост человека?", scan, isFile);
        if (ask_height) {
            h = askDouble("рост человека", scan, isFile);
        }
        Boolean ask_loc = askBoolean("Хотите ли вводить локацию человека?", scan, isFile);
        if (ask_loc) {
            loc = new AskLocation().build(scan, isFile);
        }
        if (!(h == 0) & !(loc == null)) {
            return new Person(name, h, loc);
        } else if (!(h == 0)) {
            return new Person(name, h);
        } else if (!(loc == null)) {
            return new Person(name, loc);
        }
        return new Person(name);


    }
}
