package model.lambda;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import model.Scanner2;

public interface Scanner<T> {

    //Long scan(Predicate<T> predicate);
//    Function<Predicate<T>, Exception> scan(T t);
    Exception scan(T t);
    
    //Function<T, Exception> scan(T t);

    Predicate<String> nonNullPredicate = Objects::nonNull;

    static <T> void check(Scanner2<T> scanner, T t) {

        scanner.scan(t);
    }

    public static <T> Predicate<T> chain(Function<T, Predicate<T>> mapFn, T[] args) {
        return Arrays.asList(args)
                .stream()
                .map(x -> mapFn.apply(x))
                .reduce(p -> false, Predicate::or);
    }

    public static void main(String[] args) {

        //ExceptionSupplier<Scanner> node = scan -> pred ->;
        Predicate<String> pr = string -> string.length() > 0;

//        Scanner<String> scan2 = s -> s -> stringNotBlank.test(s) -> new IllegalArgumentException("must be defined");
        Scanner<String> scan =  s -> {
            if (!pr.test(s)) return new IllegalArgumentException("lenght of " + " must be equals to");
            return null;
        };

        String nullString = null;


        //boolean outcome = nonNullPredicate.and(hasLengthOf10).test(nullString);
    }

}
