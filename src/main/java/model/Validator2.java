/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * @author olivier
 */
public interface Validator2<T> {

    static <T> void validate(T t, Consumer<T> consumer) {
        
        consumer.accept(t);
    }
    
    static <T> void validate(T t, Consumer<T>... validators) {
        
        for (Consumer<T> validator : validators) {
            validator.accept(t);
        }
    }
    
    static <T> void validate(T t, Stream<Consumer<T>> validators) {
        
        validators.forEach(validator -> validator.accept(t));
    } 

}
