/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.ejbs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import javax.ejb.Stateless;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import mx.edu.ittepic.aeecommerce.entities.Company;
import mx.edu.ittepic.aeecommerce.entities.Role;
import mx.edu.ittepic.aeecommerce.entities.Users;
import mx.edu.ittepic.aeecommerce.util.Image;
import mx.edu.ittepic.aeecommerce.util.Message;
import mx.edu.ittepic.aeecommerce.util.Utilities;

/**
 *
 * @author gustavo
 */
@Stateless
public class EJBEcommerceUsers {

    @PersistenceContext
    private EntityManager entity;

    public String createUser(String username, String password, String phone,
            String neigborhood, String zipcode, String city, String country,
            String state, String region, String street, String email, String streetnumber,
            String photo, String cellphone, String companyid, String roleid, String gender) {

        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            String nPassword = Utilities.md5(password);
            Users user = new Users(0, nPassword, phone, neigborhood, zipcode, city,
                    country, state, region, street, streetnumber, photo, cellphone, gender.charAt(0));
            user.setUserid(null);
            Company c = entity.find(Company.class, Integer.parseInt(companyid));
            Role r = entity.find(Role.class, Integer.parseInt(roleid));
            user.setCompanyid(c);
            user.setRoleid(r);
            user.setUsername(username);
            user.setEmail(email);
            user.setApikey(Utilities.md5(username));
            entity.persist(user);
            entity.flush();

            m.setCode(200);
            m.setMsg("ok");
            m.setDetail("chido compa");

        } catch (IllegalArgumentException e) {
            m.setCode(406);
            m.setMsg("Error, tipo de dato invalido");
            m.setDetail(e.getMessage());
        } catch (TransactionRequiredException e) {
            m.setCode(403);
            m.setMsg("Error, prohibido");
            m.setDetail(e.getMessage());
        } catch (PersistenceException e) {
            m.setCode(500);
            m.setMsg("Error, Algo salió mal :(, vuelve a intentarlo");
            m.setDetail(e.getMessage());
        }

