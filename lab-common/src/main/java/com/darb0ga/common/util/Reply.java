package com.darb0ga.common.util;

import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

@Getter
public final class Reply implements Serializable{
    private Collection<String> response;

    public Reply() {
        this.response = new LinkedHashSet<>();
    }

    public void addResponse(String message) {
        response.add(message);
    }
}
