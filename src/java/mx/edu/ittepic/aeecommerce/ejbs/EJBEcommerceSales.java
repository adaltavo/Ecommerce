/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.ejbs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mx.edu.ittepic.aeecommerce.entities.Product;
import mx.edu.ittepic.aeecommerce.entities.Sale;
import mx.edu.ittepic.aeecommerce.entities.Salesline;
import mx.edu.ittepic.aeecommerce.entities.Users;

/**
 *
 * @author gustavo
 */
@TransactionAttribute(NOT_SUPPORTED)
@Stateless
public class EJBEcommerceSales {

    @PersistenceContext
    private EntityManager entity;

    @TransactionAttribute(REQUIRED)
    public String Checkout(Map<String, String> items) {
         String error="excelente manu";
        try {
           
            //Quito los productos del stock
            error="Error, datos inconsistentes del producto";
            Product producto[] = new Product[Integer.parseInt(items.get("itemCount"))];
            for(int i=0; i<producto.length;i++){//esto puede fallar
                error="Error, Algun(os) producto(s) no existe(n)";
                producto[i]=(Product)entity.createNamedQuery("Product.findByProductname").setParameter("productname", items.get("item_name_"+(i+1))).getSingleResult();
                error="Error, no hay suficiente stock";
                producto[i].setStock(producto[i].getStock()-Integer.parseInt(items.get("item_quantity_"+(i+1))));
                if(producto[i].getStock()<0){
                    throw (new IllegalArgumentException());
                }
                 error="Error, datos inconsistentes del producto(2)";
                entity.merge(producto[i]);
            }
            
            //Se registra la venta
            error="Error, datos inconsistentes de venta";
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Sale venta = new Sale();
            venta.setDate(new Date());
            error="Error, el usuario no es válido";
            venta.setUserid(entity.find(Users.class, 6));
            double total=0;
            for(int i=0;i<Integer.parseInt(items.get("itemCount"));i++){
                total+=Double.parseDouble(items.get("item_price_"+(i+1)));
            }
            //Se registra el detalle de venta
            venta.setAmount(total);
            entity.persist(venta);
            error="Error, datos inconsistentes del detalleventa";
            Salesline detalleVenta[]= new Salesline[producto.length];
            for(int i=0; i<producto.length;i++){
                detalleVenta[i]=new Salesline();
                error="Error, datos inconsistentes del producto (no existe)";
                detalleVenta[i].setProductid(producto[i]);
                detalleVenta[i].setPurchprice(producto[i].getPurchprice());
                detalleVenta[i].setQuantity(Integer.parseInt(items.get("item_quantity_"+(i+1))));
                error="Error, datos inconsistentes de la venta (No existe)";
                detalleVenta[i].setSaleid(venta);
                detalleVenta[i].setSaleprice(Double.parseDouble(items.get("item_price_"+(i+1))));
                error="Error, datos inconsistentes del detalleventa (2)";
                entity.persist(detalleVenta[i]);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return items.get("cancel_return")+"?error="+error;
        }
        return items.get("return")+"?hue="+"Exito!";
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
