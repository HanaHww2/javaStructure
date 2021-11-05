package me.study.structure;

public class IterableEx{
    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5};
        for (int i = 0, len = arr.length; i < len; i++) {
            System.out.println(arr[i]);
        }
        for (int x : arr) { // Iterable 인터페이스 구현시 가능한 for each문
            System.out.println(x);
        }
    }
}
