/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author olivier
 */
public class MyException2 extends Exception {
    
    private String bundleKey;
    
    public MyException2(String bundleKey) {
        super("erreur2 " + bundleKey);
        this.bundleKey = bundleKey;
    }
}
