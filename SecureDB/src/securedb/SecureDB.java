/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package securedb;

import oracle.SecureOracle;

/**
 *
 * @author Thayson
 */
public class SecureDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        /* Criando um objeto para teste */
        SecureOracle teste = new SecureOracle();
        
        /* verificar parametros de segurança do Oracle */
        teste.pwdDefault();
        teste.dataDictionary();
        teste.loginAttemptsLimit();
        teste.remoteAccess();
        //teste.moveTheAuditTable();
        
    } 
}