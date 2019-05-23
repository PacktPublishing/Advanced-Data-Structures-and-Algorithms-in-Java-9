package Section_6;

import Section_1.OneArgumentStatement;

public abstract class EventConsumer<E> {
	OneArgumentStatementWithException consumptionCode;
    EventStream<E> eventStream;
    Task taskList = null;
    private ProducerConsumerQueue<StreamEvent> queue;
    private OneArgumentStatement<Exception> errorHandler = (ex)->ex.printStackTrace();
    
    class StreamEvent{
        Object value;
        Task task;
    }
    
    private Task eventStreamToTask(EventStream stream){
        Task t = new ProcessorTask(consumptionCode);
        EventStream s = stream;
        while(s.previous !=null){
            if(s.mapper!=null)
                t = new MapperTask(s.mapper, t);
            else if(s.filter!=null){
                t = new FilterTask(s.filter, t);
            }
            s = s.previous;
        }
        return t;
    }
    
    EventConsumer(OneArgumentStatementWithException consumptionCode,EventStream<E> eventStream) {
        this.consumptionCode = consumptionCode;
        this.eventStream = eventStream;
        taskList = eventStreamToTask(eventStream);
    }
    
    public EventConsumer<E> onError(OneArgumentStatement<Exception> errorHandler){
        EventConsumer<E> consumer = new EventConsumer<E>(consumptionCode, eventStream) { };
        consumer.taskList = taskList;
        consumer.errorHandler = errorHandler;
        return consumer;
    }
    
    class ConsumerCodeContainer implements Consumer<StreamEvent>{
    	@Override
        public void onError(Exception error) {
            errorHandler.doSomething(error);
        }
    	
    	@Override
        public void onMessage(StreamEvent evt) {
            if(evt.task instanceof ProcessorTask){
                try {
                    ((ProcessorTask) evt.task).processor.doSomething(evt.value);
                } catch (Exception e) {
                    queue.sendError(e);
                }
            }else if(evt.task instanceof FilterTask){
                StreamEvent nextEvent = new StreamEvent();
                try {
                    if((Boolean)((FilterTask) evt.task).filter.compute(evt.value)) {
                        nextEvent.task =((FilterTask) evt.task).nextTask;
                        nextEvent.value = evt.value;
                        queue.produce(nextEvent);
                    }
                } catch (Exception e) {
                    queue.sendError(e);
                }
            }else if(evt.task instanceof MapperTask){
                StreamEvent nextEvent = new StreamEvent();
                try {
                    nextEvent.value = ((MapperTask) evt.task).mapper.compute(evt.value);
                    nextEvent.task = ((MapperTask) evt.task).nextTask;
                    queue.produce(nextEvent);
                } catch (Exception e) {
                    queue.sendError(e);
                }
            }
        }
    }
    
    public void process(int bufferSize,int numberOfProducerThreads,int numberOfConsumerThreads) {
    	queue = new ProducerConsumerQueue<>(bufferSize, numberOfConsumerThreads,new ConsumerCodeContainer());
        EventStream s = eventStream;
        
        while(s.previous !=null){
            s = s.previous;
        }
        
        EventStream startingStream=s;
        
        Runnable producerRunnable = ()->{
            while(true){
                Object value = startingStream.read();
                if(value==null){
                    break;
                }
                StreamEvent nextEvent = new StreamEvent();
                try {
                    nextEvent.value = value;
                    nextEvent.task = taskList;
                    queue.produceExternal(nextEvent);
                } catch (Exception e) {
                    queue.sendError(e);
                }
            }
            try {
                queue.markCompleted();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        
        Thread [] producerThreads = new Thread[numberOfProducerThreads];
        for(int i=0;i<numberOfProducerThreads;i++){
            producerThreads[i] = new Thread(producerRunnable);
            producerThreads[i].start();
        }
        for(int i=0;i<numberOfProducerThreads;i++){
            try {
                producerThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
