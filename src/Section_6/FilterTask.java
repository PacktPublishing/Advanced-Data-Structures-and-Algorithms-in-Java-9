package Section_6;

public class FilterTask implements Task {
	OneArgumentExpressionWithException filter;
    Task nextTask;
    
    public FilterTask(OneArgumentExpressionWithException filter,Task nextTask) {
        this.filter = filter;
        this.nextTask = nextTask;
    }
}
