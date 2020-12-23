/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author olivier
 */
public interface MappingTool {

    static Set<String> getNames(Stream<User> mappings) {

        return mappings.map(u -> u.getName()).collect(Collectors.toSet());
    }

}
