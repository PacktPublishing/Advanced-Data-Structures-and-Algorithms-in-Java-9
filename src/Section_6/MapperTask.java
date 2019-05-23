package Section_6;

public class MapperTask implements Task {
	OneArgumentExpressionWithException mapper;
    Task nextTask;
    
    public MapperTask(OneArgumentExpressionWithException mapper,Task nextTask) {
        this.mapper = mapper;
        this.nextTask = nextTask;
    }
}
