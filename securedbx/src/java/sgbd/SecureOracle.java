package sgbd;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import libraries.Configuration;
import libraries.ConnectionSGBD;


/**
 *
 * @author Thayson
 */
public class SecureOracle {
    
    private ConnectionSGBD driver;
    private Configuration config; 
    
    Statement stat = null;
    ResultSet result = null;

    public SecureOracle(ConnectionSGBD driver){ 
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
