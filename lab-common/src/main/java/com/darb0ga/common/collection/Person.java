package com.darb0ga.common.collection;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Класс человека
 * @author darya
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements Serializable {

    @XmlElement(name = "name", required = true)
    private String name; //Поле не может быть null, Строка не может быть пустой
    /**
     * Рост человека
     */
    @XmlElement(name = "height", required = true, nillable = true)
    private Double height; //Поле может быть null, Значение поля должно быть больше 0
    /**
     * Локация человека
     */
    @XmlElement(name = "location", required = true, nillable = true)
    private Location location; //Поле может быть null

    public Person(String name, double height, Location location) {
        this.name = name;
        this.height = height;
        this.location = location;
    }

    public Person(String name) {
        this.name = name;
        this.height = null;
        this.location = null;
    }
    public Person(String name, double height) {
        this.name = name;
        this.height = height;
        this.location = null;
    }
    public Person(String name, Location loc) {
        this.name = name;
        this.height = null;
        this.location = loc;
    }
    public Person(){}

    @Override
    public String toString() {
        if (location == null & !(height == null)) {
            return "Person{name: " + name + ", " +
                    "height: " + height + "}";
        } else if (height == null& location==null) {
            return "Person{name: " + name + "}";
        } else if (height == null) {
            return "Person{name: " + name + ", " +
                    "location: " + location + "}";
        }
        return "Person{name: " + name + ", "+
                "height: " + height + ", " +
                "location: " + location + "}";
    }

}
