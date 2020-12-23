package model;

import java.util.function.Predicate;

public interface FinalNode {
    
    String execute();
    
    public static void main(String[] args) {
        
        Node<FinalNode> node = () -> () -> "ok";
 
        System.out.println(node.run().execute());
        
        Predicate<String> isInteger = s -> s.matches("");
        
        
    }
}
