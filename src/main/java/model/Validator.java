package model;

import java.util.function.Consumer;
import java.util.function.Predicate;

@FunctionalInterface
public interface Validator {

    void validate(Predicate<?> condition, Consumer<Exception> action);

}
