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
import org.json.JSONObject;
import java.util.ArrayList;
import org.json.JSONException;


/**
 *
 * @author Thayson
 */
public final class SecureSqlServer {
    
    private ConnectionSGBD driver;
    public JSONObject sysAdminUsers = new JSONObject();
    public JSONObject dbOwnerUser = new JSONObject();
    public JSONObject saUser = new JSONObject();
    public JSONObject guestUser = new JSONObject();
    public JSONObject usersWithoutLogin = new JSONObject();
    public JSONObject auditLevel = new JSONObject();
    public JSONObject loginsWithoutPermissions = new JSONObject();
    public JSONObject administratorsGroup = new JSONObject();
    public JSONObject localAdministratorsGroup = new JSONObject();
    public JSONObject numberOfEventLogs = new JSONObject();
    public JSONObject passwordExpirationPolicy = new JSONObject();
    public JSONObject exampleDatabases = new JSONObject();
    public JSONObject authenticationMode = new JSONObject();
    public JSONObject validBackups = new JSONObject();
    public JSONObject loginFailures = new JSONObject();
    public JSONObject dbOwnerLogins = new JSONObject();
    public JSONObject enabledNetworkProtocols = new JSONObject();
    public JSONObject notificationsAboutEvents = new JSONObject();
    
    public SecureSqlServer(ConnectionSGBD driver) {
        this.driver = driver;
        
        getSysAdminUsers();
        getDBOwnerUser();
        getSAUser();
        getGuestUser();
        getLoginsWithoutPermissions();
        getUsersWithoutLogin();
        getAuditLevel();
        getAdministratorsGroup();
        getLocalAdministratorsGroup();
        getNumberOfEventLogs();
        getPasswordExpirationPolicy();
        getExampleDatabases();
        getAuthenticationMode();
        getValidBackups();
        getLoginFailures();
        getDBOwnerLogins();
        getEnabledNetworkProtocols();
        getNotificationsAboutEvents();
    }

