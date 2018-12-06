package com.regex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        if (args.length == 1 && args[0].equals("-test")) {
            new Test().runTest();
            return;
        }

        File outFile = new File("out.txt");
        File inFile;
        FileWriter writer = null;
        Scanner sc;

        if (args.length == 1) {
            inFile = new File(args[0]);
        } else {
            inFile = new File("in.txt");
        }

        try {
            writer = new FileWriter(outFile);
            sc = new Scanner(inFile);
            while (sc.hasNextLine()) {
                try {
                    String line = sc.nextLine();
                    String[] lineArr = line.split(" ");
                    writer.write(lineArr[0] + " ");
                    Regex regex = new Regex(lineArr[0]);
                    for (int i = 1; i< lineArr.length; i++) {
                        writer.write(String.valueOf(regex.doesMatch(lineArr[i])) + " ");
                    }
                } catch (NullPointerException e) {
                    writer.write("ошибка в выражении");
                }
                writer.write("\n");
            }
            writer.close();
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

}
