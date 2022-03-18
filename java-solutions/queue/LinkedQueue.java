package queue;

//контракты в интерфейсе

public class LinkedQueue extends AbstractQueue{
    private Node end = new Node();
    private Node start = end;

    protected LinkedQueue makeQueue() {
        return new LinkedQueue();
    }

    protected void enqueueImpl(Object x) {
        end.value = x;
        end.next = new Node();
        end = end.next;
    }

    protected Object elementImpl() {
        return start.value;
    }

    protected Object dequeueImpl() {
        Object result = start.value;
        start = start.next;
        return result;
    }

    protected void clearImpl() {
        start = end;
    }

    public Object[] toArray() {
        Object[] result = new Object[size];
        Node ind = start;
        for (int i = 0; i < size; i++) {
            result[i] = ind.value;
            ind = ind.next;
        }
        return result;
    }

    private static class Node {
        private Object value;
        private Node next;

        public Node() {
            this.value = null;
            this.next = null;
        }
    }
}
