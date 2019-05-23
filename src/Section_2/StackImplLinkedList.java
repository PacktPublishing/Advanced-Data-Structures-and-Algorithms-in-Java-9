package Section_2;



import java.util.Iterator;

/**
 * Created by debasishc on 12/8/16.
 */
public class StackImplLinkedList<E> implements Stack<E> {
    protected LinkedList<E> list = getNewLinkedList();

    protected LinkedList<E> getNewLinkedList(){
        return new LinkedList<>();
    }

    @Override
    public void push(E value) {
        list.appendFirst(value);
    }

    @Override
    public E pop() {
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
        Stack<Integer> store = new StackImplLinkedList<>();
        store.push(4);
        store.push(3);
        store.push(2);
        System.out.println(store.pop());
        System.out.println(store.pop());
        System.out.println(store.pop());
        System.out.println(store.pop());

    }
    
    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }
}
