package com.darb0ga.common.collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;

/**
 * Класс координат
 * @author darya
 */

public class Coordinates implements Serializable {
    /**
     * координата по x
     */
    @Getter
    @Column(nullable = false)
    @NonNull
    private Float x; //Поле не может быть null
    /**
     * координата по y
     */
    @Getter
    @Column(nullable = false)
    @NonNull
    private Integer y; //Поле не может быть null
    public Coordinates(float x, int y){
        this.x = x;
        this.y = y;
    }

    public Coordinates(){}

    @Override
    public String toString() {
        return "{" + x + "; " + y+ "}";
    }
}
