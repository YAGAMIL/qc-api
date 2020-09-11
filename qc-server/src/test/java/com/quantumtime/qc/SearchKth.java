package com.quantumtime.qc;
import	java.util.ArrayList;

import java.util.List;
import java.util.stream.IntStream;

/**
 * .Description:查询第k大的奇数 Program:qc-api.Created on 2019-11-18 10:49
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 */
public class SearchKth {

    private static int findKth(int[] unsortedArr, int low, int high, int k) {
        if ((low == high) && unsortedArr[low] % 2 != 0) return unsortedArr[low];
        int partition = quickSortStep(unsortedArr, low, high);
        // 找到的是第几个大值
        int index = high - partition + 1;
        // 此时向左查找, 查找的是相对位置的值，k在左段中的下标为k-index
        return (index == k)
                ? isOdd(unsortedArr[partition])
                : (index < k)
                        ? findKth(unsortedArr, low, partition - 1, k - index)
                        : findKth(unsortedArr, partition + 1, high, k);
    }

    // 快排
    private static int quickSortStep(int[] arr, int begin, int end) {
        int key = arr[begin];
        while (begin < end) {
            while (key < arr[end] && begin < end) end--;
            arr[begin] = arr[end];
            while (key > arr[begin] && begin < end) begin++;
            arr[end] = arr[begin];
        }
        arr[end] = key;
        return end;
    }

    private static int isOdd(int num) {
        return (num % 2 == 0) ? 0 : num;
    }

    private static int findKth(int[] arr, int k) {
        return findKth(arr, 0, arr.length - 1, k);
    }

    public static void main(String[] args) {
        //        IntStream.range(0, 1668).mapToObj(i -> UUIDUtils.getUUID32()).forEach(System.out::println);
//        int[] arr = new int[] {1, 5, 8, 2, 3, 9, 4};
//
//        System.out.println(Arrays.toString(bubbleSort(arr)));

        List <String> list = new ArrayList<> (1609);
        for (int i = 0; i <173; i++) {
            list.add("666");
        }
        expandList(list, 1609);
        list.forEach(System.err::println);
//        int lastSum = 1609;
//        int currentSize = 173;
//        int initSize = 173;
//        int i = lastSum / initSize;
//        int num = lastSum % initSize;
//        System.out.println("倍数"+ i +"余数" + num );
    }

    public static int[] bubbleSort(int[] arr) {
        int n = arr.length;

        IntStream.range(0, n)
                .map(i -> n - i)
                .flatMap(bound -> IntStream.range(0, bound - 1))
                .filter(j -> arr[j] > arr[j + 1])
                .forEach(j -> {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                });
        return arr;
    }

    public static void expandList(List<String> list, int lastSum) {

        int initSize = list.size();
        int i = lastSum / initSize;
        int num = lastSum % initSize;
        List<String> medium = new ArrayList<> ();
        IntStream.range(0, i).mapToObj(j -> list).forEach(medium::addAll);
        medium.addAll(list.subList(0,num));
    }
}
