package Section_6;

import java.io.File;
import java.io.PrintStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeFixedLengthSpinlockQueue<E> {
	int nextEnqueueIndex;
    int nextDequeueIndex;
    E[] store;
    AtomicBoolean[] enqueueLocks;
    AtomicBoolean[] dequeueLocks;
    AtomicInteger currentElementCount = new AtomicInteger(0);
    int length;
    volatile boolean alive = true;
    
    public ThreadSafeFixedLengthSpinlockQueue(int length){
        this.length = length;
        store = (E[]) new Object[length];
        enqueueLocks = new AtomicBoolean[length];
        dequeueLocks = new AtomicBoolean[length];
        for(int i=0;i<length;i++){
            enqueueLocks[i] = new AtomicBoolean(false);
            dequeueLocks[i] = new AtomicBoolean(true);
        }
    }
    
    public void enqueue(E value) throws InterruptedException {
        while (true) {
            int index = nextEnqueueIndex;
            nextEnqueueIndex = (nextEnqueueIndex+1) % length;
            if(enqueueLocks[index].compareAndSet(false,true)){
                currentElementCount.incrementAndGet();
                store[index] = value;
                dequeueLocks[index].set(false);
                return;
            }
        }
    }
    
    public E dequeue() throws InterruptedException {
        while(alive) {
            int index = nextDequeueIndex;
            nextDequeueIndex = (nextDequeueIndex+1) % length;
            if(dequeueLocks[index].compareAndSet(false,true)){
                E value = store[index];
                enqueueLocks[index].set(false);
                currentElementCount.decrementAndGet();
                return value;
            }
        }
        throw new InterruptedException("");
    }
    
    public int currentElementCount(){
        return currentElementCount.get();
    }

    public void killDequeuers(){
        alive = false;
    }
    
    public void enqueueProducerOnly(E value ) throws InterruptedException{
        int halfLength = length/2;
        while (true) {

            int index = nextEnqueueIndex;
            nextEnqueueIndex = (nextEnqueueIndex+1) % length;
            if(enqueueLocks[index].compareAndSet(false,true)){
                int numberOfElements = currentElementCount.get();
                if(numberOfElements>=halfLength || (!currentElementCount.compareAndSet(numberOfElements, numberOfElements+1))){
                    enqueueLocks[index].set(false);
                    continue;
                }
                store[index] = value;
                dequeueLocks[index].set(false);
                return;
            }
        }
    }
    
    public static void main(String[] args) throws Exception{
    	final ThreadSafeFixedLengthSpinlockQueue<Integer> queue = new ThreadSafeFixedLengthSpinlockQueue<>(32);
        PrintStream out = new PrintStream(new File("E:\\\\Workspace\\\\Advanced Data Structures And Algorithms In Java 9\\\\src\\\\Section_6\\\\output1.txt"));
        long start = System.currentTimeMillis();
        
        Runnable dequeer = ()->{
            while(true) {
                int value = 0;
                try {
                    value = queue.dequeue();
                } catch (InterruptedException e) {
                    break;
                }
                out.println(Thread.currentThread().getId() +
                        "  " + (System.currentTimeMillis()-start) +" " + value);

            }
        };
        
        Runnable dequeer2 = ()->{
            while(true) {
                int value = 0;
                try {
                    value = queue.dequeue();
                    if(value>5)
                        queue.enqueue(value/2);
                } catch (InterruptedException e) {
                    break;
                }
                out.println(Thread.currentThread().getId() +
                        "  " + (System.currentTimeMillis()-start) +" " + value);
            }
        };
        
        Runnable enqueer = ()->{
            for(int i=0;i<10000;i++) {
                try {
                    queue.enqueueProducerOnly(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (queue.currentElementCount()>0);
            queue.killDequeuers();
        };
        
        for(int i=0;i<2;i++){
            new Thread(enqueer).start();
        }
        for(int i=0;i<10;i++){
            new Thread(dequeer2).start();
        }
        new Thread(dequeer2).start();
    }
}