    //FINALIZADO
    public void getSysAdminUsers(){
        
        String sql = "EXEC master.sys.sp_helpsrvrolemember";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString("MemberName"));
            }
            this.sysAdminUsers.put("sysAdminUsers", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // PROBLEMA. NO RETORNO DA CONSULTA. RETORNA MAIS DE UMA TABELA
    /*Verificar em cada banco de dados os membros associados a role db_owner*/
    public void getDBOwnerUser(){
        
        String sql = "EXEC master.sys.sp_MSforeachdb '\n" +
                    "PRINT ''?''\n" +
                    "EXEC [?].dbo.sp_helpuser ''dbo'''";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        /*
        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString("LoginName"));
            }
            this.dbOwnerUser.put("LoginName", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    //FINALIZADO
    public void getSAUser(){
        
        String sql = "SELECT l.name, CASE WHEN l.name = 'sa' THEN 'NO' ELSE 'YES' END as Renamed,\n" +
                    "  s.is_policy_checked, s.is_expiration_checked, l.is_disabled\n" +
                    "FROM sys.server_principals AS l\n" +
                    " LEFT OUTER JOIN sys.sql_logins AS s ON s.principal_id = l.principal_id\n" +
                    "WHERE l.sid = 0x01";
        
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
       
        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString(1));
                r.add(fields.getString(2));
                r.add(fields.getString(3));
                r.add(fields.getString(4));
                r.add(fields.getString(5));
            }
            this.saUser.put("saUser", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    //FINALIZADO
    /* Verificar quais são as permissões que foram concedidas para o usuário padrão "guest" */
    public void getGuestUser(){
        
        String sql = "SET NOCOUNT ON\n" +
                    "CREATE TABLE #guest_perms \n" +
                    " ( db SYSNAME, class_desc SYSNAME, \n" +
                    "  permission_name SYSNAME, ObjectName SYSNAME NULL)\n" +
                    "EXEC master.sys.sp_MSforeachdb\n" +
                    "'INSERT INTO #guest_perms\n" +
                    " SELECT ''?'' as DBName, p.class_desc, p.permission_name, \n" +
                    "   OBJECT_NAME (major_id, DB_ID(''?'')) as ObjectName\n" +
                    " FROM [?].sys.database_permissions p JOIN [?].sys.database_principals l\n" +
                    "  ON p.grantee_principal_id= l.principal_id \n" +
                    " WHERE l.name = ''guest'' AND p.[state] = ''G'''\n" +
                    " \n" +
                    "SELECT db AS DatabaseName, class_desc, permission_name, \n" +
                    "  CASE WHEN class_desc = 'DATABASE' THEN db ELSE ObjectName END as ObjectName, \n" +
                    "  CASE WHEN DB_ID(db) IN (1, 2, 4) AND permission_name = 'CONNECT' THEN 'Default' \n" +
                    "  ELSE 'Potential Problem!' END as CheckStatus\n" +
                    "FROM #guest_perms\n" +
                    "DROP TABLE #guest_perms";
        
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString(1));
                r.add(fields.getString(2));
                r.add(fields.getString(3));
                r.add(fields.getString(4));
                r.add(fields.getString(5));
            }
            this.guestUser.put("guestUser", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

    //FINALIZADO
    /* Verificar os logins cadastrados que não tem permissões associadas */
    public void getLoginsWithoutPermissions(){
        
        String sql = "SET NOCOUNT ON\n" +
                    "CREATE TABLE #all_users (db VARCHAR(70), sid VARBINARY(85), stat VARCHAR(50))\n" +
                    "EXEC master.sys.sp_msforeachdb\n" +
                    "'INSERT INTO #all_users  \n" +
                    " SELECT ''?'', CONVERT(varbinary(85), sid) , \n" +
                    "  CASE WHEN  r.role_principal_id IS NULL AND p.major_id IS NULL \n" +
                    "  THEN ''no_db_permissions''  ELSE ''db_user'' END\n" +
                    " FROM [?].sys.database_principals u LEFT JOIN [?].sys.database_permissions p \n" +
                    "   ON u.principal_id = p.grantee_principal_id  \n" +
                    "   AND p.permission_name <> ''CONNECT''\n" +
                    "  LEFT JOIN [?].sys.database_role_members r \n" +
                    "   ON u.principal_id = r.member_principal_id\n" +
                    "  WHERE u.SID IS NOT NULL AND u.type_desc <> ''DATABASE_ROLE'''\n" +
                    "IF EXISTS \n" +
                    "(SELECT l.name FROM sys.server_principals l LEFT JOIN sys.server_permissions p \n" +
                    "  ON l.principal_id = p.grantee_principal_id  \n" +
                    "  AND p.permission_name <> 'CONNECT SQL'\n" +
                    " LEFT JOIN sys.server_role_members r \n" +
                    "  ON l.principal_id = r.member_principal_id\n" +
                    " LEFT JOIN #all_users u \n" +
                    "  ON l.sid= u.sid\n" +
                    " WHERE r.role_principal_id IS NULL  AND l.type_desc <> 'SERVER_ROLE' \n" +
                    "  AND p.major_id IS NULL\n" +
                    " )\n" +
                    "BEGIN\n" +
                    " SELECT DISTINCT l.name LoginName, l.type_desc, l.is_disabled, \n" +
                    "  ISNULL(u.stat + ', but is user in ' + u.db  +' DB', 'no_db_users') db_perms, \n" +
                    "  CASE WHEN p.major_id IS NULL AND r.role_principal_id IS NULL  \n" +
                    "  THEN 'no_srv_permissions' ELSE 'na' END srv_perms \n" +
                    " FROM sys.server_principals l LEFT JOIN sys.server_permissions p \n" +
                    "   ON l.principal_id = p.grantee_principal_id  \n" +
                    "   AND p.permission_name <> 'CONNECT SQL'\n" +
                    "  LEFT JOIN sys.server_role_members r \n" +
                    "   ON l.principal_id = r.member_principal_id\n" +
                    "   LEFT JOIN #all_users u \n" +
                    "   ON l.sid= u.sid\n" +
                    "  WHERE  l.type_desc <> 'SERVER_ROLE' \n" +
                    "   AND ((u.db  IS NULL  AND p.major_id IS NULL \n" +
                    "     AND r.role_principal_id IS NULL )\n" +
                    "   OR (u.stat = 'no_db_permissions' AND p.major_id IS NULL \n" +
                    "     AND r.role_principal_id IS NULL)) \n" +
                    " ORDER BY 1, 4\n" +
                    "END\n" +
                    "DROP TABLE #all_users";
        
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString(1));
                r.add(fields.getString(2));
                r.add(fields.getString(3));
                r.add(fields.getString(4));
                r.add(fields.getString(5));
            }
            this.loginsWithoutPermissions.put("loginsWithoutPermissions", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    //FINALIZADO
    /* Verificar usuários órfãos do banco de dados */
    public void getUsersWithoutLogin(){
        
        String sql = "SET NOCOUNT ON\n" +
                    "CREATE TABLE #orph_users (db SYSNAME, username SYSNAME, \n" +
                    "    type_desc VARCHAR(30),type VARCHAR(30))\n" +
                    "EXEC master.sys.sp_msforeachdb  \n" +
                    "'INSERT INTO #orph_users\n" +
                    " SELECT ''?'', u.name , u.type_desc, u.type\n" +
                    " FROM  [?].sys.database_principals u \n" +
                    "  LEFT JOIN  [?].sys.server_principals l ON u.sid = l.sid \n" +
                    " WHERE l.sid IS NULL \n" +
                    "  AND u.type NOT IN (''A'', ''R'', ''C'') -- not a db./app. role or certificate\n" +
                    "  AND u.principal_id > 4 -- not dbo, guest or INFORMATION_SCHEMA\n" +
                    "  AND u.name NOT LIKE ''%DataCollector%'' \n" +
                    "  AND u.name NOT LIKE ''mdw%'' -- not internal users in msdb or MDW databases'\n" +
                    "    \n" +
                    " SELECT * FROM #orph_users\n" +
                    " \n" +
                    " DROP TABLE #orph_users";
        
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString(1));
                r.add(fields.getString(2));
                r.add(fields.getString(3));
                r.add(fields.getString(4));
                r.add(fields.getString(5));
            }
            this.usersWithoutLogin.put("usersWithoutLogin", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

    //FINALIZADO
    public void getAuditLevel(){
        
        String sql = "DECLARE @AuditLevel int\n" +
                    "EXEC master.dbo.xp_instance_regread N'HKEY_LOCAL_MACHINE', \n" +
                    "   N'Software\\Microsoft\\MSSQLServer\\MSSQLServer', \n" +
                    "   N'AuditLevel', @AuditLevel OUTPUT\n" +
                    "SELECT CASE WHEN @AuditLevel = 0 THEN 'None'\n" +
                    "   WHEN @AuditLevel = 1 THEN 'Successful logins only'\n" +
                    "   WHEN @AuditLevel = 2 THEN 'Failed logins only'\n" +
                    "   WHEN @AuditLevel = 3 THEN 'Both failed and successful logins' \n" +
                    "   END AS [AuditLevel] ";
        
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString(1));
            }
            this.auditLevel.put("auditLevel", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    // PROBLEMA. ESSA CONSULTA NAO RETORNA NADA
    public void getNumberOfEventLogs(){
        
        String sql = "EXEC master.dbo.xp_instance_regwrite N'HKEY_LOCAL_MACHINE', \n" +
                    "       N'Software\\Microsoft\\MSSQLServer\\MSSQLServer', \n" +
                    "       N'NumErrorLogs', REG_DWORD, 48";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        /*
        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString(1));
            }
            this.numberOfEventLogs.put("numberOfEventLogs", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    } 

    //PROBLEMA
    public void getNotificationsAboutEvents(){
        
        String sql = "EXEC msdb.dbo.sp_add_operator @name=N'NotifyDBA_Group', \n" +
                    "  @enabled=1, \n" +
                    "  @email_address=N'NotifyDBAs@company.com'\n" +
                    "\n" +
                    "EXEC msdb.dbo.sp_add_alert @name = N'Sev. 14 Errors - Permissions', \n" +
                    "  @severity = 14, \n" +
                    "  @include_event_description_in = 1\n" +
                    "\n" +
                    "EXEC msdb.dbo.sp_add_notification @alert_name = N'Sev. 14 Errors - Permissions', \n" +
                    "@operator_name = N'NotifyDBA_Group', @notification_method = 1";
        //PreparedStatement preparedStatement = driver.prepareStatement(sql);
        //ResultSet fields = driver.executeQuery(preparedStatement);
    } 

    // PROBLEMA. NO RETORNO DA CONSULTA. RETORNA MAIS DE UMA TABELA
    public void getDBOwnerLogins(){
        
        String sql = "EXEC master.sys.sp_MSforeachdb '\n" +
                    "PRINT ''?''\n" +
                    "EXEC [?].dbo.sp_helprolemember ''db_owner'''";
        
        /*
        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString(1));
                r.add(fields.getString(2));
                r.add(fields.getString(3));
            }
            this.dbOwnerLogins.put("dbOwnerLogins", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    } 

    //FINALIZADO
    public void getAdministratorsGroup(){
        
        String sql = "SELECT r.name  as SrvRole, u.name  as LoginName  \n" +
                    "FROM sys.server_role_members m JOIN\n" +
                    "  sys.server_principals r ON m.role_principal_id = r.principal_id  JOIN\n" +
                    "  sys.server_principals u ON m.member_principal_id = u.principal_id \n" +
                    "WHERE u.name = 'BUILTIN\\Administrators'";
        
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString(1));
                r.add(fields.getString(2));
            }
            this.administratorsGroup.put("administratorsGroup", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    //FINALIZADO
    public void getLocalAdministratorsGroup(){
        
        String sql = "EXEC master.sys.xp_logininfo 'BUILTIN\\Administrators','members'";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString(1));
                r.add(fields.getString(2));
                r.add(fields.getString(3));
                r.add(fields.getString(4));
                r.add(fields.getString(5));
            }
            this.localAdministratorsGroup.put("localAdministratorsGroup", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }     

    //FINALIZADO
    /* Verificar a política de senha de expiração/atualização de senha no SqlServer */
    public void getPasswordExpirationPolicy(){
        
        String sql = "SELECT name  FROM sys.sql_logins \n" +
                    " WHERE  is_policy_checked=0 OR is_expiration_checked = 0";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString(1));
            }
            this.passwordExpirationPolicy.put("passwordExpirationPolicy", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }     

    //FINALIZADO
    public void getExampleDatabases(){
        
        String sql = "SELECT name FROM master.sys.databases \n" +
                    " WHERE name IN ('pubs', 'Northwind') OR name LIKE 'Adventure Works%'";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString(1));
            }
            this.exampleDatabases.put("exampleDatabases", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }     

    //PROBLEMA. RETORNO DESSA CONSULTA É ESTRANHO
    public void getAuthenticationMode(){
        
        String sql = "SELECT SERVERPROPERTY ('IsIntegratedSecurityOnly') as 'result'";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            ArrayList<Integer> r = new ArrayList<>();
            if(fields != null){
                while(fields.next()){
                   r.add(fields.getInt(1));
                }
            }
            this.authenticationMode.put("authenticationMode", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  

    // PROBLEMA AO EXECUTAR ESSA CONSULTA
    public void getEnabledNetworkProtocols(){
        
        String sql = "EXEC master.dbo.xp_instance_regread N'HKEY_LOCAL_MACHINE',\n" +
                "  N'Software\\Microsoft\\MSSQLServer\\MSSQLServer\\SuperSocketNetLib\\Np', \n" +
                "  N'Enabled'\n" +
                "\n" +
                "--------------\n" +
                "EXEC master.dbo.xp_instance_regread N'HKEY_LOCAL_MACHINE',\n" +
                "  N'Software\\Microsoft\\MSSQLServer\\MSSQLServer\\SuperSocketNetLib\\Np', \n" +
                "  N'Enabled', \n" +
                "  @NamedPipesEnabled OUTPUT\n" +
                "  \n" +
                "SELECT @NamedPipesEnabled AS NamedPipesEnabled";
         /*  
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
       
        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                
            }
            this.enabledNetworkProtocols.put("enabledNetworkProtocols", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }  

    // PROBLEMA AO EXECUTAR ESSA CONSULTA
    /* Verificar se existem backups válidos. */
    public void getValidBackups(){
       
        String sql = "EXEC master.sys.sp_validatelogins";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
 /*
        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString(1));
                r.add(fields.getString(2));
            }
            this.validBackups.put("validBackups", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }  

    public String getCurrentSecurityPatches(){
        
        String sql = driver.config.getProperty("SQLServergetCurrentSecurityPatches");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        return "teste";
    }  

    public String getNumberOfViews(){
        
        String sql = driver.config.getProperty("SQLServergetNumberOfViews");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        return "teste";
    }  

    // PROBLEMA AO EXECUTAR ESSA CONSULTA
    /* Verificar quais são os protocolos de rede habilitados  */
    public void getLoginFailures(){
        
        String sql = "EXEC master.dbo.xp_instance_regread N'HKEY_LOCAL_MACHINE',\n" +
                    "  N'Software\\Microsoft\\MSSQLServer\\MSSQLServer\\SuperSocketNetLib\\Np', \n" +
                    "  N'Enabled'";
      /*  PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString(1));
                r.add((fields.getDate(2)).toString());
            }
            this.loginFailures.put("loginFailures", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    } 

    /* AQUI PODEMOS VERIFICAR COM A INFORMAÇÃO DADA PELO USUÁRIO NO FORM*/
    public String getDefaultPort(){
        
        String sql = driver.config.getProperty("SQLServergetDefaultPort");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        return "teste";
    } 

    /* Encryption, Tokenization and Data Masking
    public String getEncryption(){
        
        String sql = driver.config.getProperty("SQLServergetEncryption");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        return "teste";
    } 
    */

}