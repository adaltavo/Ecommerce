/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.ejbs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import javax.ejb.Stateless;
import javax.persistence.TransactionRequiredException;
import mx.edu.ittepic.aeecommerce.entities.Role;
import mx.edu.ittepic.aeecommerce.util.Message;

/**
 *
 * @author gustavo
 */
@Stateless
public class EJBEcommerceRoles {

    @PersistenceContext
    private EntityManager entity;

    public String getRoles() {
        List<Role> listRoles;
        Message m = new Message();
        String msg = "";

        Query q = entity.createNamedQuery("Role.findAll");
        listRoles = q.getResultList();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        msg = gson.toJson(listRoles);

        m.setCode(200);
        m.setMsg(msg);
        m.setDetail("Ok");

        return gson.toJson(m);
    }

    public String updateRole(String roleid, String name) {
        Message m = new Message();
        Role r = new Role();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            /*    
        r.setRoleid(Integer.parseInt(roleid));
        r.setRolename(name);
        entity.refresh(entity.merge(r));
             */

 /*Manejado por el contenedor
            Query q = entity.createNamedQuery("Role.updateRole")
                    .setParameter("id", Integer.parseInt(roleid))
                    .setParameter("name", name);
            if (q.executeUpdate() == 1) {
                m.setCode(200);
                m.setMsg("El rol se actualizo correctamente");
                m.setDetail("ok: ");
            }
            else{
                m.setCode(404);
                m.setMsg(":(");
                m.setDetail("El rol no existe");
            }
             */
 /*
            Query q= entity.createNativeQuery("Update role set rolename='"+name+"' where roleid="+roleid);
             if (q.executeUpdate() == 1) {
                m.setCode(200);
                m.setMsg("El rol se actualizo correctamente");
                m.setDetail("ok: ");
            }
            else{
                m.setCode(404);
                m.setMsg(":(");
                m.setDetail("El rol no existe");
            }
             */
            r = entity.find(Role.class, Integer.parseInt(roleid));
            if (r == null) {
                m.setCode(404);
                m.setMsg("No se encontró el elemento");
                m.setDetail("");
            } else {
                r.setRolename(name);
                entity.merge(r);
                m.setCode(200);
                m.setMsg("El rol se actualizo correctamente");
                m.setDetail("ok: ");
            }
        } catch (NumberFormatException e) {
            m.setCode(406);
            m.setMsg("Error, el tipo de dato '" + roleid + "' debe ser entero");
            m.setDetail(e.getMessage());
        } catch (IllegalArgumentException e) {
            m.setCode(406);
            m.setMsg("Error, el id '" + roleid + "' ya se ha usado previamente");
            m.setDetail(e.getMessage());
        }

        return gson.toJson(m);
    }

    public String createRole(String roleid, String name) {
        Message m = new Message();
        Role r = new Role();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            //r.setRoleid(Integer.parseInt(roleid));
            r.setRolename(name);
            entity.persist(r);
            entity.flush();
            m.setCode(200);
            m.setMsg("El rol se insertó correctamente");
            m.setDetail("ok: " + r.getRoleid());
        } catch (Exception e) {

        }
        return gson.toJson(m);
    }

    public String getRole(String roleid) {
        Message m = new Message();
        Role r = new Role();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            /*
            r = entity.find(Role.class, Integer.parseInt(roleid));
            m.setCode(200);
            m.setMsg(gson.toJson(r));
            m.setDetail("ok");
            */
            Query q= entity.createNativeQuery("Select roleid,rolename from role where roleid="+roleid, Role.class);
            r=(Role) q.getSingleResult();
            m.setCode(200);
            m.setMsg(gson.toJson(r));
            m.setDetail("ok");
            
        } catch (Exception e) {

        }
        return gson.toJson(m);
    }

    public String getRoleByName(String rolename) {
        Message m = new Message();
        Role r = new Role();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            Query q = entity.createNativeQuery("select * from role where rolename like '" + rolename + "%'", Role.class)
                    .setParameter("name", rolename + "%");
            List<Role> list = q.getResultList();

            m.setCode(200);
            m.setMsg(gson.toJson(list));
            m.setDetail("ok");
        } catch (IllegalArgumentException e) {
            m.setCode(406);
            m.setMsg("Error, el tipo de dato '" + rolename + "' debe ser string");
            m.setDetail(e.getMessage());
        } catch (TransactionRequiredException e) {
            m.setCode(406);
            m.setMsg("Error, algo paso con la base de datos");
            m.setDetail(e.getMessage());
        }
        //catch(NumberFormatException e){}
        return gson.toJson(m);
    }

    public String deleteRole(String roleid) {
        Message m = new Message();
        Role r = new Role();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            r = entity.find(Role.class, Integer.parseInt(roleid));
            entity.remove(r);
            m.setCode(200);
            m.setMsg("eliminado correctamente");
            m.setDetail("ok");
        } catch (IllegalArgumentException e) {
            m.setCode(406);
            m.setMsg("Error, el tipo de dato '" + roleid + "' debe ser entero");
            m.setDetail(e.getMessage());
        } catch (TransactionRequiredException e) {
            m.setCode(406);
            m.setMsg("Error, algo paso con la base de datos");
            m.setDetail(e.getMessage());
        }
        //catch(NumberFormatException e){}

        return gson.toJson(m);
    }
    

}
