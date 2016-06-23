/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import libraries.ConnectionSGBD;

/**
 *
 * @author Thayson
 */
@WebServlet(name = "ServletConnection", urlPatterns = {"/ServletConnection"})
public class ServletConnection extends HttpServlet {

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
            out.println("<title>Servlet ServletConnection</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletConnection at " + request.getContextPath() + "</h1>");
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

        PrintWriter out = response.getWriter();
        
        String host = request.getParameter("host");
        String port = request.getParameter("port");
        String base = request.getParameter("base");
        String user = request.getParameter("user");
        String password = request.getParameter("password");
        String sgbd = request.getParameter("sgbd");

        ConnectionSGBD con = new ConnectionSGBD(host, port, base, user, password, sgbd); 

        if(con.estado == 1){
           /* Cookie cookieHost = new Cookie("host", host);
            Cookie cookiePort = new Cookie("port", port);
            Cookie cookieBase = new Cookie("base", base);
            Cookie cookieUser = new Cookie("user", user);
            Cookie cookiePassword = new Cookie("pass", password);
            Cookie cookieSgbd = new Cookie("sgbd", sgbd);        

            response.addCookie(cookieHost);
            response.addCookie(cookiePort);
            response.addCookie(cookieBase);
            response.addCookie(cookieUser);
            response.addCookie(cookiePassword);
            response.addCookie(cookieSgbd);   */

            switch(sgbd){
                case "postgresql":
                    out.print("sqlserver.jsp");
                    break;
                case "oracle":
                    out.print("oracle.jsp");
                    break;
                case "sqlserver":
                    out.print("sqlserver.jsp");
                    break;
            }  
        }else{
            out.print(0);
        }
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
