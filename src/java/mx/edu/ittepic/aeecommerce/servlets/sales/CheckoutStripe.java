/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.servlets.sales;

import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.stripe.*;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gustavo
 */
@WebServlet(name = "CheckoutStripe", urlPatterns = {"/CheckoutStripe"})
public class CheckoutStripe extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CheckoutStripe</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CheckoutStripe at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        Stripe.apiKey = "sk_test_ArSJnaKVRLrB8MnQc5KZ1irw";
        String token = request.getParameter("param");
        try {
            System.out.print("token >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + token);
            Map<String, Object> chargeParams = new HashMap<String, Object>();
            chargeParams.put("amount", (int) (Math.random() * 10000)); // Amount in cents
            chargeParams.put("currency", "usd");
            //chargeParams.put("source", new GsonBuilder().create().fromJson(token.split("JSON: ")[1], Token.class));//NO funciona por el timestamp
            chargeParams.put("source", token);
            chargeParams.put("description", "Example charge");

            Charge charge = Charge.create(chargeParams);
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Cache-Control", "no-store");
            response.getWriter().print(new GsonBuilder().create().toJson(charge));
            System.out.println(new GsonBuilder().create().toJson(charge));
        } catch (CardException e) {
            // The card has been declined
        } catch (AuthenticationException ex) {
            Logger.getLogger(CheckoutStripe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidRequestException ex) {
            Logger.getLogger(CheckoutStripe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (APIConnectionException ex) {
            Logger.getLogger(CheckoutStripe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (APIException ex) {
            Logger.getLogger(CheckoutStripe.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
    
   <com.stripe.android.model.Token@440702821 id=> JSON: {
  "card": {
    "address_city": null,
    "address_country": null,
    "address_line1": null,
    "address_line2": null,
    "address_state": null,
    "address_zip": null,
    "country": "US",
    "currency": null,
    "cvc": null,
    "exp_month": 12,
    "exp_year": 2017,
    "fingerprint": null,
    "last4": "4242",
    "name": null,
    "number": null,
    "type": "Visa"
  },
  "created": "Nov 27, 2016 11:21:07 PM",
  "id": "tok_19KiVj2eZvKYlo2C4qSLszof",
  "livemode": false,
  "used": false
}
     */
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
