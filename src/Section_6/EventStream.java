package Section_6;

public abstract class EventStream<E> {
	EventStream previous;
    OneArgumentExpressionWithException mapper;
    OneArgumentExpressionWithException filter;
    
    public abstract E read();
    
    public <R> EventStream<R> map(OneArgumentExpressionWithException<E,R> mapper){
        EventStream<R> mapped = new EventStream<R>() {

            @Override
            public R read() {
                return null;
            }
        };
        mapped.mapper = mapper;
        mapped.previous = this;
        return mapped;
    }
       
    public EventConsumer<E> consume(OneArgumentStatementWithException<E> consumer){
        EventConsumer eventConsumer = new EventConsumer(consumer, this) {};
        return eventConsumer;
    }

    public EventStream<E> filter(OneArgumentExpressionWithException<E,Boolean> filter){
        EventStream<E> mapped = new EventStream<E>() {

            @Override
            public E read() {
                return null;
            }
        };
        mapped.filter = filter;
        mapped.previous = this;
        return mapped;
    } 
    
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
}
