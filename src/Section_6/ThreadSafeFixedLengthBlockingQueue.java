package Section_6;

import java.io.File;
import java.io.PrintStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeFixedLengthBlockingQueue<E> {
	Semaphore underflowSemaphore;
    Semaphore overflowSemaphore;
    AtomicInteger nextEnqueueIndex;
    AtomicInteger nextDequeueIndex;
    
    E[] store;
    
    Semaphore [] enqueueLocks;
    Semaphore [] dequeueLocks;
    
    int length;
    
    boolean alive;
    
    public ThreadSafeFixedLengthBlockingQueue(int length){
        this.length = length;
        store = (E[]) new Object[length];
        nextEnqueueIndex = new AtomicInteger();
        nextDequeueIndex = new AtomicInteger();
        underflowSemaphore = new Semaphore(length);
        overflowSemaphore = new Semaphore(length);
        underflowSemaphore.acquireUninterruptibly(length);
        enqueueLocks = new Semaphore[length];
        dequeueLocks = new Semaphore[length];
        for(int i=0;i<length;i++){
            enqueueLocks[i] = new Semaphore(1);
            dequeueLocks[i] = new Semaphore(1);
            dequeueLocks[i].acquireUninterruptibly();
        }
    }
    
    public void enqueue(E value) throws InterruptedException {
        overflowSemaphore.acquire();
        int index = (length + nextEnqueueIndex.getAndIncrement() % length) % length;
        enqueueLocks[index].acquire();
        store[index] = value;
        dequeueLocks[index].release();
        underflowSemaphore.release();
    }
    
    public E dequeue() throws InterruptedException {
        while (alive && !underflowSemaphore.tryAcquire(1, TimeUnit.SECONDS));
        if(!alive){
            Thread.currentThread().interrupt();
        }
        int index = (length + nextDequeueIndex.getAndIncrement() % length) % length;
        dequeueLocks[index].acquire();
        E value = store[index];
        enqueueLocks[index].release();
        overflowSemaphore.release();
        return value;
    }
    
    public int currentElementCount(){
        return underflowSemaphore.availablePermits();
    }
    
    public void killDequeuers(){
        alive = false;
    }
}
