package model;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Scanner2<T> {
    
    Function<T, Exception> scan(T t);
    Predicate<String> positiveInteger = s -> s.matches("");
    
    static <T> void check(Scanner2<T> scanner, T t) {
        
        scanner.scan(t);
    }
    
    public static void main(String[] args) {
        
        Node<FinalNode> node = () -> () -> "ok";
 
        System.out.println(node.run().execute());
        
        Predicate<String> isInteger = s -> s.matches("");
        
        Predicate<String> lenght5 = s -> s.length() == 5;
        
        // OK Scanner<String> s = p -> s2 -> new Exception("");
        
        Scanner<String> scan2 = s -> s2 -> new IllegalArgumentException(s + " must be defined");
        
        Scanner2<String> scanner2 = s ->  s2 -> {
            if (isInteger.test(s2)) {
                return new IllegalArgumentException(" must be defined");
            }
            return null;
        };
        
        Scanner2<String> scanner3 = s ->  s2 -> isInteger.test(s2) ? new IllegalArgumentException("must be defined"): null;
        
        Supplier<IllegalArgumentException> supplier1 = () -> new IllegalArgumentException("must be defined");
        
        Long when = 120L;
        // validator.scan(when, integerScanner);
        
        check(scanner3, "node");
    }
    
    
}