        return gson.toJson(m);

    }

    public String createUser(String username, String password, String phone,
            String neigborhood, String zipcode, String city, String country,
            String state, String region, String street, String email, String streetnumber,
            Image photo, String cellphone, String companyid, String roleid, String gender) {

        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            String nPassword = Utilities.md5(password);
            Users user = new Users(0, nPassword, phone, neigborhood, zipcode, city,
                    country, state, region, street, streetnumber, photo.getName(), cellphone, gender.charAt(0));
            user.setUserid(null);
            Company c = entity.find(Company.class, Integer.parseInt(companyid));
            Role r = entity.find(Role.class, Integer.parseInt(roleid));
            user.setCompanyid(c);
            user.setRoleid(r);
            user.setUsername(username);
            user.setEmail(email);
            user.setApikey(Utilities.md5(username));
            Path file;
            try {
                //file = Files.createTempFile(Paths.get("/var/www/images/users/"), "user-", ".png");
                try (InputStream input = photo.getContent()) {
                    if (input.available() > 0) {//Hay una imagen?
                        file = Files.createTempFile(Paths.get("/var/www/images/users/"), "user-", ".png");
                        System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + input.available());
                        Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
                        user.setPhoto(file.getFileName().toString());
                    } else {//usa umagen genérica
                        System.out.print("F>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + input.available());
                        user.setPhoto("user.png");
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(EJBEcommerceUsers.class.getName()).log(Level.SEVERE, null, ex);
            }

            entity.persist(user);
            entity.flush();

            m.setCode(200);
            m.setMsg("ok");
            m.setDetail("chido compa");

        } catch (IllegalArgumentException e) {
            m.setCode(406);
            m.setMsg("Error, tipo de dato invalido");
            m.setDetail(e.getMessage());
        } catch (TransactionRequiredException e) {
            m.setCode(403);
            m.setMsg("Error, prohibido");
            m.setDetail(e.getMessage());
        } catch (PersistenceException e) {
            m.setCode(500);
            m.setMsg("Error, Algo salió mal :(, vuelve a intentarlo");
            m.setDetail(e.getMessage());
        }

        return gson.toJson(m);

    }

    public String deleteUser(String userid) {
        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            Users user = entity.find(Users.class, Integer.parseInt(userid));
            if (user != null) {
                if (!user.getPhoto().equals("user.png")) {
                    Files.delete(Paths.get("/var/www/images/users/" + user.getPhoto()));
                }
                entity.remove(user);
                m.setCode(200);
                m.setMsg("ok");
                m.setDetail("todo hermoso tmb");

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
        } catch (PersistenceException e) {
            m.setCode(500);
            m.setMsg("Error, Algo salió mal :(, vuelve a intentarlo");
            m.setDetail(e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(EJBEcommerceUsers.class.getName()).log(Level.SEVERE, null, ex);
            m.setCode(403);
            m.setMsg("Error, un problema pasó con la imagen");
            m.setDetail(ex.getMessage());
        }

        return gson.toJson(m);
    }

    public String updateUser(String userid, String username, String password, String phone,
            String neigborhood, String zipcode, String city, String country,
            String state, String region, String street, String email, String streetnumber,
            Image photo, String cellphone, String companyid, String roleid, String gender) {

        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            String nPassword = Utilities.md5(password);
            Users user = entity.find(Users.class, Integer.parseInt(userid));
            Company c = entity.find(Company.class, Integer.parseInt(companyid));
            Role r = entity.find(Role.class, Integer.parseInt(roleid));
            user.setUsername(username);
            user.setPassword(nPassword);
            user.setPhone(phone);
            user.setNeigborhood(neigborhood);
            user.setZipcode(zipcode);
            user.setCity(city);
            user.setCountry(country);
            user.setState(state);
            user.setRegion(region);
            user.setStreet(street);
            user.setEmail(email);
            user.setStreetnumber(streetnumber);
            
            Path file;
            try {
                //file = Files.createTempFile(Paths.get("/var/www/images/users/"), "user-", ".png");
                try (InputStream input = photo.getContent()) {
                    if (input.available() > 0) {//Hay una imagen?
                        file = Files.createTempFile(Paths.get("/var/www/images/users/"), "user-", ".png");
                        System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + input.available());
                        Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
                        user.setPhoto(file.getFileName().toString());
                    } else {//usa umagen genérica
                        System.out.print("F>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + input.available());
                        user.setPhoto("user.png");
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(EJBEcommerceUsers.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            user.setCellphone(cellphone);
            user.setRoleid(r);
            user.setCompanyid(c);
            user.setGender(gender.charAt(0));
            user.setApikey(Utilities.md5(username));

            if (user != null) {
                entity.merge(user);
                m.setCode(200);
                m.setMsg("ok");
                m.setDetail("todo hermoso tmb");

            } else {
                m.setCode(404);
                m.setMsg("Error");
                m.setDetail("No encontrado");
            }
        } catch (TransactionRequiredException e) {
            m.setCode(403);
            m.setMsg("Error, prohibido");
            m.setDetail(e.getMessage());
        } catch (PersistenceException e) {
            m.setCode(500);
            m.setMsg("Error, Algo salió mal :(, vuelve a intentarlo");
            m.setDetail(e.getMessage());
        }

        return gson.toJson(m);

    }

    public String validate(String user, String password) {
        Query q;
        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            q = entity.createNamedQuery("Users.findUser").setParameter("user", user).setParameter("password", password);
            q.getSingleResult();
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

    public String getUsers() {
        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        List<Users> ul;
        try {
            ul = entity.createNamedQuery("Users.findAll").getResultList();
            m.setCode(200);
            m.setDetail(gson.toJson(ul));
            m.setMsg(gson.toJson(ul));

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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
