package com.darb0ga.common.managers;

import com.darb0ga.common.collection.Models.LabForReading;
import com.darb0ga.common.exceptions.NoSuchIDException;
import jakarta.xml.bind.*;
import java.io.*;
import java.util.*;


import com.darb0ga.common.collection.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Менеджер коллекции
 * @author darya
 */
@Getter
@Setter
public class CollectionManager {
    protected StandardConsole console;
    protected static File file;
    private static HashSet<LabWork> collection = new HashSet<>();
    public static final Date initializationTime = new Date();

    public static Set<LabWork> getCollection() {
        return collection;
    }

    public static boolean removeById(long id) {
        try{
            return collection.remove(findById(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static LabWork findById(long id) throws NoSuchIDException {
        for (LabWork lab : collection) {
            if (lab.getId() == id) {
                return lab;
            }
        }
        return null;
    }

    public static void updateById(LabWork newLab, int id) throws NoSuchIDException {
        LabWork lab = findById(id);
        lab.setMinimalPoint(newLab.getMinimalPoint());
        lab.setCreationDate(new Date());
        lab.setAuthor(newLab.getAuthor());
        lab.setName(newLab.getName());
        lab.setCoordinates(newLab.getCoordinates());
        lab.setDifficulty(newLab.getDifficulty());

    }

    public static void readCollection(String path) throws IOException{
        file = new File(path);
        StandardConsole console = new StandardConsole(false);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException();
            }
        }
        if (!file.isFile()) {
            throw new IOException();
        }
        if (!file.canRead()) {
            throw new IOException();
        }
        try {
            Scanner fileReader = new Scanner(file);
            StringBuilder s = new StringBuilder();
            while (fileReader.hasNextLine()) {
                s.append(fileReader.nextLine());
            }
            StringReader newFile = new StringReader(s.toString());

            collection.clear();

            JAXBContext jaxbContext = JAXBContext.newInstance(LabForReading.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            LabForReading work = (LabForReading) jaxbUnmarshaller.unmarshal(newFile);
            collection = new HashSet<>(work.getCollectionOfLabs());
        } catch (Exception e) {
            console.printError(e.getMessage());
        }


    }
    public static void saveCollection(){
        StandardConsole console = new StandardConsole(false);
        String file_path = System.getenv("file_path");
        if (file_path == null || file_path.isEmpty())
            console.printError("Путь должен быть в переменных окружения в перменной 'file_path'");
        else console.println("Путь получен успешно");
        try {
            LabForReading labs = new LabForReading();
            labs.setCollectionOfLabs(collection);
            JAXBContext context = JAXBContext.newInstance(LabForReading.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            FileWriter fileWriter = new FileWriter(file);
            marshaller.marshal(labs, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            console.printError("Ошибка ввода вывода");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addElement(LabWork lab) {
        collection.add(lab);
    }


    public static void removeElements(Collection<LabWork> toRemove) {
        collection.removeAll(toRemove);
    }
}
