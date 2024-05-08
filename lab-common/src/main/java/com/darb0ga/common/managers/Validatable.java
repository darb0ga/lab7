package com.darb0ga.common.managers;


import com.darb0ga.common.collection.LabWork;

/**
 * Интерфейс для сравнения экземпляров LabWork
 *
 * @author darya
 */
public interface Validatable {

    int compareTo(LabWork o);
}
