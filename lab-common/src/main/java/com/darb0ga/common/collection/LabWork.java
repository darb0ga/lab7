package com.darb0ga.common.collection;

import com.darb0ga.common.managers.DateAdapter;
import com.darb0ga.common.managers.IDGenerator;
import com.darb0ga.common.managers.Validatable;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Класс лабработы
 * @author darya
 */
@Getter
@Setter
@XmlRootElement(name = "labwork")
@XmlAccessorType(XmlAccessType.FIELD)
public class LabWork implements Validatable, Serializable {
    /**
     * id
     */
    @XmlElement(name="id", required = true)
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    /**
     * Название лабораторной
     */
    @XmlElement(name="name", required = true)
    private String name; //Поле не может быть null, Строка не может быть пустой
    /**
     * Координаты работы
     */
    @XmlElement(name="coordinates", required = true)
    private Coordinates coordinates; //Поле не может быть null
    /**
     * Дата создания работы
     */
    @XmlElement(name = "creationDate", required = true)
    @XmlJavaTypeAdapter(DateAdapter.class)
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    /**
     * Минимальное значение прогресса в работе
     */
    @XmlElement(name="minimalpoint", required = true)
    private float minimalPoint; //Значение поля должно быть больше 0
    /**
     * Сложность работы
     */
    @XmlElement(name="difficulty", required = true)
    private Difficulty difficulty; //Поле не может быть null
    /**
     * Создатель работы
     */
    @XmlElement(name="person", required = true)
    private Person author; //Поле не может быть null

    public LabWork(String name, Coordinates coordinates, float minimalPoint, Difficulty difficulty, Person author){
        this.id = IDGenerator.generate();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.minimalPoint = minimalPoint;
        this.difficulty = difficulty;
        this.author = author;
    }
    public LabWork(int id, String name, Coordinates coordinates, Date creationDate, float minimalPoint, Difficulty difficulty, Person author){
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.minimalPoint = minimalPoint;
        this.difficulty = difficulty;
        this.author = author;

    }

    public LabWork(){}

    @Override
    public String toString() {
        return "LabWork{id: " + id + ", " +
                "name: " + name + ", " +
                "coordinates: " + coordinates + ", " +
                "creationDate: " + creationDate + ", " +
                "minimalPoint: " + minimalPoint + ", " +
                "difficulty: " + difficulty + ", " +
                "author: " +  author + "}";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabWork that = (LabWork) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, minimalPoint, difficulty, author);
    }

    @Override
    public int compareTo(LabWork o) {
        return Float.compare(minimalPoint, o.getMinimalPoint());
    }
}
