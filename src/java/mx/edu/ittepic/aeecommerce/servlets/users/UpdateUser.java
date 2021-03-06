/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.servlets.users;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import mx.edu.ittepic.aeecommerce.ejbs.EJBEcommerceUsers;
import mx.edu.ittepic.aeecommerce.util.Image;

/**
 *
 * @author gustavo
 */
@WebServlet(name = "UpdateUser", urlPatterns = {"/UpdateUser"})
@MultipartConfig
public class UpdateUser extends HttpServlet {
    @EJB
    EJBEcommerceUsers ejb;
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
            out.println("<title>Servlet UpdateUser</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateUser at " + request.getContextPath() + "</h1>");
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
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        String userid = request.getParameter("userid");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");

        String neigborhood = request.getParameter("neigborhood");
        String zipcode = request.getParameter("zipcode");
        String city = request.getParameter("city");
        String country = request.getParameter("country");

        String state = request.getParameter("state");
        String region = request.getParameter("region");
        String street = request.getParameter("street");
        String email = request.getParameter("email");
        String streetnumber = request.getParameter("streetnumber");

        //String photo = request.getParameter("photo");
        String cellphone = request.getParameter("cellphone");
        String companyid = request.getParameter("companyid");
        String roleid = request.getParameter("roleid");
        String gender = request.getParameter("gender");
        
        Part foto= request.getPart("photo");
        String fotoname = Paths.get(foto.getSubmittedFileName()).getFileName().toString();
        InputStream fcontent = foto.getInputStream();
        Image imagen = new Image(fotoname, fcontent);
        
        
        response.getWriter().print(ejb.updateUser(userid,username, password, phone, neigborhood, 
                zipcode, city, country, state, region, street, email, streetnumber, 
                imagen, cellphone, companyid, roleid, gender));
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
