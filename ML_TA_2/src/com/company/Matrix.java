package com.company;

public class Matrix {

    public static double[][] deleteString(double [][] startArray, int n) {

        double[][] finalArray = new double[startArray.length - 1][startArray[0].length];

        int index = 0;

        for (int i =0; i< startArray.length; i++) {

            if (i == n) {

                continue;

            }

            System.arraycopy(startArray[i], 0, finalArray[index], 0, startArray[0].length);

            index++;

        }

        return finalArray;

    }

    public static double[][] deleteColumn(double [][] startArray, int n) {

        double[][] finalArray = new double[startArray.length][startArray[0].length - 1];

        int index ;

        for (int i =0; i< startArray.length; i++) {

            index = 0;

            for (int j = 0; j < startArray[0].length; j++) {

                if (j == n) {

                    continue;

                }

                finalArray[i][index++]=startArray[i][j];

            }
        }

        return finalArray;

    }

    public static void swapTwoLines(double [][] arr, int n1, int n2) {

        double[] tempLine = new double[arr[0].length];

        System.arraycopy(arr[n1], 0, tempLine, 0, arr[0].length);

        System.arraycopy(arr[n2], 0, arr[n1], 0, arr[0].length);

        System.arraycopy(tempLine, 0, arr[n2], 0, arr[0].length);

    }

    public static int calculateTheRankOfTheMatrix(double [][] arr) {

       int rank = 0;

        for (double[] doubles : arr) {

            for (int j = 0; j < arr[0].length - 1; j++) {

                if (doubles[j] != 0) {

                    rank++;

                    break;

                }
            }
        }

       return rank;

    }

    public static int calculateTheRankOfTheAugmentedMatrix(double [][] arr) {

        int rank = 0;

        for (double[] doubles : arr) {

            for (int j = 0; j < arr[0].length; j++) {

                if (doubles[j] != 0) {

                    rank++;

                    break;

                }
            }
        }

        return rank;

    }

    public static void reduceTheMatrixToASteppedForm(double [][] arr) {

        int index = 0;

        for (int i = 0; i < arr[0].length - 1; i++) {

            for (int j = index; j < arr.length; j++) {

                if (arr[j][i] != 0) {

                    Matrix.swapTwoLines(arr, index, j);

                    break;

                }
            }

            index++;

        }
    }
}
