package Section_6;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeCounter {
	AtomicInteger counter;
    public int incrementAndGet(){
        while (true){
            int value = counter.get();
            if(counter.compareAndSet(value, value+1)){
                return value;
            }
        }
    }
}
