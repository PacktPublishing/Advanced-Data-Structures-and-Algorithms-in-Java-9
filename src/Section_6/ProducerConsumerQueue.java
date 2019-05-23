package Section_6;

import java.io.File;
import java.io.PrintStream;

public class ProducerConsumerQueue<E> {
	enum EventType{
		INVOCATION,ERROR,COMPLETION
	}
	class Event{
        E value;
        Exception error;
        EventType eventType;
    }
	ThreadSafeFixedLengthSpinlockQueue<Event> queue;
    boolean alive = true;
    Thread [] threads;
    
    public ProducerConsumerQueue(int bufferSize,int threadCount,Consumer<E> consumer) {
    	queue = new ThreadSafeFixedLengthSpinlockQueue<>(bufferSize);
        threads = new Thread[threadCount];
        Runnable consumerCode = ()->{
            try{
                while(alive || queue.currentElementCount()>0){
                    Event e = queue.dequeue();
                    switch (e.eventType) {
                        case INVOCATION:
                            consumer.onMessage(e.value);
                            break;
                        case ERROR:
                            consumer.onError(e.error);
                            break;
                        case COMPLETION:
                            alive = false;
                            consumer.onComplete();
                    }
                }
                queue.killDequeuers();
            } catch (InterruptedException e) {
            } finally{
            }
        };
        for(int i=0;i<threadCount;i++) {
            threads[i] = new Thread(consumerCode);
            threads[i].start();
        }
    }
    
    public void produce(E value) throws InterruptedException {
        Event event = new Event();
        event.value = value;
        event.eventType = EventType.INVOCATION;
        queue.enqueue(event);
    }
    
    public void markCompleted() throws InterruptedException {
        Event event = new Event();
        event.eventType = EventType.COMPLETION;
        queue.enqueue(event);
    }
    
    public void sendError(Exception ex) {
        Event event = new Event();
        event.error = ex;
        event.eventType = EventType.ERROR;
        try {
            queue.enqueue(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void joinThreads() throws InterruptedException {
        for(Thread t: threads){
            t.join();
        }
    }
    
    public void produceExternal(E value) throws InterruptedException {
        Event event = new Event();
        event.value = value;
        event.eventType = EventType.INVOCATION;
        queue.enqueueProducerOnly(event);
    }
}
