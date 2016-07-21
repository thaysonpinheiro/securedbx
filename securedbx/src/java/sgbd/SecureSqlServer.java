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
public class SecureSqlServer {
    
    private ConnectionSGBD driver;
    public JSONObject sysAdminUsers = new JSONObject();
    public JSONObject dbOwnerUser = new JSONObject();
    public JSONObject saUser = new JSONObject();
    
    public SecureSqlServer(ConnectionSGBD driver) {
        this.driver = driver;
        
        getSysAdminUsers();
        getDBOwnerUser();
        getSAUser();

    }

    public void getSysAdminUsers(){
        
        String sql = "EXEC master.sys.sp_helpsrvrolemember";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            ArrayList<String> r = new ArrayList<>();
            while(fields.next()){
                r.add(fields.getString("MemberName"));
            }
            this.sysAdminUsers.put("MemberName", r);
            
        } catch (SQLException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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

    /* Verificar quais são as permissões que foram concedidas para o usuário padrão "guest" */
    public String getGuestUser(){
        
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
                    " CASE WHEN class_desc = 'DATABASE' THEN db ELSE ObjectName END as ObjectName, \n" +
                    " CASE WHEN DB_ID(db) IN (1, 2, 4) AND permission_name = 'CONNECT' THEN 'Default' \n" +
                    "  ELSE 'Potential Problem!' END as CheckStatus\n" +
                    "FROM #guest_perms\n" +
                    "DROP TABLE #guest_perms";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        
        return "teste";
    } 

    /* Verificar os logins cadastrados que não tem permissões associadas */
    public String getLoginsWithoutPermissions(){
        
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

        return "teste";
    } 
    
    /* Verificar usuários órfãos do banco de dados */
    public String getUsersWithoutLogin(){
        
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

        return "teste";
    } 

    public String getAuditLevel(){
        
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
        
        return "teste";
    } 
    
    public String getNumberOfEventLogs(){
        
        String sql = "EXEC master.dbo.xp_instance_regwrite N'HKEY_LOCAL_MACHINE', \n" +
                    "       N'Software\\Microsoft\\MSSQLServer\\MSSQLServer', \n" +
                    "       N'NumErrorLogs', REG_DWORD, 48";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        
        return "teste";
    } 

    public String getNotificationsAboutEvents(){
        
        String sql = driver.config.getProperty("SQLServergetNotificationsAboutEvents");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        
        return "teste";
    } 

    public String getDBOwnerLogins(){
        
        String sql = "EXEC master.sys.sp_MSforeachdb '\n" +
                    "PRINT ''?''\n" +
                    "EXEC [?].dbo.sp_helpuser ''dbo'''";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        return "teste";
    } 

    public String getAdministratorsGroup(){
        
        String sql = driver.config.getProperty("SQLServergetAdministratorsGroup");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        return "teste";
    } 
    
    public String getLocalAdministratorsGroup(){
        
        String sql = "EXEC master.sys.xp_logininfo 'BUILTIN\\Administrators','members'";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        return "teste";
    }     

    /* Verificar a política de senha de expiração/atualização de senha no SqlServer */
    public String getPasswordExpirationPolicy(){
        
        String sql = "SELECT name  FROM sys.sql_logins \n" +
                    " WHERE  is_policy_checked=0 OR is_expiration_checked = 0";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        return "teste";
    }     

    public String getExampleDatabases(){
        
        String sql = driver.config.getProperty("SQLServergetExampleDatabases");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        return "teste";
    }     

    public String getAuthenticationMode(){
        
        String sql = driver.config.getProperty("SQLServergetAuthenticationMode");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        return "teste";
    }  

    public String getEnabledNetworkProtocols(){
        
        String sql = driver.config.getProperty("SQLServergetEnabledNetworkProtocols");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        return "teste";
    }  

    /* Verificar se existem backups válidos. */
    public String getValidBackups(){
        
        String sql = "EXEC master.sys.sp_validatelogins";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        return "teste";
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

    /* Verificar quais são os protocolos de rede habilitados  */
    public String getLoginFailures(){
        
        String sql = "EXEC master.dbo.xp_instance_regread N'HKEY_LOCAL_MACHINE',\n" +
                    "  N'Software\\Microsoft\\MSSQLServer\\MSSQLServer\\SuperSocketNetLib\\Np', \n" +
                    "  N'Enabled'";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        return "teste";
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