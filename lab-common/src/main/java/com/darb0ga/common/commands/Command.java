package com.darb0ga.common.commands;

import com.darb0ga.common.collection.LabWork;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Абстрактный класс команда
 *
 * @author darya
 */
@Setter
@Getter
public abstract class Command implements Commandable, Serializable {
    private final String name;
    private String addition;
    private final String description;
    private LabWork assertNewLab;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getInfo() {
        return description;
    }

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        Command that = (Command) other;
        return (Objects.equals(getName(), that.getName()) && Objects.equals(getInfo(), that.getInfo()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + '{' + "name= " + this.getName() + ", description= " + this.getInfo() + '}';
    }
}
