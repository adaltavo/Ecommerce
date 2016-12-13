/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.ejbs;

import com.google.gson.GsonBuilder;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import mx.edu.ittepic.aeecommerce.servlets.sales.CheckoutStripe;
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
    @Path("/login/{username}/{password}")
    @GET
    public Object login(@PathParam("username") String username, @PathParam("password") String password) {
        Users u;
        try {
            entity.clear();
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
            entity.flush();

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

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/getCart/{userid}/{apikey}")
    public Object getCart(@PathParam("userid") int userid, @PathParam("apikey") String apikey) {
        Users u;
        entity.clear();
        List<CartResponse> response = new ArrayList<CartResponse>();
        try {
            u = (Users) entity.createNamedQuery("Users.findByApikey")
                    .setParameter("userid", userid)
                    .setParameter("apikey", apikey)
                    .getSingleResult();
            List q = entity.createNamedQuery("UsersCart.findByUseridPurchase")//Se obtiene el carrito de la base de datos
                    .setParameter("userid", u.getUserid())
                    .setParameter("purchased", false)
                    .getResultList();
            for (Object item : q) {//Formamos el carrito para la respuesta
                Object[] res = (Object[]) item;
                //response.add(new CartResponse(res[0].toString(), res[1].toString(), (Long) res[2], (Double) res[3]));
                response.add(new CartResponse(res[0].toString(), res[1].toString(), (Long) res[2],
                        (Double) res[3], res[4].toString(), res[5].toString(), res[6].toString(), (int)res[7]));
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
    @Path("/addProduct/{apikey}")
    public Object addProduct(UsersCart json, @PathParam("apikey") String apikey) {
        Users u;
        List<CartResponse> response = new ArrayList<CartResponse>();
        try {
            u = (Users) entity.createNamedQuery("Users.findByApikey")//Verificamos de que el usuario es válido
                    .setParameter("userid", json.getUserid())
                    .setParameter("apikey", apikey)
                    .getSingleResult();
            json.setPurchased(false);
            entity.persist(json);//Aquí se guarda el nuevo producto al carrito del usuario
            entity.flush();
            List q = entity.createNamedQuery("UsersCart.findByUseridPurchase")//Se obtiene el carrito de la base de datos
                    .setParameter("userid", u.getUserid())
                    .setParameter("purchased", false)
                    .getResultList();
            for (Object item : q) {//Formamos el carrito para la respuesta
                Object[] res = (Object[]) item;
                //response.add(new CartResponse(res[0].toString(), res[1].toString(), (Long) res[2], (Double) res[3]));
                response.add(new CartResponse(res[0].toString(), res[1].toString(), (Long) res[2],
                        (Double) res[3], res[4].toString(), res[5].toString(), res[6].toString(), (int)res[7]));
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

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/deleteProduct/{apikey}")
    public Object deleteProduct(UsersCart json, @PathParam("apikey") String apikey) {
        Users u;
        List<CartResponse> response = new ArrayList<CartResponse>();
        try {
            u = (Users) entity.createNamedQuery("Users.findByApikey")//Verificamos de que el usuario es válido
                    .setParameter("userid", json.getUserid())
                    .setParameter("apikey", apikey)
                    .getSingleResult();

            entity.flush();
            entity.createNamedQuery("UsersCart.deleteByUseridAndProduct")
                    .setParameter("userid", u.getUserid())
                    .setParameter("productid", json.getProductid())
                    .executeUpdate();
            List q = entity.createNamedQuery("UsersCart.findByUseridPurchase")//Se obtiene el carrito de la base de datos
                    .setParameter("userid", u.getUserid())
                    .setParameter("purchased", false)
                    .getResultList();
            for (Object item : q) {//Formamos el carrito para la respuesta
                Object[] res = (Object[]) item;
                //response.add(new CartResponse(res[0].toString(), res[1].toString(), (Long) res[2], (Double) res[3]));
                response.add(new CartResponse(res[0].toString(), res[1].toString(), (Long) res[2],
                        (Double) res[3], res[4].toString(), res[5].toString(), res[6].toString(), (int)res[7]));
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

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/deleteCart/{apikey}")
    public Object deleteCart(UsersCart json, @PathParam("apikey") String apikey) {
        Users u;
        int response = 0;
        try {
            u = (Users) entity.createNamedQuery("Users.findByApikey")//Verificamos de que el usuario es válido
                    .setParameter("userid", json.getUserid())
                    .setParameter("apikey", apikey)
                    .getSingleResult();

            int q = entity.createNamedQuery("UsersCart.deleteByUserid")//Se obtiene el carrito de la base de datos
                    .setParameter("userid", u.getUserid())
                    .executeUpdate();
            response = q;
            entity.flush();
        } catch (NoResultException e) {
            throw new ForbiddenException();
        } catch (NonUniqueResultException e) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>do babes");
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
            entity.clear();
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
                response.add(new CartResponse(res[0].toString(), res[1].toString(), (Long) res[2],
                        (Double) res[3], res[4].toString(), res[5].toString(), res[6].toString(), (int)res[7]));
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
            //Generar respuesta por stripe

            Stripe.apiKey = "sk_test_ArSJnaKVRLrB8MnQc5KZ1irw";
            String token = json.token;
            try {
                System.out.print("token >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + token);
                Map<String, Object> chargeParams = new HashMap<String, Object>();
                chargeParams.put("amount", (int)(sale.getAmount()*100)); // Amount in cents
                chargeParams.put("currency", "USD");
                //chargeParams.put("source", new GsonBuilder().create().fromJson(token.split("JSON: ")[1], Token.class));//NO funciona por el timestamp
                chargeParams.put("source", token);
                chargeParams.put("description", "charge from android");

                Charge charge = Charge.create(chargeParams);
                if(!charge.getStatus().equals("succeeded")){
                    throw new PessimisticLockException();
                }
                //response.getWriter().print(new GsonBuilder().create().toJson(charge));
                System.out.println(new GsonBuilder().create().toJson(charge));
            } catch (CardException e) {
                throw new NonUniqueResultException();
                // The card has been declined
            } catch (AuthenticationException ex) {
                
                Logger.getLogger(CheckoutStripe.class.getName()).log(Level.SEVERE, null, ex);
                throw new NonUniqueResultException();
            } catch (InvalidRequestException ex) {
                Logger.getLogger(CheckoutStripe.class.getName()).log(Level.SEVERE, null, ex);
                throw new NonUniqueResultException();
            } catch (APIConnectionException ex) {
                Logger.getLogger(CheckoutStripe.class.getName()).log(Level.SEVERE, null, ex);
                throw new PessimisticLockException();
            } catch (APIException ex) {
                Logger.getLogger(CheckoutStripe.class.getName()).log(Level.SEVERE, null, ex);
                throw new PessimisticLockException();
            }

            //////////////////////////////////////////////////////////////
            entity.flush();

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

        private String brand;
        private String image;
        private String currency;
        private String product;
        private String user;
        private int productid;
        private Long quantity;
        private Double priceperitem;

        public CartResponse(String user, String product, Long quantity, Double priceperitem) {
            this.product = product;
            this.user = user;
            this.quantity = quantity;
            this.priceperitem = priceperitem;
        }

        public CartResponse(String user, String product, Long quantity, Double priceperitem, String currency, String image, String brand) {
            this.brand = brand;
            this.image = image;
            this.currency = currency;
            this.product = product;//
            this.user = user;       //
            this.quantity = quantity;//
            this.priceperitem = priceperitem;//
        }

        public CartResponse(String user, String product, Long quantity, Double priceperitem, String currency, String image, String brand, int productid) {
            this.brand = brand;
            this.image = image;
            this.currency = currency;
            this.product = product;
            this.user = user;
            this.productid = productid;
            this.quantity = quantity;
            this.priceperitem = priceperitem;
        }
        

        public int getProductid() {
            return productid;
        }

        public void setProductid(int productid) {
            this.productid = productid;
        }

        
        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
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

        public void setToken(String token) {
            this.token = token;
        }

        @XmlElement
        Integer userid;
        @XmlElement
        String password;
        @XmlElement
        String Npassword;
        @XmlElement
        String apikey;
        @XmlElement
        String token;

        public UserService() {

        }

        public UserService(Integer userid, String password, String Npassword, String apikey, String token) {
            this.userid = userid;
            this.password = password;
            this.Npassword = Npassword;
            this.apikey = apikey;
            this.token = token;
        }

        public UserService(Integer userid, String password, String Npassword, String apikey) {
            this.userid = userid;
            this.password = password;
            this.Npassword = Npassword;
            this.apikey = apikey;
        }

    }

}
