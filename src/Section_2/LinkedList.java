package Section_2;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by debasishc on 21/7/16.
 */
public class LinkedList<E> implements Iterable<E> {
    protected int length = 0;
    protected Node<E>[] lastModifiedNode;
    protected Node<E> first;
    protected Node<E> last;

    public int getLength(){
        return length;
    }

    public void appendAllLast(LinkedList<E> anotherList){
        if(first == null){
            first = anotherList.first;
            last = anotherList.last;
        }else{
            last.next = anotherList.first;
            last = anotherList.last;
        }
        length += anotherList.length;

    }

    public static void main(String[] args) {
//        LinkedList<Integer> linkedList = new LinkedList<>();
//        linkedList.appendFirst(4);
//        linkedList.appendFirst(1);
//        linkedList.appendFirst(2);
//        linkedList.appendFirst(3);
//        linkedList.appendLast(8);
//        linkedList.appendLast(7);
//        linkedList.appendLast(18);
//        linkedList.appendFirst(50);
//        linkedList.insert(5, 500);
//        linkedList.insert(9, 501);
//        linkedList.removeAtIndex(8);
//        linkedList.insert(8, 502);
//        linkedList.removeAtIndex(1);

        LinkedList<Integer> anotherList = new LinkedList<>();
        anotherList.appendFirst(4);
        anotherList.appendFirst(1);
        anotherList.appendFirst(2);
        anotherList.appendFirst(3);
        anotherList.appendLast(8);
        anotherList.appendLast(7);
        anotherList.appendLast(1);
        anotherList.appendLast(2);
        anotherList.appendLast(3);
        anotherList.appendLast(18);
//        linkedList.appendAllLast(anotherList);
//
//     //   linkedList.visualize(visualizer);
//
//
//        while(anotherList.getLength()>0){
//            anotherList.removeLast();
//        }

        //linkedList.<Integer>zip(anotherList).visualize(visualizer);
//        Iterator<Integer> iter = anotherList.iterator();
//        while (iter.hasNext()){
//            int val = iter.next();
//            System.out.println();
//            if(val<=4)
//                iter.remove();
//            anotherList.visualize(visualizer);
//        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ListIterator();
    }

    protected Node<E> getNewNode() {
        Node<E> node = new Node<>();
        lastModifiedNode = new Node[]{node};
        return node;
    }

    public Node<E> appendFirst(E value) {
        Node node = getNewNode();
        node.value = value;
        node.next = first;
        first = node;
        if (length == 0)
            last = node;
        length++;
        return node;
    }

    public Node<E> appendLast(E value) {
        Node node = getNewNode();
        node.value = value;
        if (last != null)
            last.next = node;
        last = node;
        if (first == null) {
            first = node;
        }

        length++;
        return node;
    }

    public Node<E> insert(int index, E value) {
        Node<E> node = getNewNode();
        if (index < 0 || index > length) {
            throw new IllegalArgumentException("Invalid index for insertion");
        } else if (index == length) {
            return appendLast(value);
        } else if (index == 0) {
            return appendFirst(value);
        } else {
            Node<E> prev = first;
            while (index > 1) {
                index--;
                prev = prev.next;
            }
            node.value = value;
            node.next = prev.next;
            prev.next = node;
            length++;
            return node;
        }
    }

    public E getFirst() {
        if (length == 0) {
            throw new NoSuchElementException();
        }
        return first.value;
    }

    public E getLast() {
        if (length == 0) {
            throw new NoSuchElementException();
        }
        return last.value;
    }

    public Node<E> removeFirst() {
        if (length == 0) {
            throw new NoSuchElementException();
        }
        Node<E> origFirst = first;
        first = first.next;
        length--;
        if (length == 0) {
            last = null;
        }
        return origFirst;
    }


    public E findAtIndex(int index) {
        Node<E> result = first;
        while (index >= 0) {
            if (result == null) {
                throw new NoSuchElementException();
            } else if (index == 0) {
                return result.value;
            } else {
                index--;
                result = result.next;
            }
        }
        return null;
    }

    protected Node<E> removeAtIndex(int index) {
        if (index >= length || index < 0) {
            throw new NoSuchElementException();
        }

        if (index == 0) {
            Node<E> nodeRemoved = first;
            removeFirst();
            return nodeRemoved;
        }

        Node justBeforeIt = first;
        while (--index > 0) {
            justBeforeIt = justBeforeIt.next;
        }

        Node<E> nodeRemoved = justBeforeIt.next;
        if (justBeforeIt.next == last) {
            last = justBeforeIt.next.next;
        }
        justBeforeIt.next = justBeforeIt.next.next;

        length--;

        return nodeRemoved;

    }

    public void setValueAtIndex(int index, E value){
        Node<E> result = first;
        while (index >= 0) {
            if (result == null) {
                throw new NoSuchElementException();
            } else if (index == 0) {
                result.value = value;
                return;
            } else {
                index--;
                result = result.next;
            }
        }
    }


    public Node<E> removeLast() {
        return removeAtIndex(length - 1);
    }

    public static class Node<E> {
        protected E value;
        protected Node next;

        public String toString() {
            return value.toString();
        }

        public Node getNext() {
            return next;
        }

        public E getValue() {
            return value;
        }
    }

    public class ListIterator implements Iterator<E> {
        protected Node<E> nextNode = first;
        protected Node<E> currentNode = null;
        protected Node<E> prevNode = null;

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new IllegalStateException();
            }
            prevNode = currentNode;
            currentNode = nextNode;
            nextNode = nextNode.next;
            return currentNode.value;
        }

        @Override
        public void remove() {
            if(currentNode==null || currentNode == prevNode){
                throw new IllegalStateException();
            }
            if(currentNode==first){
                first = nextNode;
            }else{
                prevNode.next = nextNode;
            }
            currentNode=prevNode;

        }

        public void setValue(E value){
            currentNode.value = value;
        }

    }

    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        Node<E> node = first;
        while(node!=null){
            if(node.value==null){
                sb.append("null");
            }else{
                sb.append(node.value.toString()+", ");
            }
            node=node.next;
        }
        sb.append("]");
        return sb.toString();
    }
}

