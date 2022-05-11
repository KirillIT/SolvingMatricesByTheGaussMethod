package com.company;

import java.io.File;

import java.io.FileNotFoundException;

import java.util.Scanner;

public class FromFile {

    public String[][] FromFile(String Path) {

        File file = new File(Path);

        Scanner scanner = null;

        try {

            scanner = new Scanner(file);

        } catch (FileNotFoundException ex) {

            ex.printStackTrace();

        }

        String[] s;

        String line;

        int counter;

        int countCols = 0;

        int counterRows = 0;

        String[][] arrMain = new String[100][100];

        for (int i = 0; ; i++) {

            counterRows++;

            line = scanner.nextLine();

            s = line.split(" ");

            counter = 0;

            for (String number : s) {

                arrMain[i][counter++] = number;

                if (i == 0 || countCols < counter) {

                    countCols++;

                }
            }

            if (!scanner.hasNextLine()) {

                break;

            }
        }

        String[][] trueArrMain = new String[counterRows][countCols];

        for (int i = 0; i < counterRows; i++) {

            for (int j = 0; j < countCols; j++) {

                trueArrMain[i][j] = arrMain[i][j];

            }
        }

        return trueArrMain;

    }
}
