package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import libraries.ConnectionSGBD;
import org.json.JSONObject;
import sgbd.SecureOracle;
import sgbd.SecurePostgreSql;
import sgbd.SecureSqlServer;


/**
 *
 * @author Thayson
 */
@WebServlet(name = "ServletSgbd", urlPatterns = {"/ServletSgbd"})
public class ServletSgbd extends HttpServlet {
   
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
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
                    SecureSqlServer s = new SecureSqlServer(con);

                    ArrayList<JSONObject> r = new ArrayList<>();
                    r.add(s.sysAdminUsers);
                    r.add(s.dbOwnerUser);
                    r.add(s.saUser);
                    out.print(r);
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
    }
}
