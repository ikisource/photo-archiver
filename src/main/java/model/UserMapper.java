package model;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface UserMapper {

    enum Mappings {
        A("toto"), B("titi"), C("tata");
        
        private String name;

        public String getName() {
            return name;
        }
        
        private Mappings(String name) {
            this.name = name;
        }
        
    }

    enum Numbers {
        ONE, TWO, THREE, FOUR, FIVE
    };

    EnumSet<Numbers> mappings = EnumSet.of(Numbers.ONE, Numbers.TWO);
    
    static Set<String> groups() {
        return EnumSet.of(Mappings.A, Mappings.B).stream().map(m -> m.getName()).collect(Collectors.toSet());
    }
    
    static void test() {
        
        //Mappings.A.lookup("name");
        
        // UserMapper.Mappings.A;
         //UserMapper.Mappings.B.
         
         
    }
}
