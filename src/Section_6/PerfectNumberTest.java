package Section_6;

import java.util.concurrent.atomic.AtomicLong;

public class PerfectNumberTest {

	public static boolean isPerfect(long x){
        long div = 2;
        long sum=0;
        while(true){
            long quotient = x/div;
            if(quotient<div){
                break;
            }
            if(x%div==0){
                sum+=div;
                if(quotient!=div){
                    sum+=quotient;
                }
            }
            div++;
        }
        return 1+sum==x;
    }
	
	public static void findPerfectNumberWithProducerConsumer() throws InterruptedException{
        long start = System.currentTimeMillis();
        ProducerConsumerQueue<Long> queue = new ProducerConsumerQueue<>(4096, 4, (x)->{
            if(isPerfect(x)){
                System.out.println(x);
            }
        });

        for(long i=2;i<5_00_000;i++){
            queue.produce(i);
        }
        queue.markCompleted();
        queue.joinThreads();
        System.out.println("Time in ms: "+(System.currentTimeMillis()-start));
    }
	
	public static void findPerfectNumberWithSingleThread(){
        long start = System.currentTimeMillis();
        for(long i=2;i<5_00_000;i++){
            if(isPerfect(i)){
                System.out.println(i);
            }
        }
        System.out.println("Time in ms: "+(System.currentTimeMillis()-start));
    }
	
	public static void findPerfectNumbersWithFunctionalAPI(){
        long start = System.currentTimeMillis();
        EventStream<Long> stream = new EventStream<Long>() {
            AtomicLong next = new AtomicLong(0L);
            @Override
            public Long read() {
                Long ret = next.incrementAndGet();
                if(ret<=5_00_000L){
                    return ret;
                }
                return null;
            }
        };
        stream.filter((x)->x>1)
                .filter(EventStream::isPerfect)
                .consume((x)->{System.out.println(x);})
                .onError((x)->System.out.println(x))
                .process(4096,1,4);

        System.out.println("Time in ms: "+(System.currentTimeMillis()-start));
    }
	
	public static void main(String[] args) throws Exception{
		findPerfectNumberWithProducerConsumer();        
        findPerfectNumberWithSingleThread();
        findPerfectNumbersWithFunctionalAPI();
	}
}
