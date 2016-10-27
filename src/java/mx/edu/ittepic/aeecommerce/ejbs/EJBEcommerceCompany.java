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
import javax.persistence.LockTimeoutException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import mx.edu.ittepic.aeecommerce.entities.Category;
import mx.edu.ittepic.aeecommerce.entities.Company;
import mx.edu.ittepic.aeecommerce.util.Message;

/**
 *
 * @author gustavo
 */
@Stateless
public class EJBEcommerceCompany {

    @PersistenceContext
    private EntityManager entity;

    public String newCompany(String companyname, String neighborhood, String zipcode, String city, String country,
            String state, String region, String street, String streetnumber, String phone, String rfc, String logo) {
        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        //***********
        try {
            Company co = new Company();

            //Category c = entity.find(Category.class, Integer.parseInt(categoryid));
            co.setCompanyname(companyname);
            co.setNeighborhood(neighborhood);
            co.setZipcode(zipcode);
            co.setCity(city);
            co.setCountry(country);
            co.setState(state);
            co.setRegion(region);
            co.setStreet(street);
            co.setStreetnumber(streetnumber);
            co.setPhone(phone);
            co.setRfc(rfc);
            co.setLogo(logo);

            entity.persist(co);
            entity.flush();

            m.setCode(200);
            m.setMsg("todo hermoso");
            m.setDetail(co.toString());

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

    public String getCompanies() {
        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            Query q = entity.createNamedQuery("Company.findAll");
            List<Company> companies = q.getResultList();
            m.setCode(200);
            m.setDetail("ok");
            m.setMsg(gson.toJson(companies));

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

    public String updateCompany(String id, String companyname, String neighborhood, String zipcode, String city, String country,
            String state, String region, String street, String streetnumber, String phone, String rfc, String logo) {
        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        //***********
        Company c = entity.find(Company.class, Integer.parseInt(id));

        try {
            
            if(c!=null){
            Company co = new Company();

            //Category c = entity.find(Category.class, Integer.parseInt(categoryid));
            co.setCompanyname(companyname);
            co.setNeighborhood(neighborhood);
            co.setZipcode(zipcode);
            co.setCity(city);
            co.setCountry(country);
            co.setState(state);
            co.setRegion(region);
            co.setStreet(street);
            co.setStreetnumber(streetnumber);
            co.setPhone(phone);
            co.setRfc(rfc);
            co.setLogo(logo);

            entity.persist(co);
            entity.flush();

            m.setCode(200);
            m.setMsg("todo hermoso");
            m.setDetail(co.toString());
            }else{
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
        }

        return gson.toJson(m);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
