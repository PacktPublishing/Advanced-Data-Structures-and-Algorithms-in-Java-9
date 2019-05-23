package Section_4;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortableLinkedList<E> extends LinkedList<E>{
	public void sort(Comparator<E> comparator){
        PriorityQueue<E> priorityQueue = new LinkedHeap<E>(comparator);
        while (first!=null){
            priorityQueue.enqueue(getFirst());
            removeFirst();
        }
        while (priorityQueue.checkMinimum()!=null){
            appendLast(priorityQueue.dequeueMinimum());
        }
    }
	
	public static void main(String[] args) {
        SortableLinkedList<Integer> anotherList = new SortableLinkedList<>();
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
	    anotherList.sort((a,b)->a-b);
        System.out.println(anotherList);
    }
}
