package com.darb0ga.common.commands;

import com.darb0ga.common.collection.LabWork;
import com.darb0ga.common.collection.Person;
import com.darb0ga.common.exceptions.IllegalParamException;
import com.darb0ga.common.managers.CollectionManager;
import com.darb0ga.common.util.Reply;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Команда 'print_field_ascending_author'. Выводит значения поля author всех элементов в порядке возрастания
 *
 * @author darya
 */
public class PrintFieldAscendingAuthor extends Command {
    public PrintFieldAscendingAuthor() {
        super("print_field_ascending_author", "вывести значения поля author всех элементов в порядке возрастания");
    }

    @Override
    public Reply execute(String args, Scanner scan, boolean isFile) throws IllegalParamException {
        Reply reply = new Reply();
        if (!args.isBlank()) throw new IllegalParamException("*ничего*");
        if (CollectionManager.getCollection().isEmpty()) {
            reply.addResponse("Коллекция пуста.");
        } else {
            ArrayList<Person> people = new ArrayList<>();
            for (LabWork element : CollectionManager.getCollection()) {
                people.add(element.getAuthor());
            }
            Comparator<Person> compareByName = Comparator
                    .comparing(Person::getName);

            ArrayList<Person> sortedPerson = people.stream()
                    .sorted(compareByName)
                    .collect(Collectors.toCollection(ArrayList::new));

            reply.addResponse("Поле Author в порядке убывания:");
            for (Person person : sortedPerson) {
                reply.addResponse(person.toString());
            }
        }
        return reply;
    }
}
