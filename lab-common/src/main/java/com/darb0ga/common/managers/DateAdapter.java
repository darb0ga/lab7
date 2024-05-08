package com.darb0ga.common.managers;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Класс для работы с типом данных Date
 * @author darya
 */
public class DateAdapter extends XmlAdapter<String, Date>{


    private static final String formatter = "yyyy-MM-dd HH:mm:ss";

    @Override
    public String marshal(Date v) {
        return new SimpleDateFormat(formatter).format(v);
    }


    public Date unmarshal(String v) throws Exception {
        return new SimpleDateFormat(formatter).parse(v);
    }


}
