package queue;

import java.util.Arrays;

//контракты в интерфейсе

public class ArrayQueue extends AbstractQueue {
    private int n = 16, start = 0, end = 0;
    private Object[] elements = new Object[n];

    protected ArrayQueue makeQueue(){
        return new ArrayQueue();
    }

    private Object[] toArray(int cap) {
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

    protected void clearImpl() {
        end = start;
        size = 0;
    }

    protected void enqueueImpl(Object x) {
        elements[end] = x;
        end = (end + 1) % n;
        if (end == start) {
            elements = toArray(2 * n);
            start = 0;
            end = n;
            n *= 2;
        }
    }

    protected Object elementImpl() {
        return elements[start];
    }

    protected Object dequeueImpl() {
        int s = start;
        start = (start + 1) % n;
        return elements[s];
    }

    public Object[] toArray() {
        return toArray(size);
    }

    public String toStr() {
        return Arrays.toString(toArray());
    }
}
