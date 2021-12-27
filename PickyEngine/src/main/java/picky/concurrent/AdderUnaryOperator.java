package picky.concurrent;

@FunctionalInterface
public interface AdderUnaryOperator<T> {

    T apply(T operand);

}
