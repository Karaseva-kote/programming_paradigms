package queue;

import java.util.Arrays;

//inv: size >= 0 && a[i] != null && a - содержит элементы в порядке их добавления
public class ArrayQueueModule {
    private static int n = 16, start = 0, end = 0;
    private static Object [] elements = new Object[n];

    private static Object[] toArray(int cap) {
        Object[] result = new Object[cap];
        if (cap == 0) {
            return result;
        }
        if (start >= end) {
            System.arraycopy(elements, start, result, 0, n - start);
            System.arraycopy(elements, 0, result, n - start, end);
        } else {
            System.arraycopy(elements, start, result, 0, end - start);
        }
        return result;
    }

    public static int size() {
        return (end - start + n) % n;
    }
    // post: R = size && size' = size && a[i]' = a[i]
    // заметим, что так как данные не изменяются, инвариант сохраняется

    public static boolean isEmpty() {
        return size() == 0;
    }
    // post: R = (size == 0) && size' = size && i = 0 .. size - 1 ->  a[i]' = a[i]
    // заметим, что так как данные не изменяются, инвариант сохраняется

    public static void clear() {
        end = start;
    }
    // post: size' = 0

    // pre: x != null
    public static void enqueue(Object x) {
        assert x != null;
        elements[end] = x;
        end = (end + 1) % n;
        if (end == start) {
            elements = toArray(2 * n);
            start = 0;
            end = n;
            n *= 2;
        }
    }
    // post: size' = size + 1 && i = 0 .. size - 1 -> a[i]' = a[i] && a[size]' = x
    // 0 <= 1 <= size + 1 = size' && i = 0..size - 1 -> a[i]' = a[i] != null && a[size]' = x != null, инвариант сохраняется

    // pre: size > 0
    public static Object element() {
        assert size() != 0;
        return elements[start];
    }
    // post: R = a[0] && size' = size && a[i]' = a[i]
    // заметим, что так как данные не изменяются, инвариант сохраняется

    // pre: size > 0
    public static Object dequeue() {
        assert size() != 0;
        int s = start;
        start = (start + 1) % n;
        return elements[s];
    }
    // post: R = a[0] && size' = size - 1 && i = 0 .. size - 2 -> a'[i] = a[i + 1]
    // size' = size - 1 >= 0 && a'[i] = a[i + 1] != null, инвариант сохраняется

    public static Object[] toArray() {
        return toArray(size());
    }

    public static String toStr() {
        return Arrays.toString(toArray());
    }
    // post: R = строка в виде ‘[’ голова ‘,’ … ‘,’ хвост ‘]‘ && size' = size && a[i]' = a[i]
    // заметим, что так как данные не изменяются, инвариант сохраняется
}
