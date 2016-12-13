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
import com.stripe.model.Token;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mx.edu.ittepic.aeecommerce.entities.Product;
import mx.edu.ittepic.aeecommerce.entities.Sale;
import mx.edu.ittepic.aeecommerce.entities.Salesline;
import mx.edu.ittepic.aeecommerce.entities.Users;
import mx.edu.ittepic.aeecommerce.servlets.sales.CheckoutStripe;

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
                total+=Double.parseDouble(items.get("item_price_"+(i+1)))*Integer.parseInt(items.get("item_quantity_"+(i+1)));
            }
            //Se registra el detalle de venta
            venta.setAmount(total);
            entity.persist(venta);
            
            error="Error, hubo un error con el pago";
            //////////////////////////////////////////////----------------------------------------------------Stripe
            stripeCheckout(venta.getAmount()+"");
            ///////////////////////////////////////////////////----------------------------------------------
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
    
    private void stripeCheckout(String amount) throws IllegalArgumentException
    {
        try {
            Stripe.apiKey = "pk_test_9PEolhrHd4neqTqmO4awlxNw";

            Map<String, Object> tokenParams = new HashMap<String, Object>();
            Map<String, Object> cardParams = new HashMap<String, Object>();
            cardParams.put("number", "4242424242424242");
            cardParams.put("exp_month", 11);
            cardParams.put("exp_year", 2017);
            cardParams.put("cvc", "314");
            tokenParams.put("card", cardParams);

            Token token=Token.create(tokenParams);
            ////////////////////---------------------------------------------------------------
            URL url = new URL("/AEEcommerce/CheckoutStripe"); //Enter URL here<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.connect();
            //JSONObject jsonObject = new JSONObject();
            //jsonObject.put("para_1", "arg_1");
            //Se envía el token al servlet
            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes("param=" + token.getId()+"&amount="+amount);
            wr.flush();
            wr.close();
            //Se lee la respuesta desde el servlet
            InputStream stream = httpURLConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            System.out.print(new GsonBuilder().create().toJson(total.toString()));
            //Log.d(">" + token.getCreated(), ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Ya termino" + token.getCreated());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException();
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException();
        } catch (InvalidRequestException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException();
        } catch (APIConnectionException ex) {
           ex.printStackTrace();
           throw new IllegalArgumentException();
        } catch (CardException ex) {
           ex.printStackTrace();
           throw new IllegalArgumentException();
        } catch (APIException ex) {
           ex.printStackTrace();
           throw new IllegalArgumentException();
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
