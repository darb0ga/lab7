package com.darb0ga.common.collection.Models;


import com.darb0ga.common.collection.LabWork;
import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.util.HashSet;

/**
 * Класс для парсинга объекта
 * @author darya
 */
@Getter
@Setter
@XmlRootElement(name = "labworks")
@XmlAccessorType(XmlAccessType.FIELD)
public class LabForReading{
    @XmlElement(name="labwork", type = LabWork.class)
    private HashSet<LabWork> collectionOfLabs;

}
