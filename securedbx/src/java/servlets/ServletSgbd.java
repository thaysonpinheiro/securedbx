package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import libraries.ConnectionSGBD;
import sgbd.SecureOracle;
import sgbd.SecurePostgreSql;
import sgbd.SecureSqlServer;


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
    
    protected ConnectionSGBD testConnection(ConnectionSGBD test){
         
        try {
            if(test.connection.isValid(0)){
                return test;
            } else {
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServletSgbd.class.getName()).log(Level.SEVERE, null, ex);
        }
        test = null;
        return test;
    }
     
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        processRequest(request, response);
        PrintWriter out = response.getWriter();
        
        String host = request.getParameter("host");
        String port = request.getParameter("port");
        String base = request.getParameter("base");
        String user = request.getParameter("user");
        String password = request.getParameter("password");
        String sgbd = request.getParameter("sgbd");
        
        if(host!="" && port!="" && base!="" && user!="" && password!=""){
            ConnectionSGBD con = new ConnectionSGBD(host, port, base, user, password, sgbd); 
            con = testConnection(con);

            if(con!=null){
                out.print(1);
                switch(sgbd){
                    case "postgresql":
                        SecurePostgreSql postgredb = new SecurePostgreSql(con);
                        String a = postgredb.pwdDefault();
                        System.out.println(a);
                        break;
                    case "oracle":
                        SecureOracle oracledb = new SecureOracle(con);
                        String resul = oracledb.pwdDefault();
                        System.out.println(resul);
                        break;
                    case "sqlserver":
                        SecureSqlServer sqlserverdb = new SecureSqlServer(con);
                        break;
                    default: 
                        break;
                }  
            }else{
                out.print(0);
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
