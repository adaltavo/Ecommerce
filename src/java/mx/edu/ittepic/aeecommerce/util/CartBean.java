/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.servlet.http.HttpSession;
import mx.edu.ittepic.aeecommerce.entities.Product;
import mx.edu.ittepic.aeecommerce.entities.Users;

/**
 *
 * @author gustavo
 */
@Stateful
@Remote(CartBeanRemote.class)
@EJB(name = "ejb/CartBean", beanInterface = CartBeanRemote.class, beanName = "CartBean")
public class CartBean implements CartBeanRemote {

    @PersistenceContext
    EntityManager entity;
    List<Product> cart;
    private String username;
    private String role;
    private int userid;
    
    @Override
    public String getRole() {
        return role;
    }
    

    @Override
    public String addProduct(String productid, String productname) {
        Product p = new Product();
        p.setProductid(Integer.parseInt(productid));
        p.setProductname(productname);

        cart.add(p);

        return new GsonBuilder().create().toJson(cart);
    }

    @Override
    public String removeProduct(String productid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Remove
    public void close() {

    }

    @Override
    @PostConstruct
    public void init() {
        cart = new ArrayList<>();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public String login(String username, String password) {
        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Users user;
        try {
            user = (Users) entity.createNamedQuery("Users.findUser").setParameter("user", username).setParameter("password", password).getSingleResult();
            this.username = user.getUsername();
            this.userid = user.getUserid();
            this.role= user.getRoleid().getRolename();
            m.setCode(200);
            m.setMsg("Bienvenido");
            m.setDetail("Ok");

        } catch (NoResultException e) {
            m.setCode(401);
            m.setMsg("Error");
            m.setDetail("No existe el usuario");
        } catch (NonUniqueResultException e) {
            m.setCode(400);
            m.setMsg("Error");
            m.setDetail("El usuario ya existe");
        } catch (IllegalArgumentException e) {
            m.setCode(406);
            m.setMsg("Error, tipo de dato invalido");
            m.setDetail(e.getMessage());
        } catch (TransactionRequiredException e) {
            m.setCode(403);
            m.setMsg("Error, prohibido");
            m.setDetail(e.getMessage());
        } catch (QueryTimeoutException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (PessimisticLockException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (LockTimeoutException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (PersistenceException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        }
        return gson.toJson(m);
    }
    
    

    @Override
    public String getUsername() {
        return username; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getUserid() {
        return userid; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String logout() {
        ((HttpSession) FacesContext.getCurrentInstance()
        .getExternalContext().getSession(false)).invalidate();
        return "";
    }
}
