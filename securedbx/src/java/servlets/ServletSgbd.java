package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import libraries.ConnectionSGBD;
import sgbd.SecureOracle;
import sgbd.SecurePostgreSql;
import sgbd.SecureSqlServer;

import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author Thayson
 */
@WebServlet(name = "ServletSgbd", urlPatterns = {"/ServletSgbd"})
public class ServletSgbd extends HttpServlet {

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
        

        /*
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Sgbd</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Sgbd at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }*/
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
        
        processRequest(request, response);
        response.setContentType("application/json");  
        PrintWriter out = response.getWriter();
        
        String host = request.getParameter("host");
        String port = request.getParameter("port");
        String base = request.getParameter("base");
        String user = request.getParameter("user");
        String password = request.getParameter("password");
        String sgbd = request.getParameter("sgbd");

        ConnectionSGBD driver = new ConnectionSGBD(host, port, base, user, password, sgbd); 

        if(driver.estado == 1){

            SecureSqlServer sqlserver = new SecureSqlServer(driver);
            

            try {
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonPage = new JSONObject();
                switch(sgbd){
                    case "postgresql":
                        jsonPage.put("page", "postgresql.jsp");
                        break;
                    case "oracle":
                        jsonPage.put("page", "oracle.jsp");
                        break;
                    case "sqlserver":
                        jsonPage.put("page", "sqlserver.jsp");
                        break;
                }
                jsonArray.put(jsonPage);

                /* Efetuando a a checagem */
                ArrayList sysAdminUsers = null;
                if(sysAdminUsers!=null){
                    /* Criando um objeto para armazenar os usuários */                
                    JSONObject jsonSysAdminUsers = new JSONObject();
                    
                    /* Criando uma table com os dados da checagem */
                    String table = "<table><tr>";
                    for (int i = 0; i < sysAdminUsers.size(); i++) {  
                        table += "<td>" + sysAdminUsers.get(i) + "</td>";
                    }   
                    table += "</tr></table>";
                    
                    /* Montando o objeto */
                    jsonSysAdminUsers.put("p4", table);
                    
                    /* Adicionando o objeto JSON ao array de objetos JSON */
                    jsonArray.put(jsonSysAdminUsers);
                }
                
                out.print("sqlserver.jsp");
                
            } catch (JSONException ex) {
                Logger.getLogger(ServletSgbd.class.getName()).log(Level.SEVERE, null, ex);
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
    }
}
