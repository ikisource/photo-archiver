package model.lambda;

import java.util.function.Function;
import java.util.function.Predicate;
import model.MyException;
import model.MyException.BundleKey;

public class Controller {

    static Function<Object[], MyException> myException = params -> new MyException(BundleKey.BAD_PARAMETER, params);
    
    public <T, P> void scan(T t, Predicate<T> condition, Function<Object[], MyException> exception, P... params) throws MyException {

        if (!condition.test(t)) {
            throw exception.apply(params);
        }
    }

    public static void main(String[] args) throws Exception {

        Controller controller = new Controller();

        String when = "toto";
        controller.scan(when, s -> s.length() == 5, myException, "when", 150);

    }

}
