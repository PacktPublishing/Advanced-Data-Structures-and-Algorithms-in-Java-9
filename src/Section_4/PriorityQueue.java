package Section_4;

import java.util.Iterator;

public interface PriorityQueue<E>{
    E checkMinimum();
    E dequeueMinimum();
    void enqueue(E value);
}
