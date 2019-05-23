package Section_2;

import java.util.Iterator;

/**
 * Created by debasishc on 12/8/16.
 */
public class QueueImplLinkedList<E> implements Queue<E>{
    protected LinkedList<E> list = getNewLinkedList();

    protected LinkedList<E> getNewLinkedList(){
        return new LinkedList<>();
    }

    @Override
    public void enqueue(E value) {
        list.appendLast(value);
    }

    @Override
    public E dequeue() {
        if(list.getLength()==0){
            return null;
        }
        E value = list.getFirst();
        list.removeFirst();
        return value;
    }

    @Override
    public E peek() {
        if(list.getLength()==0){
            return null;
        }
        return list.getFirst();
    }

    public static void main(String [] args){
        Queue<Integer> store = new QueueImplLinkedList<>();
        store.enqueue(4);
        store.enqueue(3);
        store.enqueue(2);
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        store.enqueue(4);
        store.enqueue(3);
        store.enqueue(2);
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        store.enqueue(4);
        store.enqueue(3);
        store.enqueue(2);
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());

    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }
}
