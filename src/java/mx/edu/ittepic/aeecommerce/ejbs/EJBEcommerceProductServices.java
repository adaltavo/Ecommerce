/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.ejbs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import mx.edu.ittepic.aeecommerce.entities.Role;
import mx.edu.ittepic.aeecommerce.util.Message;

/**
 *
 * @author gustavo
 */
@Stateless
@Path("/product")
public class EJBEcommerceProductServices {
    @PersistenceContext
    private EntityManager entity;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @GET
    @Path("/list")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Role> GetRoles(){
        Message m=new Message();
        Gson gson=new GsonBuilder().create();
        List<Role> list= entity.createNamedQuery("Role.findAll").getResultList();
        m.setCode(200);
        m.setMsg(gson.toJson(list));
        m.setDetail("ok");
        
        return list;
    }
}
