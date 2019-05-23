package Section_2;

public interface Queue<E> extends OrderedStore<E>
{
	void enqueue(E value);
	E dequeue();
	E peek();
	
	@Override
	default E checkFirst(){
		return peek();
	}
	
	@Override
    default void insert(E value){
        enqueue(value);
    }
	
	@Override
    default E pickFirst(){
        return dequeue();
    }
}
