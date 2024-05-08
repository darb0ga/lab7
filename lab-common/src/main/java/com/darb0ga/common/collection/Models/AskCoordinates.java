package com.darb0ga.common.collection.Models;


import com.darb0ga.common.collection.Coordinates;

import java.util.Scanner;

/**
 * Класс создания коорднат
 * @author darya
 */
public class AskCoordinates extends Model<Coordinates> {
    @Override
    public Coordinates build(Scanner scan, boolean isFile) {
        float x = askFloat("Float координата x", false, scan, isFile);
        int y = askInteger("Integer координата y", scan, isFile);
        return new Coordinates(x, y);
    }
}
