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
import java.util.Calendar;
import java.util.Date;
import org.json.JSONException;

/**
 *
 * @author Karlos Ítalo
 */
public final class SecureSqlServer {

    private ConnectionSGBD driver;
    
    public JSONObject group1 = new JSONObject();
    public JSONObject group2 = new JSONObject();
    public JSONObject group3 = new JSONObject();
    public JSONObject group4 = new JSONObject();
    public JSONObject group5 = new JSONObject();
    

    public SecureSqlServer(ConnectionSGBD driver) {
        this.driver = driver;
        
        //Start: Group1 methods
        this.getSysAdminUsers();
        this.getDBOwnerUser();
        this.getSAUser();
        this.getGuestUser();
        this.getLoginsWithoutPermissions();
        this.getUsersWithoutLogin();
        //End: Group2 methods
        
        //Start: Group2 methods
        this.getAuditLevel();
        this.getDbOwnerLogins();
        this.getTraceFilesDiagSecIssues();
        this.getInformationViews();
        //End: Group2 methods
        
        //Start: Group3 methods
        this.getAdministratorsGroup();
        this.getLocalAdministratorsGroup();
        this.getPasswordExpirationPolicy();
        this.getExampleDatabases();
        this.getAuthenticationmode();
        this.getEnabledNetworkProtocols();
        this.getValidBackups();
        this.getShellFileEnable();
        this.getFilestreamUsers();
        this.getLastPatch();
        //End: Group3 methods
        
        //Start: Group4 methods
        this.getLoginFailures();
        this.getDefaultPort();
        this.getDirectUpdInSystemTables();
        this.getRemoteAccessToServer();
        this.getRemoteAdminAccess();
        this.getRemoteLoginTimeout();
        this.getMasterKey();
        //End: Group4 methods
        
        //Start: Group5 methods
        this.getCertificatesOrSymmetricKeys();
        this.getEncryptedDatabases();
        //End: Group5 methods
        
    }

