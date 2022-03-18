package queue;

import java.util.Arrays;

//inv: size >= 0 && a[i] != null && a - содержит элементы в порядке их добавления
public class ArrayQueueADT {
    private int n = 16, start = 0, end = 0;
    private Object [] elements = new Object[n];

    private static Object[] toArray(ArrayQueueADT queue, int cap) {
        Object[] result = new Object[cap];
        if (cap == 0) {
            return result;
        }
        if (queue.start >= queue.end) {
            System.arraycopy(queue.elements, queue.start, result, 0, queue.n - queue.start);
            System.arraycopy(queue.elements, 0, result, queue.n - queue.start, queue.end);
        } else {
            System.arraycopy(queue.elements, queue.start, result, 0, queue.end - queue.start);
        }
        return result;
    }

    public static int size(ArrayQueueADT queue) {
        return (queue.end - queue.start + queue.n) % queue.n;
    }
    // post: R = size && size' = size && a[i]' = a[i]
    // заметим, что так как данные не изменяются, инвариант сохраняется

    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size(queue) == 0;
    }
    // post: R = (size == 0) && size' = size && i = 0 .. size - 1 ->  a[i]' = a[i]
    // заметим, что так как данные не изменяются, инвариант сохраняется

    public static void clear(ArrayQueueADT queue) {
        queue.end = queue.start;
    }
    // post: size' = 0

    // pre: x != null
    public static void enqueue(ArrayQueueADT queue, Object x) {
        assert x != null;
        queue.elements[queue.end] = x;
        queue.end = (queue.end + 1) % queue.n;
        if (queue.end == queue.start) {
            queue.elements = toArray(queue,2 * queue.n);
            queue.start = 0;
            queue.end = queue.n;
            queue.n *= 2;
        }
    }
    // post: size' = size + 1 && i = 0 .. size - 1 -> a[i]' = a[i] && a[size]' = x
    // 0 <= 1 <= size + 1 = size' && i = 0..size - 1 -> a[i]' = a[i] != null && a[size]' = x != null, инвариант сохраняется

    // pre: size > 0
    public static Object element(ArrayQueueADT queue) {
        assert queue.size(queue) != 0;
        return queue.elements[queue.start];
    }
    // post: R = a[0] && size' = size && a[i]' = a[i]
    // заметим, что так как данные не изменяются, инвариант сохраняется

    // pre: size > 0
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size(queue) != 0;
        int s = queue.start;
        queue.start = (queue.start + 1) % queue.n;
        return queue.elements[s];
    }
    // post: R = a[0] && size' = size - 1 && i = 0 .. size - 2 -> a'[i] = a[i + 1]
    // size' = size - 1 >= 0 && a'[i] = a[i + 1] != null, инвариант сохраняется

    public static Object[] toArray(ArrayQueueADT queue) {
        return queue.toArray(queue, queue.size(queue));
    }

    public static String toStr(ArrayQueueADT queue) {
        return Arrays.toString(queue.toArray(queue));
    }
    // post: R = строка в виде ‘[’ голова ‘,’ … ‘,’ хвост ‘]‘ && size' = size && a[i]' = a[i]
    // заметим, что так как данные не изменяются, инвариант сохраняется
}
