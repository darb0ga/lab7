package com.darb0ga.common.collection;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Класс локации
 * @author darya
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Location implements Serializable {
    /**
     * локация по x
     */
    @XmlElement(name="x", required = true)
    private Integer x; //Поле не может быть null
    /**
     * локация по y
     */
    @XmlElement(name="y", required = true)
    private Float y; //Поле не может быть null
    /**
     * название локации
     */
    @XmlElement(name="name", required = true, nillable = true)
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
