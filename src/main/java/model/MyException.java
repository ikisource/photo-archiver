package model;

import java.util.Arrays;
import java.util.List;

public class MyException extends Exception {

    public enum BundleKey {
        BAD_PARAMETER,
        BAD_PARAMETER_FIELD;
    }

    private BundleKey bundleKey;
    private Object[] params;

    public MyException(BundleKey bundleKey, Object... params) {
        super("erreur " + bundleKey);

        this.bundleKey = bundleKey;
        this.params = params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        List<Object> set = Arrays.asList(params);
        return "MyException{" + "bundleKey=" + bundleKey + ", params=" + set + '}';
    }

}
