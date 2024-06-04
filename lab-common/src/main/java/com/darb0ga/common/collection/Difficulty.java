package com.darb0ga.common.collection;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * Класс энума сложности
 * @author darya
 */
public enum Difficulty implements Serializable {
    VERY_EASY,
    EASY,
    HARD,
    HOPELESS,
    TERRIBLE;
}
