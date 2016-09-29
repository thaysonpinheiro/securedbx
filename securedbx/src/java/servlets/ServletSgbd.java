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

                    r.add(postgresql.auditingEnabled);
                    r.add(postgresql.dbHighNumberOfConnections);
                    r.add(postgresql.dbServerGivesRowSecurity);
                    r.add(postgresql.dbServerUseSSL);
                    r.add(postgresql.defaultProceduralLang);
                    r.add(postgresql.functionsHighNumbersOfParameters);
                    r.add(postgresql.latestVersionBin);
                    r.add(postgresql.listenAddressesDefault);
                    r.add(postgresql.noADMStreamOrOffBackup);
                    r.add(postgresql.nonTrustedProceduralLang);
                    r.add(postgresql.objectsInPublicSchema);
                    r.add(postgresql.perDBUserNames);
                    r.add(postgresql.publicObjectsInsDelUp);
                    r.add(postgresql.publicObjectsPrivileges);
                    r.add(postgresql.securityPolicies);
                    r.add(postgresql.serverWithDefaultEncription);
                    r.add(postgresql.shortTimeoutAut);
                    r.add(postgresql.superUsers);
                    r.add(postgresql.tablesWithRowSecurity);
                    r.add(postgresql.usersAccessOtherUsers);
                    r.add(postgresql.usersEternalPass);
                    r.add(postgresql.usersNoADMCreateDB);

                    out.print(r);
                    break;
                    
                case "oracle":
                    SecureOracle oracle = new SecureOracle(con);

                    r.add(oracle.administrativeRoles);
                    r.add(oracle.auditingIsEnabled);
                    r.add(oracle.defaultDatabasePassword);
                    r.add(oracle.deprecatedOptimizer);
                    r.add(oracle.distinctUsers);
                    r.add(oracle.enablesSystemAuditing);
                    r.add(oracle.externalLibraries);
                    r.add(oracle.failedLogin);
                    r.add(oracle.invisibleUsers);
                    r.add(oracle.loginAttempts);
                    r.add(oracle.loginAttempts2);
                    r.add(oracle.manyNonSystemUserSessions);
                    r.add(oracle.nonAdministrativeUsers);
                    r.add(oracle.nonAdministrativeUsers2);
                    r.add(oracle.nonAdministrativeUsers3);
                    r.add(oracle.nonCaseSensitivePasswords);
                    r.add(oracle.nonDefaultPrivilege);
                    r.add(oracle.privilegesConfigured);
                    r.add(oracle.securityRoles);
                    r.add(oracle.serverVersionInformation);
                    r.add(oracle.systemPrivileges);     
                    r.add(oracle.useMaximumMemorySize);  
                    r.add(oracle.writeFiles);  

                    //r.add(s.notificationsAboutEvents);
                    out.print(r);
                    break;
                    
                case "sqlserver":
                    SecureSqlServer sqlserver = new SecureSqlServer(con);

                    r.add(sqlserver.administratorsGroup);
                    r.add(sqlserver.auditLevel);
                    r.add(sqlserver.autenticationmode);
                    r.add(sqlserver.authenticationMode);
                    r.add(sqlserver.certificatesOrSymmetricKeys);
                    r.add(sqlserver.dbOwnerLogins);
                    r.add(sqlserver.dbOwnerUser);
                    r.add(sqlserver.defaultPort);
                    r.add(sqlserver.directUpdInSystemTables);
                    r.add(sqlserver.enabledNetworkProtocols);
                    r.add(sqlserver.encryptedDatabases);
                    r.add(sqlserver.exampleDatabases);
                    r.add(sqlserver.filestreamUsers);
                    r.add(sqlserver.guestUser);
                    r.add(sqlserver.informationViews);
                    r.add(sqlserver.lastPatch);
                    r.add(sqlserver.localAdministratorsGroup);
                    r.add(sqlserver.loginsWithoutPermissions);
                    r.add(sqlserver.loginFailures);
                    r.add(sqlserver.masterKey);
                    r.add(sqlserver.notificationsAboutEvents);
                    r.add(sqlserver.numberOfEventLogs);
                    r.add(sqlserver.passwordExpirationPolicy);
                    r.add(sqlserver.remoteAccessToServer);
                    r.add(sqlserver.remoteAdminAccess);
                    r.add(sqlserver.remoteLoginTimeout);
                    r.add(sqlserver.saUser);
                    r.add(sqlserver.sysAdminUsers);
                    r.add(sqlserver.traceFilesDiagSecIssues);
                    r.add(sqlserver.usersWithoutLogin);
                    r.add(sqlserver.validBackups);
                    
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
