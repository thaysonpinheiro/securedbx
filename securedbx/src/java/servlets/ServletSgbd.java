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
            
            ArrayList<JSONObject> r = new ArrayList<>();
            switch(sgbd){
                case "postgresql":
                    SecurePostgreSql postgresql = new SecurePostgreSql(con);

                    r.add(postgresql.superUsers);
                    r.add(postgresql.usersAccessOtherUsers);
                    r.add(postgresql.securityPolicies);
                    r.add(postgresql.publicObjectsPrivileges);
                    r.add(postgresql.usersEternalPass);
                    r.add(postgresql.usersNoADMCreateDB);
                    r.add(postgresql.auditingEnabled);
                    r.add(postgresql.tablesWithRowSecurity);
                    r.add(postgresql.objectsInPublicSchema);
                    r.add(postgresql.publicObjectsInsDelUp);
                    r.add(postgresql.defaultProceduralLang);
                    r.add(postgresql.nonTrustedProceduralLang);
                    r.add(postgresql.latestVersionBin);
                    r.add(postgresql.noADMStreamOrOffBackup);
                    r.add(postgresql.listenAddressesDefault);
                    r.add(postgresql.perDBUserNames);
                    r.add(postgresql.serverWithDefaultEncription);
                    r.add(postgresql.dbServerGivesRowSecurity);
                    r.add(postgresql.shortTimeoutAut);
                    r.add(postgresql.functionsHighNumbersOfParameters);
                    r.add(postgresql.dbHighNumberOfConnections);
                    r.add(postgresql.dbServerUseSSL);

                    out.print(r);
                    break;
                    
                case "oracle":
                    SecureOracle oracle = new SecureOracle(con);
                                            
                    //Setando resultado para grupo 1:                                      
                    r.add(oracle.invisibleUsers);
                    r.add(oracle.distinctUsers);
                    r.add(oracle.securityRoles);
                    r.add(oracle.nonAdministrativeUsers2);
                    r.add(oracle.nonAdministrativeUsers3);
                    r.add(oracle.nonDefaultPrivilege);
                    r.add(oracle.privilegesConfigured);
                    r.add(oracle.administrativeRoles);
                                                               
                    //Setando resultado para grupo 2:
                    r.add(oracle.auditingIsEnabled);
                    r.add(oracle.manyNonSystemUserSessions);
                    r.add(oracle.nonAdministrativeUsers);
                    r.add(oracle.enablesSystemAuditing);
                    
                    //Setando resultado para grupo 3:
                    r.add(oracle.systemPrivileges);
                    r.add(oracle.externalLibraries);
                    r.add(oracle.defaultDatabasePassword);
                    r.add(oracle.writeFiles);
                    r.add(oracle.deprecatedOptimizer);
                    r.add(oracle.useMaximumMemorySize);
                                                              
                    //Setando resultado para grupo 4;
                    r.add(oracle.failedLogin);
                    r.add(oracle.loginAttempts);
                    r.add(oracle.loginAttempts2);     
                    r.add(oracle.serverVersionInformation);  
                    r.add(oracle.nonCaseSensitivePasswords);  

                    out.print(r);
                    break;
                    
                case "sqlserver":
                    SecureSqlServer sqlserver = new SecureSqlServer(con);

                    r.add(sqlserver.group1);
                    r.add(sqlserver.group2);
                    r.add(sqlserver.group3);
                    r.add(sqlserver.group4);
                    r.add(sqlserver.group5);
                    
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
