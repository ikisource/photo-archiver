package model;

import java.util.function.Predicate;

public interface Validate<T> {
    
    String doIt(Predicate<T> p);
    
    static <T> void analyse(Validate<T> validate, T t) {
        
        
    }
}