    //FINALIZADO
    //06
    public void getSysAdminUsers() {

        String sql = "EXEC master.sys.sp_helpsrvrolemember";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            //Seta o valor inicial como true e se algum elemento nÃ£o comeÃ§ar como sa nem com NT seta o valor para false
            this.group1.put("sysAdminUsers", "true");
            while (fields.next()) {
                //r.add(fields.getString("MemberName"));
                if (!fields.getString("MemberName").startsWith("sa") && !fields.getString("MemberName").startsWith("NT")) {
                    this.group1.put("sysAdminUsers", "false");
                }
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getDBOwnerUser() {

        String sql = "select r.name as role_name, m.name as member_name from sys.database_role_members rm \n"
                + "inner join sys.database_principals r on rm.role_principal_id = r.principal_id\n"
                + "inner join sys.database_principals m on rm.member_principal_id = m.principal_id\n"
                + "where r.name = 'db_owner' and m.name != 'dbo'  ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.group1.put("dbOwnerUser", "true");
            } else {
                this.group1.put("dbOwnerUser", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //FINALIZADO
    //11
    public void getSAUser() {

        String sql = "SELECT l.name, CASE WHEN l.name = 'sa' THEN 'NO' ELSE 'YES' END as Renamed,\n"
                + "  s.is_policy_checked, s.is_expiration_checked, l.is_disabled\n"
                + "FROM sys.server_principals AS l\n"
                + " LEFT OUTER JOIN sys.sql_logins AS s ON s.principal_id = l.principal_id\n"
                + "WHERE l.sid = 0x01";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            this.group1.put("saUser", "false");

            while (fields.next()) {
                if (fields.getString("Renamed").equals("NO")
                        && fields.getString("is_policy_checked").equals("1")
                        && fields.getString("is_expiration_checked").equals("1")
                        && fields.getString("is_disabled").equals("1")) {
                    this.group1.put("saUser", "true");
                }
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //FINALIZADO
    /* Verificar quais sÃ£o as permissÃµes que foram concedidas para o usuÃ¡rio padrÃ£o "guest" */
    //12
    public void getGuestUser() {

        String sql = "SET NOCOUNT ON\n"
                + "CREATE TABLE #guest_perms \n"
                + " ( db SYSNAME, class_desc SYSNAME, \n"
                + "  permission_name SYSNAME, ObjectName SYSNAME NULL)\n"
                + "EXEC master.sys.sp_MSforeachdb\n"
                + "'INSERT INTO #guest_perms\n"
                + " SELECT ''?'' as DBName, p.class_desc, p.permission_name, \n"
                + "   OBJECT_NAME (major_id, DB_ID(''?'')) as ObjectName\n"
                + " FROM [?].sys.database_permissions p JOIN [?].sys.database_principals l\n"
                + "  ON p.grantee_principal_id= l.principal_id \n"
                + " WHERE l.name = ''guest'' AND p.[state] = ''G'''\n"
                + " \n"
                + "SELECT db AS DatabaseName, class_desc, permission_name, \n"
                + "  CASE WHEN class_desc = 'DATABASE' THEN db ELSE ObjectName END as ObjectName, \n"
                + "  CASE WHEN DB_ID(db) IN (1, 2, 4) AND permission_name = 'CONNECT' THEN 'Default' \n"
                + "  ELSE 'Potential Problem!' END as CheckStatus\n"
                + "FROM #guest_perms\n"
                + "DROP TABLE #guest_perms";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.group1.put("guestUser", "true");
            } else {
                this.group1.put("guestUser", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //FINALIZADO
    /* Verificar os logins cadastrados que nÃ£o tem permissÃµes associadas */
    //15
    public void getLoginsWithoutPermissions() {

        String sql = "SET NOCOUNT ON\n"
                + "CREATE TABLE #all_users (db VARCHAR(70), sid VARBINARY(85), stat VARCHAR(50))\n"
                + "EXEC master.sys.sp_msforeachdb\n"
                + "'INSERT INTO #all_users  \n"
                + " SELECT ''?'', CONVERT(varbinary(85), sid) , \n"
                + "  CASE WHEN  r.role_principal_id IS NULL AND p.major_id IS NULL \n"
                + "  THEN ''no_db_permissions''  ELSE ''db_user'' END\n"
                + " FROM [?].sys.database_principals u LEFT JOIN [?].sys.database_permissions p \n"
                + "   ON u.principal_id = p.grantee_principal_id  \n"
                + "   AND p.permission_name <> ''CONNECT''\n"
                + "  LEFT JOIN [?].sys.database_role_members r \n"
                + "   ON u.principal_id = r.member_principal_id\n"
                + "  WHERE u.SID IS NOT NULL AND u.type_desc <> ''DATABASE_ROLE'''\n"
                + "IF EXISTS \n"
                + "(SELECT l.name FROM sys.server_principals l LEFT JOIN sys.server_permissions p \n"
                + "  ON l.principal_id = p.grantee_principal_id  \n"
                + "  AND p.permission_name <> 'CONNECT SQL'\n"
                + " LEFT JOIN sys.server_role_members r \n"
                + "  ON l.principal_id = r.member_principal_id\n"
                + " LEFT JOIN #all_users u \n"
                + "  ON l.sid= u.sid\n"
                + " WHERE r.role_principal_id IS NULL  AND l.type_desc <> 'SERVER_ROLE' \n"
                + "  AND p.major_id IS NULL\n"
                + " )\n"
                + "BEGIN\n"
                + " SELECT DISTINCT l.name LoginName, l.type_desc, l.is_disabled, \n"
                + "  ISNULL(u.stat + ', but is user in ' + u.db  +' DB', 'no_db_users') db_perms, \n"
                + "  CASE WHEN p.major_id IS NULL AND r.role_principal_id IS NULL  \n"
                + "  THEN 'no_srv_permissions' ELSE 'na' END srv_perms \n"
                + " FROM sys.server_principals l LEFT JOIN sys.server_permissions p \n"
                + "   ON l.principal_id = p.grantee_principal_id  \n"
                + "   AND p.permission_name <> 'CONNECT SQL'\n"
                + "  LEFT JOIN sys.server_role_members r \n"
                + "   ON l.principal_id = r.member_principal_id\n"
                + "   LEFT JOIN #all_users u \n"
                + "   ON l.sid= u.sid\n"
                + "  WHERE  l.type_desc <> 'SERVER_ROLE' \n"
                + "   AND ((u.db  IS NULL  AND p.major_id IS NULL \n"
                + "     AND r.role_principal_id IS NULL )\n"
                + "   OR (u.stat = 'no_db_permissions' AND p.major_id IS NULL \n"
                + "     AND r.role_principal_id IS NULL)) \n"
                + " ORDER BY 1, 4\n"
                + "END\n"
                + "DROP TABLE #all_users";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.group1.put("loginsWithoutPermissions", "true");
            } else {
                this.group1.put("loginsWithoutPermissions", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //FINALIZADO
    /* Verificar usuário orfãos do banco de dados */
    //16
    public void getUsersWithoutLogin() {

        String sql = "SET NOCOUNT ON\n"
                + "CREATE TABLE #orph_users (db SYSNAME, username SYSNAME, \n"
                + "    type_desc VARCHAR(30),type VARCHAR(30))\n"
                + "EXEC master.sys.sp_msforeachdb  \n"
                + "'INSERT INTO #orph_users\n"
                + " SELECT ''?'', u.name , u.type_desc, u.type\n"
                + " FROM  [?].sys.database_principals u \n"
                + "  LEFT JOIN  [?].sys.server_principals l ON u.sid = l.sid \n"
                + " WHERE l.sid IS NULL \n"
                + "  AND u.type NOT IN (''A'', ''R'', ''C'') -- not a db./app. role or certificate\n"
                + "  AND u.principal_id > 4 -- not dbo, guest or INFORMATION_SCHEMA\n"
                + "  AND u.name NOT LIKE ''%DataCollector%'' \n"
                + "  AND u.name NOT LIKE ''mdw%'' -- not internal users in msdb or MDW databases'\n"
                + "    \n"
                + " SELECT * FROM #orph_users\n"
                + " \n"
                + " DROP TABLE #orph_users";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.group1.put("usersWithoutLogin", "true");
            } else {
                this.group1.put("usersWithoutLogin", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //*
    public void getAuditLevel() {

        String sql = "DECLARE @AuditLevel int\n"
                + "EXEC master.dbo.xp_instance_regread N'HKEY_LOCAL_MACHINE', \n"
                + "   N'Software\\Microsoft\\MSSQLServer\\MSSQLServer', \n"
                + "   N'AuditLevel', @AuditLevel OUTPUT\n"
                + "SELECT CASE WHEN @AuditLevel = 0 THEN 'None'\n"
                + "   WHEN @AuditLevel = 1 THEN 'Successful logins only'\n"
                + "   WHEN @AuditLevel = 2 THEN 'Failed logins only'\n"
                + "   WHEN @AuditLevel = 3 THEN 'Both failed and successful logins' \n"
                + "   END AS [AuditLevel] ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            fields.next();

            if (fields.getString("AuditLevel").equals("None")) {
                this.group2.put("auditLevel", "false");
            }

            if (fields.getString("AuditLevel").equals("Successful logins only") || fields.getString("AuditLevel").equals("Failed logins only")) {
                this.group2.put("auditLevel", "warning");
            }

            if (fields.getString("AuditLevel").equals("Both failed and successful logins")) {
                this.group2.put("auditLevel", "true");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getDbOwnerLogins() {
        
        String sql = "EXEC master.sys.sp_MSforeachdb '\n" +
                     "PRINT ''?''\n" +
                     "EXEC [?].dbo.sp_helpuser ''dbo'''";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        //ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            
            boolean hasResults = preparedStatement.execute();
            this.group2.put("dbOwnerLogins", "true");
            
            while (hasResults) {
                ResultSet rs = preparedStatement.getResultSet();
                if(rs.next() && (!rs.getString(3).equals("sa") && !(rs.getString(3) != null) ))
                    this.group2.put("dbOwners", "true");
                hasResults = preparedStatement.getMoreResults();
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //FINALIZADO
    //04
    public void getAdministratorsGroup() {

        String sql = "SELECT r.name  as SrvRole, u.name  as LoginName  \n"
                + "FROM sys.server_role_members m JOIN\n"
                + "  sys.server_principals r ON m.role_principal_id = r.principal_id  JOIN\n"
                + "  sys.server_principals u ON m.member_principal_id = u.principal_id \n"
                + "WHERE u.name = 'BUILTIN\\Administrators'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.group3.put("administratorsGroup", "true");
            } else {
                this.group3.put("administratorsGroup", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //FINALIZADO
    //05
    public void getLocalAdministratorsGroup() {

        String sql = "EXEC master.sys.xp_logininfo 'BUILTIN\\Administrators','members'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.group3.put("localAdministratorsGroup", "true");
            } else {
                this.group3.put("localAdministratorsGroup", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Finalizada
    //09
    public void getPasswordExpirationPolicy() {

        String sql = "SELECT name FROM [sys].[sql_logins] WHERE [is_policy_checked] = 0 OR ([is_policy_checked] = 1 AND [is_expiration_checked] = 0)";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        System.out.println(preparedStatement);
        System.out.println(fields);
        try {
            ArrayList<String> r = new ArrayList<>();

            this.group3.put("passwordExpirationPolicy", "true");

            while (fields.next()) {
                String name = fields.getString("name");

                if (!name.equals("sa")
                        && !name.equals("##MS_PolicyEventProcessingLogin##")
                        && !name.equals("##MS_PolicyTsqlExecutionLogin##")
                        && !name.equals("zm.nestle")) {

                    this.group3.put("passwordExpirationPolicy", "false");

                }
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //FINALIZADO
    //10
    public void getExampleDatabases() {

        String sql = "SELECT name FROM master.sys.databases \n"
                + " WHERE name IN ('pubs', 'Northwind') OR name LIKE 'Adventure Works%'";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.group3.put("exampleDatabases", "true");
            } else {
                this.group3.put("exampleDatabases", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //FINALIZADO
    //18
    public void getShellFileEnable() {

        String sql = "SELECT cast (SERVERPROPERTY ('IsIntegratedSecurityOnly') AS VARCHAR(20)) as 'result'";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            this.group3.put("shellFileEnable", "true");
            System.out.println(preparedStatement);
            System.out.println(fields);
            System.out.println(driver);
            while (fields.next()) {
                if (!fields.getString(1).equals("0")) {
                    this.group3.put("shellFileEnable", "false");
                }
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Finalizado obs: checa named pipes
    //14
    public void getEnabledNetworkProtocols() {

        String sql = "EXEC master.dbo.xp_instance_regread N'HKEY_LOCAL_MACHINE',\n"
                + "N'Software\\Microsoft\\MSSQLServer\\MSSQLServer\\SuperSocketNetLib\\Np', \n"
                + "N'Enabled'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            this.group3.put("enabledNetworkProtocols", "true");

            while (fields.next()) {
                if (fields.getString("Value").equals("Enabled")) {
                    this.group3.put("enabledNetworkProtocols", "false");
                }
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Finalizado
    //17
    public void getValidBackups() {

        String sql = "SELECT \n"
                + "CONVERT(CHAR(100), SERVERPROPERTY('Servername')) AS Server, \n"
                + "msdb.dbo.backupset.database_name, \n"
                + "msdb.dbo.backupset.backup_start_date, \n"
                + "msdb.dbo.backupset.backup_finish_date, \n"
                + "msdb.dbo.backupset.expiration_date, \n"
                + "CASE msdb..backupset.type \n"
                + "WHEN 'D' THEN 'Database' \n"
                + "WHEN 'L' THEN 'Log' \n"
                + "END AS backup_type, \n"
                + "msdb.dbo.backupset.backup_size, \n"
                + "msdb.dbo.backupmediafamily.logical_device_name, \n"
                + "msdb.dbo.backupmediafamily.physical_device_name, \n"
                + "msdb.dbo.backupset.name AS backupset_name, \n"
                + "msdb.dbo.backupset.description \n"
                + "FROM msdb.dbo.backupmediafamily \n"
                + "INNER JOIN msdb.dbo.backupset ON msdb.dbo.backupmediafamily.media_set_id = msdb.dbo.backupset.media_set_id \n"
                + "WHERE (CONVERT(datetime, msdb.dbo.backupset.backup_start_date, 102) >= GETDATE() - 7) \n"
                + "ORDER BY \n"
                + "msdb.dbo.backupset.database_name, \n"
                + "msdb.dbo.backupset.backup_finish_date ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            this.group3.put("validBackups", "true");

            if (!fields.next()) {

                this.group3.put("validBackups", "false");

            } else {

                fields.next();
                //data atual e data do ultimo backup
                Date actualDate = new Date();
                Date lastBackupDate = fields.getDate("backup_finish_date");

                //uma semana antes da data atual
                Calendar c = Calendar.getInstance();
                c.setTime(actualDate);
                c.add(Calendar.DATE, -7);
                actualDate = c.getTime();

                if (lastBackupDate.before(actualDate)) {
                    this.group3.put("validBackups", "false");
                }

            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*    public String getCurrentSecurityPatches() {

        String sql = driver.config.getProperty("SQLServergetCurrentSecurityPatches");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        return "teste";
    }*/

 /* public String getNumberOfViews() {

        String sql = driver.config.getProperty("SQLServergetNumberOfViews");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        return "teste";
    }*/
    // PROBLEMA AO EXECUTAR ESSA CONSULTA
    /* Verificar quais sÃ£o os protocolos de rede habilitados  
    03*/
    public void getLoginFailures() {

        String sql = "execute master..sp_ReadErrorLog 0,1,'Failed','Login'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
         
        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next() && fields != null) {
                this.group4.put("loginFailures", "true");
            } else {
                this.group4.put("loginFailures", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //*
    public void getDefaultPort() {

        String sql = "SELECT TOP 1 local_tcp_port as 'defaultPort' \n"
                + "FROM sys.dm_exec_connections\n"
                + "WHERE local_tcp_port IS NOT NULL";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            this.group4.put("defaultPort", "true");

            while (fields.next()) {
                if (fields.getString("defaultPort").equals("1433")) {
                    System.out.println(fields.getString("defaultPort"));
                    this.group4.put("defaultPort", "false");
                }
            }
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* Encryption, Tokenization and Data Masking
    public String getEncryption(){
        
        String sql = driver.config.getProperty("SQLServergetEncryption");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        return "teste";
    } 
     */
    /**
     * Karlos a partir daqui 25
     *
     * @return the informationViews
     *
     */
    public void getInformationViews() {
        String sql = "SELECT * FROM INFORMATION_SCHEMA.VIEWS\n"
                + "WHERE TABLE_CATALOG not in ('master', 'model', 'msdb', 'tempdb')";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (!fields.next()) {

                this.group2.put("informationViews", "false");
            } else {
                this.group2.put("informationViews", "true");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 26
     *
     * @return the certificatesOrSymmetricKeys
     */
    public void getCertificatesOrSymmetricKeys() {
        String sql1 = "SELECT 1 FROM sys.certificates\n"
                + "union\n"
                + "SELECT 1 FROM sys.credentials ";
        String sql2 = "SELECT 1 FROM sys.symmetric_keys\n"
                + "UNION\n"
                + "SELECT 1 FROM sys.asymmetric_keys";

        PreparedStatement preparedStatement1 = driver.prepareStatement(sql1);
        PreparedStatement preparedStatement2 = driver.prepareStatement(sql2);

        ResultSet fields1 = driver.executeQuery(preparedStatement1);
        ResultSet fields2 = driver.executeQuery(preparedStatement2);

        try {

            if (!fields1.next() && !fields2.next()) {
                this.group5.put("certificatesOrSymmetricKeys", "false");
            } else {
                this.group5.put("certificatesOrSymmetricKeys", "true");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 27
     *
     * @return the masterKey
     */
    public void getMasterKey() {
        String sql = "SELECT * FROM sys.master_key_passwords";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (!fields.next()) {
                this.group4.put("masterKey", "false");
            } else {
                this.group4.put("masterKey", "true");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 28
     *
     * @return the encryptedDatabases
     */
    public void getEncryptedDatabases() {
        String sql = "SELECT * \n"
                + "FROM sys.dm_database_encryption_keys\n"
                + "WHERE encryption_state=3";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (!fields.next()) {
                this.group5.put("encryptedDatabases", "false");
            } else {
                this.group5.put("encryptedDatabases", "true");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 29
     *
     * @return the lastPatch
     */
    public void getLastPatch() {
        String sql = "SELECT cast (SERVERPROPERTY('productversion') as varchar(20))";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getString(1).equals("12.0.2269.0")) {
                this.group3.put("lastPatch", "true");
            } else {
                this.group3.put("lastPatch", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 13
     *
     * @return the authenticationmode
     */
    public void getAuthenticationmode() {
        String sql = "SELECT cast (SERVERPROPERTY ('IsIntegratedSecurityOnly') as varchar(20))";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getString(1).equals("1")) {
                this.group3.put("authenticationMode", "true");
            } else {
                this.group3.put("authenticationMode", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 19
     *
     * @return the filestreamUsers
     */
    public void getFilestreamUsers() {
        String sql = "SELECT  cast(value as varchar) as value   FROM sys.configurations where name = 'filestream access level'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getString("value").equals("0")) {
                this.group3.put("filestreamUsers", "true");
            } else {
                this.group3.put("filestreamUsers", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**20
     * @return the traceFilesDiagSecIssues
     */
    public void getTraceFilesDiagSecIssues() {
         String sql = "SELECT  cast(value as varchar) as value \n" +
                    "FROM sys.configurations\n" +
                    "where name = 'default trace enabled'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getString("value").equals("1")) {
                this.group2.put("traceFilesDiagSecIssues", "true");
            } else {
                this.group2.put("traceFilesDiagSecIssues", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**21
     * @return the directUpdInSystemTables
     */
    public void getDirectUpdInSystemTables() {
        String sql = "SELECT cast(value as varchar) as value \n" +
                    "FROM sys.configurations\n" +
                    "where name = 'allow updates'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getString("value").equals("0")) {
                this.group4.put("directUpdInSystemTables", "true");
            } else {
                this.group4.put("directUpdInSystemTables", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**22
     * @return the remoteAccessToServer
     */
    public void getRemoteAccessToServer() {
        String sql = "SELECT cast(value as varchar) as value \n" +
                    "FROM sys.configurations \n" +
                    "where name = 'remote access' ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getString("value").equals("0")) {
                this.group4.put("remoteAccessToServer", "true");
            } else {
                this.group4.put("remoteAccessToServer", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**23
     * @return the remoteAdminAccess
     */
    public void getRemoteAdminAccess() {
               String sql = "SELECT cast(value as varchar) as value \n" +
                            "FROM sys.configurations\n" +
                            "where name = 'remote admin connections'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getString("value").equals("0")) {
                this.group4.put("remoteAdminAccess", "true");
            } else {
                this.group4.put("remoteAdminAccess", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**24
     * @return the remoteLoginTimeout
     */
    public void getRemoteLoginTimeout() {
                String sql = "SELECT cast(value as varchar) as value \n" +
                            "FROM sys.configurations \n" +
                            "where name = 'remote login timeout (s)' ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getInt("value")<=10) {
                this.group4.put("remoteLoginTimeout", "true");
            } else {
                this.group4.put("remoteLoginTimeout", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Karlos acabou aqui.
}
