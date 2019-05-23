package Section_6;

import java.util.concurrent.Semaphore;

public class SemaphoreExample {
	volatile int threadSafeInt=0;
	Semaphore semaphore=new Semaphore(1);
	
	public int incremementAndGet() throws InterruptedException{
        semaphore.acquire();
        int previousValue = threadSafeInt++;
        semaphore.release();
        return previousValue;
    }
}
