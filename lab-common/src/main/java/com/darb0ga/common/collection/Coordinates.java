package com.darb0ga.common.collection;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;

import java.io.Serializable;

/**
 * Класс координат
 * @author darya
 */
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinates implements Serializable {
    /**
     * координата по x
     */
    @XmlElement(name="x", required = true)
    private Float x; //Поле не может быть null
    /**
     * координата по y
     */
    @XmlElement(name="y", required = true)
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
