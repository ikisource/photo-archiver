package model;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Stream;

public interface Mapping {
    
    Stream<User> users = Arrays.stream(MyEnum.values()).map(m -> m.user);
            
    public enum MyEnum implements MappingTool {

        ID(new User("Olivier", "pwd1")), NAME(new User("Nathalie", "pwd2"));
        private final User user;

        private MyEnum(User user) {
            this.user = user;
        }
    }

    public static void main(String[] args) {

        EnumSet eSet = EnumSet.allOf(Mapping.MyEnum.class);
        EnumSet e2 = EnumSet.allOf(Mapping.MyEnum.class);
                
        System.out.println(eSet);
        eSet.forEach(System.out::println);
        
    }
}
