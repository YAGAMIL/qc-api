package com.quantumtime.qc;

import java.util.stream.IntStream;

/**
 * .Description:鸡兔同笼 Program:qc-api.Created on 2019-11-20 18:19
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public class Demo2 {


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Demo2 m = new Demo2();
        int[][] test = {
            {0, 3, 9},
            {2, 8, 5},
            {5, 7, 0}
        };

        int result = m.maxHeart(test);
        System.out.print(result);
    }

    private int maxHeart(int[][] arr1) {
        if (arr1 == null || arr1.length == 0 || arr1[0].length == 0) return 0;
        int x = arr1.length;
        int y = arr1[0].length;
        int[][] ints = getArr(arr1, true);
        {
            int i = 1;
            while (i < x - 1) {
                int j = 2;
                while (j < y) {
                    ints[i][j] = Math.max(ints[i - 1][j], ints[i][j - 1]) + ints[i][j];
                    j++;
                }
                i++;
            }
        }
        int i1 = ints[x - 2][y - 1];
        int[][] ints1 = getArr(arr1, false);
        int i = 2;
        while (i < x) {
            int j = 1;
            while (j < y - 1) {
                ints1[i][j] = Math.max(ints1[i - 1][j], ints1[i][j - 1]) + ints1[i][j];
                j++;
            }
            i++;
        }
        return i1 + ints1[x - 1][y - 2];
    }

    private void setMax(int[][] ints, int i, int j, int x, int y) {
        for (; i < x; i++) {
            while (j < y - 1) {
                ints[i][j] = Math.max(ints[i - 1][j], ints[i][j - 1]) + ints[i][j];
                j++;
            }
        }
    }


    private int[][] getArr(int[][] dbArr, boolean isStep) {
        int n = dbArr[0].length;
        int m = dbArr.length;
        int[][] arr = new int[m][n];
        int x = isStep ? 0 : 1;
        int y = isStep ? 1 : 0;
        IntStream.range(0, m).forEach(i -> System.arraycopy(dbArr[i], 0, arr[i], 0, n));
        IntStream.range(1, m).forEach(i -> arr[x][i] += arr[x][i - 1]);
        IntStream.range(1, n).forEach(i -> arr[i][y] += arr[i - 1][y]);
        return arr;
    }
}
