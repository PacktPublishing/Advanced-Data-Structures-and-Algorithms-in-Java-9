package Section_1;

public class LinkedList<E> {
    private E head;
    private LinkedList<E> tail;
    private int length;

    protected LinkedList(){

    }

    protected LinkedList(E head, LinkedList<E> tail){
        this.head = head;
        this.tail = tail;
        this.length = tail.getLength()+1;
    }

    public static class EmptyList<E> extends LinkedList<E>{
        @Override
        public E head() {
            throw new NoValueException("head() invoked on empty list");
        }

        @Override
        public LinkedList<E> tail() {
            throw new NoValueException("tail() invoked on empty list");
        }

        @Override
        public void forEach(OneArgumentStatement<E> processor) {}

        @Override
        public <R> LinkedList<R> map(OneArgumentExpression<E, R> transformer) {
            return LinkedList.emptyList();
        }

        @Override
        public <R> R foldLeft(R initialValue, TwoArgumentExpression<R, E, R> computer) {
            return initialValue;
        }

        @Override
        public <R> R foldRight(TwoArgumentExpression<E, R, R> computer, R initialValue) {
            return initialValue;
        }

        @Override
        public LinkedList<E> filter(OneArgumentExpression<E, Boolean> selector) {
            return this;
        }

        @Override
        public <R> LinkedList<R> flatMap(OneArgumentExpression<E, LinkedList<R>> transformer) {
            return LinkedList.emptyList();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int getLength() {
            return 0;
        }
    }

    public static <E> LinkedList<E> emptyList(){
        return new EmptyList<>();
    }
    public E head(){
        return head;
    }
    public LinkedList<E> tail(){
        return tail;
    }
    public LinkedList<E> add(E value){
        return new LinkedList<>(value,this);
    }
    public void forEach(OneArgumentStatement<E> processor){
        processor.doSomething(head());
        tail().forEach(processor);
    }

    public <R> LinkedList<R> map(OneArgumentExpression<E,R> transformer){
        return new LinkedList<>(transformer.compute(head()), tail.map(transformer));
    }

    public LinkedList<E> append(LinkedList<E> rhs){
        return this.foldRight((x,l)->l.add(x),rhs);
    }

    public <R> LinkedList<R> flatMap(OneArgumentExpression<E, LinkedList<R>> transformer){
        return transformer.compute(head()).append(tail().flatMap(transformer));
    }

    public <R> R foldLeft(R initialValue, TwoArgumentExpression<R,E,R> computer){
        R newInitialValue = computer.compute(initialValue, head());
        return tail().foldLeft(newInitialValue, computer);
    }

    public <R> R foldRight(TwoArgumentExpression<E,R,R> computer, R initialValue){
        R computedValue = tail().foldRight(computer, initialValue);
        return computer.compute(head(), computedValue);
    }

    public LinkedList<E> filter(OneArgumentExpression<E, Boolean> selector){
        if(selector.compute(head())){
            return new LinkedList<E>(head(), tail().filter(selector));
        }else{
            return tail().filter(selector);
        }
    }
    public boolean isEmpty(){
        return false;
    }

    private static LinkedList<Integer> ofRange(int start, int end, LinkedList<Integer> tailList){
        if(start>=end){
            return tailList;
        }else{
            return ofRange(start+1, end, tailList).add(start);
        }
    }

    public static LinkedList<Integer> ofRange(int start, int end){
        return ofRange(start,end, LinkedList.emptyList());
    }

    public int getLength(){
        return length;
    }
}
