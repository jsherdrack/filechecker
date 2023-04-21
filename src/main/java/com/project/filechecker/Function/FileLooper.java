package com.project.filechecker.Function;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 *This class is responsible for looping through all the files in a given file path and extracting all files with the
 * requested extensions.
 */
public class FileLooper {

    static List<File> fileNames = new ArrayList<>();
    public static List<File> fileLooper(String sDir){
        File[] allFiles = new File(sDir).listFiles();

        for (File file : allFiles) {
            if (file.getName().toLowerCase().endsWith(".arf")){
                fileNames.add(file);
                System.out.println(file.getAbsolutePath());
            }
            if (file.getName().toLowerCase().endsWith(".adicht")){
                fileNames.add(file);
                System.out.println(file.getAbsolutePath());
            }
            if (file.getName().toLowerCase().endsWith(".bmp")){
                fileNames.add(file);
                System.out.println(file.getAbsolutePath());
            }
            if (file.getName().toLowerCase().endsWith(".dcm")){
                fileNames.add(file);
                System.out.println(file.getAbsolutePath());
            }
            if (file.isDirectory()) {
                System.out.println("Checking: " +file.getAbsolutePath());
                fileLooper(file.getAbsolutePath());
            }
        }
        return fileNames;
    }
}
