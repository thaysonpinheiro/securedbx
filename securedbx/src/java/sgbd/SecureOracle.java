package sgbd;


import java.sql.DriverManager;
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
    
    protected ConnectionSGBD driver;
    public final Configuration config;
    
    Connection connection = null;
    Statement stat = null;
    ResultSet result;

    public SecureOracle() { 
        this.config = new Configuration(); 
        this.driver = new ConnectionSGBD("localhost", "1521", "xe", "system", "admin", "oracle");
    }
       
    /* verifica os usuários que ainda estão com senha padrão*/
    public void pwdDefault() {
        try {
            String sql = config.getProperty("getUsersPwdDefaultoracle");
            PreparedStatement preparedStatement = driver.prepareStatement(sql);
            if(result.next()){
        	System.out.println("Há usuário com senha padrão!");  
            }
        } catch (SQLException ex) {
            Logger.getLogger(SecureOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /* verifica se o dicionário de dados do sgbd está desativado */
    public void dataDictionary() {     
        try {
            stat = connection.createStatement();
            result = stat.executeQuery("SELECT value FROM v$parameter WHERE name = 'O7_DICTIONARY_ACCESSIBILITY'");  
            if(result.next()) {
        	System.out.println(result.getString(1));  
            }          
        } catch (SQLException ex) {
            Logger.getLogger(SecureOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /* move a tabela de AUD$ para outro local */
    public void moveTheAuditTable() {}
    
    /* verifica se o acesso remoto está livre para qualquer usuário da rede */
    public void remoteAccess() {
        try {
            stat = connection.createStatement();
            result = stat.executeQuery("SELECT value FROM v$parameter WHERE name = 'remote_os_authent'");  
            if(result.next()) {
        	System.out.println(result.getString(1));  
            }          
        } catch (SQLException ex) {
            Logger.getLogger(SecureOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /* verifica se já existe um limite de tentativas de login no sgbd */
    public void loginAttemptsLimit() {
        try {
            stat = connection.createStatement();
            result = stat.executeQuery("SELECT profile, resource_name, limit FROM dba_profiles WHERE profile='DEFAULT'");  
            while(result.next()){
                if(result.getString(2).equalsIgnoreCase("FAILED_LOGIN_ATTEMPTS")) {
                    System.out.println(result.getString(3)); 
                } 
            }          
        } catch (SQLException ex) {
            Logger.getLogger(SecureOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /* algoritmo que auxilia na verificação de senhas comuns utilizadas por usuários relevantes */
    public void bruteForce(){}
    
}
