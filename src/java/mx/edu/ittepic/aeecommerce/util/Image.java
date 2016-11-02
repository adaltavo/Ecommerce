/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.util;

import java.io.InputStream;

/**
 *
 * @author gustavo
 */
public class Image {
    
    
    
     public Image(String name, InputStream content) {
        this.name = name;
        this.content = content;
    }

    public Image() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }
    private String name;
    private InputStream content;

   
    
    
}
