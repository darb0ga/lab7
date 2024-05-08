package com.darb0ga.common.managers;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
/**
 * Класс хранящий коллекцию сканеров и коллекцию исполняемых файлов
 *
 * @author darya
 */
@Getter
@Setter
public class ScannerManager {

    @Getter
    private static final Deque<String> pathFiles = new ArrayDeque<>();

    @Getter
    private static final Deque<Scanner> scanners = new ArrayDeque<>();

    public static boolean recurse(String path) {
        return pathFiles.contains(new File(path).getAbsolutePath());
    }

    public static void setFileScanner(String filename) throws FileNotFoundException {
        pathFiles.add(new File(filename).getAbsolutePath());
        scanners.add(new Scanner(new FileReader(filename)));
    }


}
