package com.darb0ga.common.collection;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * Класс энума сложности
 * @author darya
 */
@Getter
@AllArgsConstructor
public enum Difficulty implements Serializable {
    VERY_EASY,
    EASY,
    HARD,
    HOPELESS,
    TERRIBLE;
}
