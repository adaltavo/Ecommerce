/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.ejbs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.edu.ittepic.aeecommerce.entities.Product;
import mx.edu.ittepic.aeecommerce.entities.Role;
import mx.edu.ittepic.aeecommerce.util.Message;

/**
 *
 * @author gustavo
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
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
    
    @GET
    @Path("/getProducts/{userid}/{apikey}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object getProducts(@PathParam("apikey") String apikey,@PathParam("userid") int userid){
        List<Product> products = new ArrayList<Product>();
        try{
            entity.createNamedQuery("Users.findByApikey")
                    .setParameter("apikey", apikey)
                    .setParameter("userid", userid)
                    .getSingleResult();
            products = entity.createNamedQuery("Product.findAll").getResultList();
            entity.clear();
            //entity.close();
        }catch (NoResultException e) {
            throw new ForbiddenException();
        } catch (NonUniqueResultException e) {
            throw new BadRequestException();
        } catch (IllegalArgumentException e) {
            throw new NotAcceptableException();
        } catch (TransactionRequiredException e) {
            throw new ForbiddenException();
        } catch (QueryTimeoutException e) {
            throw new ServiceUnavailableException();
        } catch (PessimisticLockException e) {
            throw new InternalServerErrorException();
        } catch (LockTimeoutException e) {
            throw new ServiceUnavailableException();
        } catch (PersistenceException e) {
            throw new InternalServerErrorException();
        }
        return products;
    }
}
