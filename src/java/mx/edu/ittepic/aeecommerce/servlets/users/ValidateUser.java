/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.servlets.users;

import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.ittepic.aeecommerce.ejbs.EJBEcommerceUsers;
import static mx.edu.ittepic.aeecommerce.entities.Product_.productid;
import static mx.edu.ittepic.aeecommerce.entities.Product_.productname;
import mx.edu.ittepic.aeecommerce.util.CartBeanRemote;
import mx.edu.ittepic.aeecommerce.util.Message;

/**
 *
 * @author gustavo
 */
@WebServlet(name = "ValidateUser", urlPatterns = {"/ValidateUser"})
public class ValidateUser extends HttpServlet {

    @EJB
    private EJBEcommerceUsers ejb;

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
            out.println("<title>Servlet ValidateUser</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ValidateUser at " + request.getContextPath() + "</h1>");
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
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");

        String user = request.getParameter("username");
        String password = request.getParameter("password");
        CartBeanRemote cart = (CartBeanRemote) request.getSession().getAttribute("ejbsession");
        if (cart == null) {
            InitialContext ic;
            try {
                ic = new InitialContext();
                cart = (CartBeanRemote) ic.lookup("java:comp/env/ejb/CartBean");
                Message m = new GsonBuilder().create().fromJson(cart.login(user, password), Message.class);
                if (m.getCode() == 200) {
                    request.getSession().setAttribute("ejbsession", cart);
                    if (cart.getRole().equals("admin")) {
                        response.getWriter().print("{\"code\":200,\"role\":\"admin\"}");
                    } else if (cart.getRole().equals("user")) {
                        response.getWriter().print("{\"code\":200,\"role\":user}");
                    }
                    System.out.print(">>>>>>>>>>>>>>>>>>>>>200"+cart.getRole());
                } else {
                    System.out.print(">>>>>>>>>>>>>>>>>>>>>401");
                    response.getWriter().print("{\"code\":401}");
                    //response.sendRedirect("index.html");
                }
                //out.print(cart.addProduct(productid, productname));

            } catch (NamingException ex) {
                out.print(ex);
            }
        } else {//out.print(cart.addProduct(productid, productname));
            if (cart.getRole().equals("admin")) {
                response.sendRedirect("/index.html");
            }
            System.out.print(">>>>>>>>>>>>>>>>>>>>>400");
        }
        //response.getWriter().print(ejb.validate(user, password));

        //processRequest(request, response);
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
        processRequest(request, response);
    }

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
