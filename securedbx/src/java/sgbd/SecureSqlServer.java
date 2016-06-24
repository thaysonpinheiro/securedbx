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
    public int sysAdminUsers(){
        String sql = driver.config.getProperty("SQLServergetSysAdminUsers");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            while(fields.next()){
                System.out.println(fields.getString(1));
                return 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
       return 0;
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
