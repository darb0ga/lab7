package com.darb0ga.common.collection.Models;

import java.util.Scanner;
import com.darb0ga.common.collection.Location;
/**
 * Класс для создания локации
 *
 * @author darya
 */
public class AskLocation extends Model<Location> {
    @Override
    public Location build(Scanner scan, boolean isFile) {
        int a = askInteger("Integer координата y(локация)", scan, isFile);
        float b = askFloat("Float координата x(локация)", false, scan, isFile);
        Boolean ask = askBoolean("Хотите ли вводить наименование локации?", scan, isFile);
        if (ask) {
            if(!isFile) {
                console.println("Введите имя локации: ");
            }
            String name = scan.nextLine().trim();
            return new Location(a, b, name);
        } else {
            return new Location(a, b);
        }
    }
}
