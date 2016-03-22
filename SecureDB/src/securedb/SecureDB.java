/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package securedb;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 * @author Thayson
 */
public class SecureDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        Connection connection = null;
        
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "admin");
            System.out.println("Conexão bem sucedida!");      
        } catch (SQLException e) {
                System.out.println("Falha na conexão!");
        }
    } 
}
