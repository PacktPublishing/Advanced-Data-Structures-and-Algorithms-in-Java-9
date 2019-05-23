package Section_1;

@FunctionalInterface
public interface TwoArgumentExpression<A,B,R> {
    R compute(A lhs, B rhs);
}
