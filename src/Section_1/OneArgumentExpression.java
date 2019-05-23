package Section_1;

@FunctionalInterface
public interface OneArgumentExpression<A,R> {
    R compute(A a);
}
