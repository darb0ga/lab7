package com.darb0ga.common.managers;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class AuthorsRight implements Serializable {
    private String name;
    @Getter
    private String password;
    private boolean isGotIn;
    @Getter
    @Setter
    private int ID;

    public AuthorsRight(String name, String psswd) {
        this.name = name;
        this.password = psswd;
        this.isGotIn = false;
    }

}

