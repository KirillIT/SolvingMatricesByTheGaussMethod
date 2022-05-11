package com.company;

import javax.swing.*;

import java.awt.*;

import java.util.Arrays;

import java.util.Objects;

public class FormMain extends JFrame {

    private JButton CalculationButton;

    private JButton ButtonToFile;

    private JButton ButtonFromFile;

    private JPanel formMain;

    private JTable tableMain;

    private JScrollPane ScrollMain;

    private JTextArea TextArea;

    public FormMain() {

        this.setTitle("Решение систем линейных уравнений методом Гаусса");

        this.setContentPane(formMain);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pack();

        ru.vsu.cs.util.JTableUtils.initJTableForArray(tableMain, 40, true, true, true, true);

        ru.vsu.cs.util.JTableUtils.resizeJTable(tableMain, 4, 5);

        CalculationButton.addActionListener(e -> {

            String[][] rawArray;

            double[][] leftSideOfTheArray = new double[tableMain.getRowCount()][tableMain.getColumnCount()/3];

            double[][] rightSideOfTheArray = new double[tableMain.getRowCount()][1];

            try {

                rawArray =  ru.vsu.cs.util.JTableUtils.readStringMatrixFromJTable(tableMain); //переписываем из JTable в строковую матрицу

                int firstTempIndex = 0, secondTempIndex;

                int signIdentifier;

                for (int i = 0; i < tableMain.getRowCount(); i++) { //записываем матрицу (нерасширенную)

                    secondTempIndex = 0;

                    for (int j = 0; j < tableMain.getColumnCount() - 2; j++) {

                        if (j == 0) {

                            assert rawArray != null;

                            leftSideOfTheArray[firstTempIndex][secondTempIndex++] = Integer.parseInt(rawArray[i][j]);

                        }

                        else if (j % 3 != 1 && j % 3 != 2) {

                            if (rawArray[i][j-1].toCharArray()[0]=='-') {

                                signIdentifier = -1;

                            }
                            else {

                                signIdentifier = 1;

                            }

                            leftSideOfTheArray[firstTempIndex][secondTempIndex++] = Integer.parseInt(rawArray[i][j])* signIdentifier;

                        }
                    }

                    firstTempIndex++;

                }

                for (int i = 0; i < tableMain.getRowCount(); i++) { //записываем правую часть (то, что после '=')

                    assert rawArray != null;

                    rightSideOfTheArray[i][0] = Integer.parseInt(rawArray[i][tableMain.getColumnCount() - 1]);

                }

                double[][] extendedMatrix = new double[tableMain.getRowCount()][tableMain.getColumnCount()/3+1];

                String[] resultsForVariablesWithACoefficientOfZeroInAllRows = new String[tableMain.getColumnCount()/3];

                for (int i = 0; i < tableMain.getRowCount(); i++) { //тут записываем дополненную матрицу

                    for (int j = 0; j < tableMain.getColumnCount()/3+1; j++) {

                        if (j == tableMain.getColumnCount()/3) {

                            extendedMatrix[i][j] = rightSideOfTheArray[i][0];

                        }

                        else {

                            extendedMatrix[i][j] = leftSideOfTheArray[i][j];

                        }
                    }
                }

                for (int i = 0; i  < extendedMatrix.length; i++) { //удаляем нулевые строки

                    for (int j = 0; j < extendedMatrix[0].length; j++) {

                        if (extendedMatrix[i][j] != 0) {

                            break;

                        }

                        if (j == extendedMatrix[0].length - 1) {

                            extendedMatrix = Matrix.deleteString(extendedMatrix, i);

                            i--;

                        }
                    }
                }

                for (int i = 0; i  < extendedMatrix[0].length - 1; i++ ) { //для нулевых столбоцов делаем пометку, что x с индексом (номер столбца + 1) - любое число

                    for (int j = 0; j < extendedMatrix.length; j++) {

                        if (extendedMatrix[j][i] != 0) {

                            break;

                        }

                        if (j == extendedMatrix.length - 1) {

                            resultsForVariablesWithACoefficientOfZeroInAllRows[i] = "any number";

                            break;

                        }
                    }
                }

                for (int i = 0; i  < extendedMatrix[0].length - 1; i++) { //удаляем нулевые столбцы

                    for (int j = 0; j < extendedMatrix.length; j++) {

                        if (extendedMatrix[j][i] != 0) {

                            break;

                        }

                        if (j == extendedMatrix.length - 1) {

                            extendedMatrix = Matrix.deleteColumn(extendedMatrix, i);

                            i--;

                            break;

                        }
                    }
                }

                boolean isZero;

                boolean tempBoolean;

                int lastLineNumber = extendedMatrix.length - 1;

                for (int i = 0; i  < extendedMatrix.length; i++) { //приводим матрицу к ступенчатому виду (до преобразований, чтобы смогли корректно считать)

                    isZero = true;

                    tempBoolean = false;

                    for (int j = 0; j < extendedMatrix[0].length -1; j++){

                        if (extendedMatrix[i][j] != 0) {

                            isZero = false;

                            break;

                        }
                    }

                    if (isZero && i < lastLineNumber) {

                        while (true) {

                            for (int j = 0; j < extendedMatrix[0].length - 1; j++) {

                                if (extendedMatrix[lastLineNumber][j] != 0) {

                                    tempBoolean = true;

                                    break;

                                }
                            }

                            if (tempBoolean) {

                                break;

                            }

                            lastLineNumber--;

                        }

                        if (i < lastLineNumber) { //меняем строки местами

                            Matrix.swapTwoLines(extendedMatrix, i, lastLineNumber);

                        }
                    }
                }

                if (Matrix.calculateTheRankOfTheMatrix(extendedMatrix) == Matrix.calculateTheRankOfTheAugmentedMatrix(extendedMatrix)) { //если ранг расширенной матрицы равен рангу нерасширенной

                    double coefficient;

                    int initialIndexI = 0;

                    int initialIndexJ= 0;

                    int howManyLinesBelow;

                    int min = extendedMatrix.length;

                    if (min > extendedMatrix[0].length) {

                        min = extendedMatrix[0].length;

                    }

                    for (int i = 1 ; i  < min; i++) { //основной цикл алгебраических преобразований

                        Matrix.reduceTheMatrixToASteppedForm(extendedMatrix); //каждый шаг приводим матрицу к ступенчатому виду

                        if (Matrix.calculateTheRankOfTheMatrix(extendedMatrix) != Matrix.calculateTheRankOfTheAugmentedMatrix(extendedMatrix)) { //если во время вычислений сразу можно определить, что получилась нулевая строка до знака '=', то решений нет

                            TextArea.setText("Нет решений");

                            break;

                        }

                        howManyLinesBelow = 1;

                        for (int j = i ; j  < extendedMatrix.length; j++) {

                            if (extendedMatrix[initialIndexI][initialIndexJ] == 0) {

                                continue;

                            }

                            if (howManyLinesBelow + initialIndexI== extendedMatrix.length) {

                                break;

                            }

                            coefficient = extendedMatrix[initialIndexI + howManyLinesBelow][initialIndexJ] / extendedMatrix[initialIndexI][initialIndexJ];

                            for (int k = initialIndexJ; k < extendedMatrix[0].length; k++) {

                                extendedMatrix[j][k] -= extendedMatrix[initialIndexI][k]*coefficient;

                                if (Math.abs(extendedMatrix[j][k]) < 0.000000000001) { //неточности вычислений убираем (это неточности не алгоритма, а именно Java)

                                    extendedMatrix[j][k] = 0;

                                }
                            }

                            howManyLinesBelow++;

                            if (howManyLinesBelow + initialIndexI== extendedMatrix.length) {

                                break;

                            }
                        }

                        initialIndexI++;

                        initialIndexJ++;

                    }

                    Matrix.reduceTheMatrixToASteppedForm(extendedMatrix);

                    for (int i = 0; i  < extendedMatrix.length; i++ ) { //удаляем нулевые строки после алгебраических преобразований

                        for (int j = 0; j < extendedMatrix[0].length; j++) {

                            if (extendedMatrix[i][j] != 0) {

                                break;

                            }

                            if (j == extendedMatrix[0].length - 1) {

                                extendedMatrix = Matrix.deleteString(extendedMatrix, i);

                                i--;

                            }
                        }
                    }

                    if (Matrix.calculateTheRankOfTheMatrix(extendedMatrix) == Matrix.calculateTheRankOfTheAugmentedMatrix(extendedMatrix)) { //опять сравниваем ранги расширенной и нерасширенной матриц

                        if (extendedMatrix.length == extendedMatrix[0].length - 1) { //если матрица квадратная, то просто вычисляем иксы

                            double[] results = new double[extendedMatrix[0].length - 1];

                            int index = results.length - 1;

                            for (int i = extendedMatrix.length - 1; i >=0; i--) {

                                results[index] = extendedMatrix[i][extendedMatrix[0].length - 1];

                                for (int j = index + 1; j < results.length; j++) {

                                    results[index] -= results[j] * extendedMatrix[i][j];

                                }

                                results[index] /= extendedMatrix[i][index];

                                if (results[index] == -0) {

                                    results[index] = 0;

                                }

                                index--;

                            }

                            int personalIndex = 0;

                            StringBuilder result = new StringBuilder();

                            for (int i = 0; i < resultsForVariablesWithACoefficientOfZeroInAllRows.length; i++) { //выводим всё в JTable

                                if (Objects.equals(resultsForVariablesWithACoefficientOfZeroInAllRows[i], "any number")) {

                                    result.append(i + 1).append("x - любое число\n");

                                }

                                else {

                                    result.append(i + 1).append("x = ").append(results[personalIndex++]).append("\n");

                                }
                            }

                            TextArea.setText(result.toString());

                        }

                        else { //если переменных больше, чем уравнений

                            StringBuilder result;

                            String[] results = new String[extendedMatrix[0].length - 1];

                            Arrays.fill(results, "");

                            int startIndex = Matrix.calculateTheRankOfTheMatrix(extendedMatrix) - 1;

                            for (int i = extendedMatrix.length - 1; i >=0; i--) {

                                result = new StringBuilder();

                                result.append("(").append(extendedMatrix[i][extendedMatrix[0].length - 1]);

                                for (int j = startIndex + 1; j < extendedMatrix[0].length - 1; j++) {

                                    char sign = '-';

                                    double shamResult = extendedMatrix[i][j];

                                    if (extendedMatrix[i][j] <= 0) {

                                        sign = '+';

                                        shamResult = Math.abs(shamResult);

                                    }

                                    if (Objects.equals(results[j], "")) {

                                        result.append(" ").append(sign).append(" ").append(shamResult).append("x").append(j + 1);

                                    }

                                    else {

                                        result.append(" ").append(sign).append(" ").append(shamResult).append("(").append(results[j]).append(")");

                                    }
                                }

                                result.append(") / ").append(extendedMatrix[i][startIndex]);

                                results[i] = result.toString();

                                startIndex--;

                            }

                            StringBuilder resultInOneLine = new StringBuilder();

                            int index = 0;

                            int firstCounter = 1;

                            int secondCounter = 1;

                            for (String arrAnswer : resultsForVariablesWithACoefficientOfZeroInAllRows) { //выводим всё в JTable

                                if (Objects.equals(arrAnswer, "any number")) {

                                    resultInOneLine.append("X").append(firstCounter++);

                                    resultInOneLine.append(" - любое число\n");

                                }

                                else {

                                    resultInOneLine.append("x").append(secondCounter++);

                                    if (Objects.equals(results[index], "")) {

                                        resultInOneLine.append(" - любое число\n");

                                    }

                                    else {

                                        resultInOneLine.append(" = ").append(results[index]).append("\n");

                                    }

                                    index++;

                                }
                            }

                            TextArea.setText(resultInOneLine.toString());

                        }
                    }

                    else {

                        TextArea.setText("Нет решений");

                    }
                }

                else {

                    TextArea.setText("Нет решений");

                }

            }

            catch (Exception a) {

                a.getMessage();

            }
        });

        ButtonToFile.addActionListener(e -> {

            String[][] rawArray;

            String[][] leftSideOfTheArray = new String[tableMain.getRowCount()][tableMain.getColumnCount() - tableMain.getColumnCount()/3];

            try {

                rawArray =  ru.vsu.cs.util.JTableUtils.readStringMatrixFromJTable(tableMain);

                int firstTempIndex = 0, secondTempIndex;

                for (int i = 0; i < tableMain.getRowCount(); i++) {

                    secondTempIndex = 0;

                    for (int j = 0; j < tableMain.getColumnCount(); j++){

                        if (j % 3 != 1) {

                            assert rawArray != null;

                            leftSideOfTheArray[firstTempIndex][secondTempIndex++] = rawArray[i][j];

                        }
                    }

                    firstTempIndex++;

                }

            }

            catch (Exception a) {

                a.getMessage();

            }

            ToFile toFile = new ToFile();

            toFile.ToFile(leftSideOfTheArray, tableMain.getRowCount(), tableMain.getColumnCount() - tableMain.getColumnCount()/3, "C:\\Users\\newli\\IdeaProjects\\ML_TA_2\\src\\com\\company\\test.txt"); //а теперь записываем из матрицы в файл

        });

        ButtonFromFile.addActionListener(e -> {

            FromFile fromFile = new FromFile();

            String[][] firstString = fromFile.FromFile("C:\\Users\\newli\\IdeaProjects\\ML_TA_2\\src\\com\\company\\test.txt");

            String[][] secondString = new String[firstString.length][firstString[0].length / 2 + firstString[0].length];

            int firstTempIndex = 0, secondTempIndex;

            for (int i = 0; i  <firstString.length; i++) {

                secondTempIndex = 0;

                for (int j = 0; j < firstString[0].length / 2 + firstString[0].length; j++) {

                    if (j % 3 == 1) {

                        secondString[i][j] = "x" + (j/3 + 1);

                    }

                    else {

                        secondString[i][j] = firstString[firstTempIndex][secondTempIndex++];

                    }
                }

                firstTempIndex++;

            }

            ru.vsu.cs.util.JTableUtils.writeArrayToJTable(tableMain, secondString);

        });
    }

    {

        $$$setupUI$$$();

    }

    private void $$$setupUI$$$() {

        formMain = new JPanel();

        formMain.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(10, 10, 10, 10), 10, 10));

        ButtonToFile = new JButton();

        ButtonToFile.setText("Загрузить в файл");

        formMain.add(ButtonToFile, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        ButtonFromFile = new JButton();

        ButtonFromFile.setText("Загрузить из файла");

        formMain.add(ButtonFromFile, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        CalculationButton = new JButton();

        CalculationButton.setHideActionText(false);

        CalculationButton.setText("Сортировать");

        formMain.add(CalculationButton, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        ScrollMain = new JScrollPane();

        formMain.add(ScrollMain, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));

        tableMain = new JTable();

        ScrollMain.setViewportView(tableMain);

    }

    public JComponent $$$getRootComponent$$$() {

        return formMain;

    }
}
