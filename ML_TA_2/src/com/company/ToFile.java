package com.company;

import java.io.File;

import java.io.FileNotFoundException;

import java.io.PrintWriter;

public class ToFile {

    public void ToFile(String[][] arrMain, int Row, int Col, String path) {

        File file = new File(path);

        PrintWriter pw = null;

        try {

            pw = new PrintWriter(file);

        } catch (FileNotFoundException ex) {

            ex.printStackTrace();

        }

        for (int i = 0; i < Row; i++) {

            for (int j = 0; j < Col; j++) {

                pw.print(arrMain[i][j] + " ");

            }

            pw.println();

        }

        pw.close();

    }
}
