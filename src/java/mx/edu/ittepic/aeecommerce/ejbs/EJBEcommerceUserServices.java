/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.ejbs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import mx.edu.ittepic.aeecommerce.entities.Product;
import mx.edu.ittepic.aeecommerce.entities.Sale;
import mx.edu.ittepic.aeecommerce.entities.Salesline;
import mx.edu.ittepic.aeecommerce.entities.Users;
import mx.edu.ittepic.aeecommerce.entities.UsersCart;
import mx.edu.ittepic.aeecommerce.util.Utilities;

/**
 *
 * @author gustavo
 */
@TransactionManagement(TransactionManagementType.CONTAINER)
@Stateless
@Path("/user")
public class EJBEcommerceUserServices {

    @PersistenceContext
    private EntityManager entity;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list")
    @GET
    public List<Users> getUsers() {
        return entity.createNamedQuery("Users.findAll").getResultList();
    }

    @Produces({MediaType.APPLICATION_JSON})
    @Path("/login")
    @GET
    public Object login(@QueryParam("username") String username, @QueryParam("password") String password) {
        Users u;
        try {
            u = (Users) entity.createNamedQuery("Users.findUser")
                    .setParameter("user", username)
                    .setParameter("password", Utilities.md5(password))
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NotAuthorizedException(Response.Status.UNAUTHORIZED);
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
        return u;
    }

    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @PUT
    @Path("/renewPassword")
    public Object renewPassword(UserService json) {
        Users u;
        try {
            u = (Users) entity.createNamedQuery("Users.findByApikeyId")
                    .setParameter("userid", json.userid)
                    .setParameter("apikey", json.apikey)
                    .setParameter("password", Utilities.md5(json.password))
                    .getSingleResult();
            u.setPassword(Utilities.md5(json.Npassword));
            entity.merge(u);

        } catch (NoResultException e) {
            throw new NotAuthorizedException(Response.Status.UNAUTHORIZED);
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
        return "{\"message\":\"ok\"}";
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/addProduct")
    public Object addProduct(UsersCart json, @QueryParam("apikey") String apikey) {
        Users u;
        List<CartResponse> response = new ArrayList<CartResponse>();
        try {
            u = (Users) entity.createNamedQuery("Users.findByApikey")
                    .setParameter("userid", json.getUserid())
                    .setParameter("apikey", apikey)
                    .getSingleResult();
            entity.persist(json);
            List q = entity.createNamedQuery("UsersCart.findByUseridPurchase")
                    .setParameter("userid", u.getUserid())
                    .setParameter("purchased", false)
                    .getResultList();
            for (Object item : q) {
                Object[] res = (Object[]) item;
                response.add(new CartResponse(res[0].toString(), res[1].toString(), (Long) res[2], (Double) res[3]));
            }

        } catch (NoResultException e) {
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
        return response;
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @TransactionAttribute(REQUIRED)
    @Path("/checkout")
    public Object checkout(UserService json) {
        String error = "excelente manu";
        Users u;
        double total = 0;
        List<CartResponse> response = new ArrayList<CartResponse>();
        List q;
        try {
            u = (Users) entity.createNamedQuery("Users.findByApikey")
                    .setParameter("userid", json.userid)
                    .setParameter("apikey", json.apikey)
                    .getSingleResult();
            q = entity.createNamedQuery("UsersCart.findByUseridPurchase")
                    .setParameter("userid", u.getUserid())
                    .setParameter("purchased", false)
                    .getResultList();
            if (q.isEmpty()) {
                throw new IllegalArgumentException(">>>>>>>>Empty Cart");
            }
            for (Object item : q) {
                Object[] res = (Object[]) item;
                response.add(new CartResponse(res[0].toString(), res[1].toString(), (Long) res[2], (Double) res[3]));
            }

            for (CartResponse c : response) {
                //disminuir stock de productos
                Product p = (Product) entity.createNamedQuery("Product.findByProductname")
                        .setParameter("productname", c.getProduct())
                        .getSingleResult();
                entity.refresh(p);
                if ((p.getStock() - c.getQuantity()) < 0) {
                    throw new NonUniqueResultException();
                }
                p.setStock((int) (long) (p.getStock() - c.getQuantity()));
                entity.merge(p);
                
                //////////
                //sacar total de venta
                total += c.getPriceperitem() * c.getQuantity();
            }
            //regstrar venta
            Sale sale = new Sale();
            sale.setDate(new Date());
            sale.setSaleid(0);
            error = "Error, el usuario no es válido";
            sale.setUserid(entity.find(Users.class, json.userid));
            sale.setAmount(total);
            entity.persist(sale);
            //////////
            //registrar detalleventa
            q = new ArrayList();
            for (CartResponse c : response) {

                Salesline detail = new Salesline();
                detail.setSaleslineid(0);
                Product p = (Product) entity.createNamedQuery("Product.findByProductname")
                        .setParameter("productname", c.getProduct())
                        .getSingleResult();
                detail.setProductid(p);
                detail.setPurchprice(p.getPurchprice());
                detail.setQuantity((int) (long) c.getQuantity());
                detail.setSaleprice(c.getPriceperitem());
                detail.setSaleid(sale);
                entity.persist(detail);

                q.add(detail);
            }
            entity.createNativeQuery("update users_cart set purchased=true where userid=" + u.getUserid()).executeUpdate();
            ///////////
            //Generar respuesta

        } catch (NoResultException e) {
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
        return q;
    }

    /*
   -------------------------------------------------------------------------------------------------------------- FALTA
    *terminar updates con imagenes (debe borrar imagen al actualizar)
    *Hacer método Remove para el carrito
    *Optimizar código
    
    
     */

 /*
    @XmlRootElement
    static class Usr{
        private String username;
        private int 
    }
     */
    static class CartResponse {

        private String product;
        private String user;
        private Long quantity;
        private Double priceperitem;

        public CartResponse(String user, String product, Long quantity, Double priceperitem) {
            this.product = product;
            this.user = user;
            this.quantity = quantity;
            this.priceperitem = priceperitem;
        }

        /*
        public CartResponse(String user, String product, String quantity) {
            this.product = product;
            this.user = user;
            this.quantity = quantity;
        }
         */
        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public Long getQuantity() {
            return quantity;
        }

        public void setQuantity(Long quantity) {
            this.quantity = quantity;
        }

        public Double getPriceperitem() {
            return priceperitem;
        }

        public void setPriceperitem(Double priceperitem) {
            this.priceperitem = priceperitem;
        }

    }

    //Clase que representa el JSON recibido a través del WS
    @XmlRootElement
    static class UserService {
        //{"userid":34,"password":"gustavo","apikey":"4c96f8324e3ba54a99e78249b95daa30","Npassword":"grecia"}
        //http://localhost:8080/AEEcommerce/webresources/user/renewPassword

        public void setUserid(Integer userid) {
            this.userid = userid;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setNpassword(String Npassword) {
            this.Npassword = Npassword;
        }

        public void setApikey(String apikey) {
            this.apikey = apikey;
        }

        @XmlElement
        Integer userid;
        @XmlElement
        String password;
        @XmlElement
        String Npassword;
        @XmlElement
        String apikey;

        public UserService() {

        }

        public UserService(Integer userid, String password, String Npassword, String apikey) {
            this.userid = userid;
            this.password = password;
            this.Npassword = Npassword;
            this.apikey = apikey;
        }

    }

}
