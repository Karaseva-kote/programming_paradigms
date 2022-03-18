package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    public Queue filter(Predicate<Object> p) {
        return function(x -> p.test(x) ? x : null);
    }

    public Queue map(Function<Object, Object> f) {
        return function(f);
    }

    private Queue function(Function<Object, Object> f) {
        Queue q = makeQueue();
        Object[] arr = toArray();
        for (Object o : arr) {
            if (f.apply(o) != null) {
                q.enqueue(f.apply(o));
            }
        }
        return q;
    }

    protected abstract AbstractQueue makeQueue();

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(Object x) {
        assert x != null;
        size++;
        enqueueImpl(x);
    }

    protected abstract void enqueueImpl(Object x);

    public Object element() {
        assert size() != 0;
        return elementImpl();
    }

    protected abstract Object elementImpl();

    public Object dequeue() {
        assert size() != 0;
        size--;
        return dequeueImpl();
    }

    protected abstract Object dequeueImpl();

    public void clear() {
        size = 0;
        clearImpl();
    }

    protected abstract void clearImpl();
}
