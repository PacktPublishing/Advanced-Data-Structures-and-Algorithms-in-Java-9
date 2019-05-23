package Section_4;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class ArrayHeap<E> implements PriorityQueue<E> {
	protected E[] store;
    protected Comparator<E> comparator;
    int numElements = 0;
    
    public ArrayHeap(int size, Comparator<E> comparator){
        store = (E[]) new Object[size];
        this.comparator = comparator;
    }
    
    protected int parentIndex(int nodeIndex){
        return ((nodeIndex+1)/2)-1;
    }
    
    protected int leftChildIndex(int nodeIndex){
        return (nodeIndex+1)*2 -1;
    }
    
    protected int rightChildIndex(int nodeIndex){
        return (nodeIndex+1)*2;
    }
    
    protected void swap(int index1, int index2){
        E temp = store[index1];
        store[index1] = store[index2];
        store[index2] = temp;
    }
    
    protected void trickleUp(int position){
        int parentIndex = parentIndex(position);
        if(position> 0 && comparator.compare(store[parentIndex], store[position])>0){
            swap(position, parentIndex);
            trickleUp(parentIndex);
        }
    }
    
    public void insert(E value){
        if(numElements == store.length){
            throw new NoSpaceException("Insertion in a full heap");
        }
        store[numElements] = value;
        numElements++;
        trickleUp(numElements-1);
    }
    
    protected void trickleDown(int position){
        int leftChild = leftChildIndex(position);
        int rightChild = leftChild+1;
        if(rightChild<numElements) {
            if (comparator.compare(store[leftChild], store[rightChild]) < 0) {
                if (comparator.compare(store[leftChild], store[position]) < 0) {
                    swap(position, leftChild);
                    trickleDown(leftChild);
                }
            } else {
                if (comparator.compare(store[rightChild], store[position]) < 0) {
                    swap(position, rightChild);
                    trickleDown(rightChild);
                }
            }
        }else if(leftChild<numElements){
            if (comparator.compare(store[leftChild], store[position]) < 0) {
                swap(position, leftChild);
                trickleDown(leftChild);
            }
        }
    }
    
    public E removeMin(){
        if(numElements==0){
            return null;
        }else{
            E value  = store[0];
            store[0] = store[numElements-1];
            numElements--;
            trickleDown(0);
            return value;
        }
    }
    
    @Override
    public E checkMinimum() {
        if(numElements==0){
            return null;
        }else{
            return store[0];
        }
    }
    
    @Override
    public E dequeueMinimum() {
        return removeMin();
    }
    
    @Override
    public void enqueue(E value) {
        insert(value);
    }
    
    protected void displayText(int parent, int depth){

        for(int i=0;i<depth;i++){
            System.out.print("    ");
        }
        if(parent>=numElements){
            System.out.println("*");
            return;
        }
        System.out.println(store[parent]);
        displayText(leftChildIndex(parent),depth+1);
        displayText(rightChildIndex(parent),depth+1);
    }
    
    protected void displayText(){
        displayText(0, 0);
    }
    
    public static <E> void heapSort(E[] array, Comparator<E> comparator){
        ArrayHeap<E> arrayHeap = new ArrayHeap<E>(0, (a,b) -> comparator.compare(b,a));
        arrayHeap.store = array;
        for(int i=0;i<array.length;i++){
            arrayHeap.insert(array[i]);
        }
        for(int i=array.length-1;i>=0;i--){
            array[i] = arrayHeap.removeMin();
        }
    }
    
    public static void main(String [] args){
        ArrayHeap<Integer> heap = new ArrayHeap<>(200, (a,b)-> b-a);
        

        Integer[] array = new Integer[200];
        for(int i=0;i<200;i++){
            array[i] = (int)(Math.random()*1000);
        }
        System.out.println(Arrays.toString(array));
        ArrayHeap.heapSort(array, (a,b)->a-b);
        System.out.println(Arrays.toString(array));
    }
}