package Section_6;

public interface Consumer<E> {
	void onMessage(E message);
    default void onError(Exception error){
        error.printStackTrace();
    }
    default void onComplete(){}
}
