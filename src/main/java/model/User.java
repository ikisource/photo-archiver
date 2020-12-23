package model;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class User {

    public static Set<String> provided;
    public final static Consumer<User> ageNotNull = user -> {if(user.age == null) throw new IllegalArgumentException("age must be defined");};
    public final static Consumer<User> nameNotNull = user -> {if(user.name == null) throw new IllegalArgumentException("name must be defined");};
    public final static Consumer<User> NAME = user -> {if(user.name == null && provided.contains("name")) throw new IllegalArgumentException("name must be defined");};
    public final static Stream<Consumer<User>> ALL = Stream.of(nameNotNull, ageNotNull);
    
    public Map<String, Consumer<User>> map;
    
    public enum Validator {
        NAME("name", ageNotNull);
        private String value;
        private Consumer<User> consumer;
        private Validator(String value, Consumer<User> consumer) {
            this.value = value;
            this.consumer = consumer;
        }
    }
    
    
    private String name;
    private String password;
    private Integer age;
    
    public User() {
        
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void validate() {
//        if (!ageNotNull.test(this)) {
//            System.out.println("user not valid");
//        };
        
        ageNotNull.accept(this);
    }

    public static void main(String[] args) {
        User user = new User();
        
        user.setName("toto");
        //Validator2.validate(user, nameNotNull, ageNotNull);
        Validator2.validate(user, User.ALL);
    }
}
