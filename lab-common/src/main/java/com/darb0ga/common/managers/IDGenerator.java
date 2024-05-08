package com.darb0ga.common.managers;



import com.darb0ga.common.collection.LabWork;

import java.util.HashSet;
/**
 * Класс генерирующий id
 * @author darya
 */
public class IDGenerator {
    public static int generate() {
        HashSet<LabWork> lab = (HashSet<LabWork>) CollectionManager.getCollection();
        boolean flag = true;

        while (true) {
            int res = (int) (Math.random() * 899999 + 100000);
            for (LabWork element : lab) {
                if (element.getId() == res) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return res;
            }
        }
    }
}
