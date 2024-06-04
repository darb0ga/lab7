package com.darb0ga.common.collection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;

/**
 * Класс локации
 * @author darya
 */

public class Location implements Serializable {
    /**
     * локация по x
     */
    @Getter
    @Column(nullable = false)
    @NonNull
    private Integer x; //Поле не может быть null
    /**
     * локация по y
     */
    @Getter
    @Column(nullable = false)
    @NonNull
    private Float y; //Поле не может быть null
    /**
     * название локации
     */
    @Getter
    @Column(nullable = true)
    @NonNull
    private String name; //Поле может быть null

    public Location(int x, float y, String name){
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public Location(int x, float y){
        this.x = x;
        this.y = y;
        this.name = null;
    }
    public Location(){}
    @Override
    public String toString() {
        if (name != null) {
            return "Location{x: " + x + ", " +
                    "y: " + y + ", " +
                    "name: " + name + "}";
        }
        return "Location{x: " + x + ", " +
                "y: " + y + "}";
    }
}
