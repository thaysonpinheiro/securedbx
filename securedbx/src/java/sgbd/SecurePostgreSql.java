/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgbd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import libraries.Configuration;
import libraries.ConnectionSGBD;

/**
 *
 * @author Thayson
 */
public class SecurePostgreSql {
    
    private ConnectionSGBD driver; 

    public SecurePostgreSql(ConnectionSGBD driver) {
        this.driver = driver;
    }
    
        /* verifica os usuários que ainda estão com senha padrão*/
    public String pwdDefault() {
        
        String sql = driver.config.getProperty("getUsersPwdDefaultoracle");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        return sql;
    }  
}
