/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgbd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import libraries.ConnectionSGBD;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
/**
 *
 * @author Thayson
 */
public class SecureSqlServer {
    
    private ConnectionSGBD driver;

    public SecureSqlServer(ConnectionSGBD driver) {
        this.driver = driver;
    }

    public void auditLevel(){}
    
    public void numverEventLog(){}
    
    public void notificationsEvents(){}
    
    public void loginFailureEvents(){}
    
    public void administratorsGroupSysAdmin(){}
    
    public void membersLocalAdministrators(){}
    
    /* CRIAR UM OBJETO JSON AQUI E RETORNA-LO */
    public ArrayList<String> sysAdminUsers() throws JSONException{
        
        String sql = driver.config.getProperty("SQLServergetSysAdminUsers");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            int i=1;
            ArrayList<String> sysAdminUsers = new ArrayList<>();
        
            while(fields.next()){
               // sysAdminUsers.add(fields.getString(1));
                System.out.println(fields.getString(1));
                i++;
            }    
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void membersRoleOwner(){}

    public void loginsBODeachDatabase(){}

    public void passwordExpirationPolicy(){}
    
    public void checkSimpleDBRemoved(){}
    
    public void checkUserSARenamedOrRemoved(){}
    
    public void checkPermissionsDefaultUserGuest(){}
    
    public void checkAuthenticationModeConnection(){}
    
    public void checkNetworkProtocols(){}
    
    public void checkLoginsNotHaveAssociatedPermissions(){}
    
    public void checkUsersWithoutLogin(){}
    
    public void checkValidBackup(){}
    
}
