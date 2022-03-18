package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Queue {
    //pre: x != null
    void enqueue(Object x);
    //post: i = 1..n: a[i]' = a[i]; a[n] = x; size' = size + 1

    //pre: size > 0
    Object element();
    //post: R = a[1], i = 1..n: a[i]' = a[i];  size' = size;

    //pre: size > 0
    Object dequeue();
    //post: R = a[1], i = 1..n - 1: a[i]' = a[i + 1];  size' = size - 1;

    int size();
    //post: R = size; i = 1..n: a[i]' = a[i]; size' = size;

    boolean isEmpty();
    //post: R = size == 0; i = 1..n: a[i]' = a[i];  size' = size;

    void clear();
    //post: size' = 0;

    Object[] toArray();
    //post: R = a[i], i = 1..n: a[i]' = a[i];  size' = size;

    Queue filter(Predicate<Object> p);
    Queue map(Function<Object, Object> f);
}
