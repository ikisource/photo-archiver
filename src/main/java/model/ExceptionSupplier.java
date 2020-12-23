package model;

public interface ExceptionSupplier<T, U> {
    
    MyException supply(T t, U u);
}
