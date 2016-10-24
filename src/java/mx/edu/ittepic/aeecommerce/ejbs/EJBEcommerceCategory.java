/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.ejbs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;
import mx.edu.ittepic.aeecommerce.entities.Category;
import mx.edu.ittepic.aeecommerce.entities.Product;
import mx.edu.ittepic.aeecommerce.util.Message;

/**
 *
 * @author lis_h
 */
@Stateless
public class EJBEcommerceCategory {

    @PersistenceContext
    private EntityManager entity;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public String newCategory(String name) {

        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        //*************
        Category c = new Category();
        // Category c = entity.find(Category.class, Integer.parseInt(categoryid));
        c.setCategoryname(name);

        entity.persist(c);
        entity.flush();

        m.setCode(200);
        m.setMsg("todo hermoso");

        m.setDetail(c.getCategoryname());
        return gson.toJson(m);
    }

    public String deleteCategory(String id) {
        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        
        Category c= entity.find(Category.class, Integer.parseInt(id));
        
         try {
            if (c != null) {
                entity.remove(c);
                m.setCode(200);
                m.setMsg("ok");
                m.setDetail("Se elimino bien");

            } else {
                m.setCode(404);
                m.setMsg("Error");
                m.setDetail("No encontrado");
            }
        } catch (IllegalArgumentException e) {
            m.setCode(406);
            m.setMsg("Error, tipo de dato invalido");
            m.setDetail(e.getMessage());
        } catch (TransactionRequiredException e) {
            m.setCode(403);
            m.setMsg("Error, prohibido");
            m.setDetail(e.getMessage());
        }
        return gson.toJson(m);
    }


}
