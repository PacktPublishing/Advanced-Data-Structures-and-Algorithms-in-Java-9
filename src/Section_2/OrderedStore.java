package Section_2;

public interface OrderedStore<E> extends Iterable<E> {
	void insert(E value);
    E pickFirst();
    E checkFirst();
}
