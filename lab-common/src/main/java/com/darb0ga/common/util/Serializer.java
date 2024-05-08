package com.darb0ga.common.util;

import lombok.Getter;

import java.io.*;
import java.util.UUID;

@Getter
public class Serializer {
    private Object object;
    private UUID clientId;

    public Serializer() {

    }

    public Serializer(Object object, UUID client) {
        this.object = object;
        this.clientId = client;
    }

    public <T> T deserialize(byte[] buffer) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(buffer))) {
            object = (T) inputStream.readObject();
            return (T) object;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
        objectOut.writeObject(object);
        return byteOut.toByteArray();
    }

}
