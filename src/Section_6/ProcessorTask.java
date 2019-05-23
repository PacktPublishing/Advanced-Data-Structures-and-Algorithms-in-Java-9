package Section_6;

public class ProcessorTask<E> implements Task {
	OneArgumentStatementWithException<E> processor;

    public ProcessorTask(OneArgumentStatementWithException<E> processor) {
        this.processor = processor;
    }
}
