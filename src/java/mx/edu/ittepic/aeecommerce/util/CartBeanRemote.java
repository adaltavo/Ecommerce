/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.util;

import javax.ejb.Remote;

/**
 *
 * @author gustavo
 */
@Remote
public interface CartBeanRemote {
    public String addProduct(String productid, String productname);
    public String removeProduct(String productid);
    public void close();
    public void init();
    public String login(String username, String password);
    public String getUsername();
    public int getUserid();
    public String getRole(); 
    public String logout();
    
}
