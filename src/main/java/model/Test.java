package model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Olivier MATHE
 */
public class Test {

    public void display(Object object) {
        System.out.println("display Object " + object);
    }
    
    public void display2(String name) {
        
    }
    
    
    public static void main(String[] args) throws NoSuchMethodException {
        
        Test test = new Test();
        
        String o = new String("abc");
        test.display(o);
        
        Class<Test> clz = Test.class;
        Method m = clz.getMethod("display2", String.class);
        System.out.println(m.getParameters()[0].getName());
        System.out.println(m.getParameters()[0].isNamePresent());
    }
    
    private Map<String, Object> analyse(Object object, Map<String, Object> map) throws IllegalArgumentException, IllegalAccessException {
        
        Map<String, Object> m = new HashMap<String, Object>();
        
        if (object instanceof String) {
            for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
                for (Field field : clazz.getDeclaredFields()) {
                    m.put(field.getName(), field.get(object));
                }
            }
        }
        map.put("dto", m);
        return map;
    }
    
    
}
