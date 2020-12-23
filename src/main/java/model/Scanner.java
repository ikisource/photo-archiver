package model;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Scanner<T> {
    
    Function<T, Exception> scan(Predicate<String> conditon);
    
    static <T> void check(Scanner<T> scanner, T t) {
        
        
    }
    
    public static void main(String[] args) {
        
        Node<FinalNode> node = () -> () -> "ok";
 
        System.out.println(node.run().execute());
        
        Predicate<String> isInteger = s -> s.matches("");
        
        // OK Scanner<String> s = p -> s2 -> new Exception("");
        
        Scanner<String> scan2 = s -> s2 -> new IllegalArgumentException(s + " must be defined");
    }
}

