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
    public JSONObject defaultPort = new JSONObject();

    //k
    public JSONObject informationViews = new JSONObject();
    public JSONObject certificatesOrSymmetricKeys = new JSONObject();
    public JSONObject masterKey = new JSONObject();
    public JSONObject encryptedDatabases = new JSONObject();
    public JSONObject lastPatch = new JSONObject();
    public JSONObject autenticationmode = new JSONObject();
    public JSONObject filestreamUsers = new JSONObject();
    public JSONObject traceFilesDiagSecIssues = new JSONObject();
    public JSONObject directUpdInSystemTables = new JSONObject();
    public JSONObject remoteAccessToServer = new JSONObject();
    public JSONObject remoteAdminAccess = new JSONObject();
    public JSONObject remoteLoginTimeout = new JSONObject();
    //k

    public SecureSqlServer(ConnectionSGBD driver) {
        this.driver = driver;

        getSysAdminUsers();
        getAuthenticationMode();
        getDBOwnerUser();
        getSAUser();
        getGuestUser();
        getLoginsWithoutPermissions();
        getUsersWithoutLogin();
        getDBOwnerLogins();
        getAuditLevel();
        getAdministratorsGroup();
        getLocalAdministratorsGroup();
        getEnabledNetworkProtocols();
        getPasswordExpirationPolicy();
        getExampleDatabases();
        getValidBackups();
        getDefaultPort();
        getLoginFailures();

    }

    //FINALIZADO
    //06
    public void getSysAdminUsers() {

        String sql = "EXEC master.sys.sp_helpsrvrolemember";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            //Seta o valor inicial como true e se algum elemento nÃ£o comeÃ§ar como sa nem com NT seta o valor para false
            this.sysAdminUsers.put("sysAdminUsers", "true");
            while (fields.next()) {
                //r.add(fields.getString("MemberName"));
                if (!fields.getString("MemberName").startsWith("sa") && !fields.getString("MemberName").startsWith("NT")) {
                    this.sysAdminUsers.put("sysAdminUsers", "false");
                }
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // FINALIZADO
    /*Verificar em cada banco de dados os membros associados a role db_owner*/
    //07
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
                this.dbOwnerUser.put("dbOwnerUser", "true");
            } else {
                this.dbOwnerUser.put("dbOwnerUser", "false");
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

            this.saUser.put("saUser", "false");

            while (fields.next()) {
                if (fields.getString("Renamed").equals("NO")
                        && fields.getString("is_policy_checked").equals("1")
                        && fields.getString("is_expiration_checked").equals("1")
                        && fields.getString("is_disabled").equals("1")) {
                    this.saUser.put("saUser", "true");
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
                this.guestUser.put("guestUser", "true");
            } else {
                this.guestUser.put("guestUser", "false");
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
                this.loginsWithoutPermissions.put("loginsWithoutPermissions", "true");
            } else {
                this.loginsWithoutPermissions.put("loginsWithoutPermissions", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //FINALIZADO
    /* Verificar usuÃ¡rios Ã³rfÃ£os do banco de dados */
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
                this.usersWithoutLogin.put("usersWithoutLogin", "true");
            } else {
                this.usersWithoutLogin.put("usersWithoutLogin", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //FINALIZADO
    //02
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
                this.auditLevel.put("auditLevel", "false");
            }

            if (fields.getString("AuditLevel").equals("Successful logins only") || fields.getString("AuditLevel").equals("Failed logins only")) {
                this.auditLevel.put("auditLevel", "warning");
            }

            if (fields.getString("AuditLevel").equals("Both failed and successful logins")) {
                this.auditLevel.put("auditLevel", "true");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Finalizado 
    //07
    public void getDBOwnerLogins() {

        String sql = "select r.name as role_name, m.name as member_name from sys.database_role_members rm \n"
                + "inner join sys.database_principals r on rm.role_principal_id = r.principal_id\n"
                + "inner join sys.database_principals m on rm.member_principal_id = m.principal_id\n"
                + "where r.name = 'db_owner' and m.name != 'dbo'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.dbOwnerLogins.put("dbOwnerLogins", "true");
            } else {
                this.dbOwnerLogins.put("dbOwnerLogins", "false");
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
                this.administratorsGroup.put("administratorsGroup", "true");
            } else {
                this.administratorsGroup.put("administratorsGroup", "false");
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
                this.localAdministratorsGroup.put("localAdministratorsGroup", "true");
            } else {
                this.localAdministratorsGroup.put("localAdministratorsGroup", "false");
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

            this.passwordExpirationPolicy.put("passwordExpirationPolicy", "true");

            while (fields.next()) {
                String name = fields.getString("name");

                if (!name.equals("sa")
                        && !name.equals("##MS_PolicyEventProcessingLogin##")
                        && !name.equals("##MS_PolicyTsqlExecutionLogin##")
                        && !name.equals("zm.nestle")) {

                    this.passwordExpirationPolicy.put("passwordExpirationPolicy", "false");

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
                this.exampleDatabases.put("exampleDatabases", "true");
            } else {
                this.exampleDatabases.put("exampleDatabases", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //FINALIZADO
    //18
    public void getAuthenticationMode() {

        String sql = "SELECT cast (SERVERPROPERTY ('IsIntegratedSecurityOnly') AS VARCHAR(20)) as 'result'";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            this.authenticationMode.put("authenticationMode", "true");
            System.out.println(preparedStatement);
            System.out.println(fields);
            System.out.println(driver);
            while (fields.next()) {
                if (fields.getString(1).equals("0")) {
                    this.authenticationMode.put("authenticationMode", "false");
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

            this.enabledNetworkProtocols.put("enabledNetworkProtocols", "true");

            while (fields.next()) {
                if (fields.getString("Value").equals("Enabled")) {
                    this.enabledNetworkProtocols.put("enabledNetworkProtocols", "false");
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

            this.validBackups.put("validBackups", "true");

            if (!fields.next()) {

                this.validBackups.put("validBackups", "false");

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
                    this.validBackups.put("validBackups", "false");
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
            if (!fields.next()) {
                this.loginFailures.put("loginFailures", "true");
            } else {
                this.loginFailures.put("loginFailures", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Finalizado 
    //01
    public void getDefaultPort() {

        String sql = "SELECT TOP 1 local_tcp_port as 'defaultPort' \n"
                + "FROM sys.dm_exec_connections\n"
                + "WHERE local_tcp_port IS NOT NULL";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            this.defaultPort.put("defaultPort", "true");

            while (fields.next()) {
                if (fields.getString("defaultPort").equals("1433")) {
                    System.out.println(fields.getString("defaultPort"));
                    this.defaultPort.put("defaultPort", "false");
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
    public JSONObject getInformationViews() {
        String sql = "SELECT * FROM INFORMATION_SCHEMA.VIEWS\n"
                + "WHERE TABLE_CATALOG not in ('master', 'model', 'msdb', 'tempdb')";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (!fields.next()) {

                this.informationViews.put("informationViews", "false");
            } else {
                this.informationViews.put("informationViews", "true");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return informationViews;
    }

    /**
     * 26
     *
     * @return the certificatesOrSymmetricKeys
     */
    public JSONObject getCertificatesOrSymmetricKeys() {
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
                this.certificatesOrSymmetricKeys.put("certificatesOrSymmetricKeys", "false");
            } else {
                this.certificatesOrSymmetricKeys.put("certificatesOrSymmetricKeys", "true");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return certificatesOrSymmetricKeys;
    }

    /**
     * 27
     *
     * @return the masterKey
     */
    public JSONObject getMasterKey() {
        String sql = "SELECT * FROMÂ sys.master_key_passwords";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (!fields.next()) {
                this.masterKey.put("masterKey", "false");
            } else {
                this.masterKey.put("masterKey", "true");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return masterKey;
    }

    /**
     * 28
     *
     * @return the encryptedDatabases
     */
    public JSONObject getEncryptedDatabases() {
        String sql = "SELECT * \n"
                + "FROM sys.dm_database_encryption_keys\n"
                + "WHERE encryption_state=3";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (!fields.next()) {
                this.encryptedDatabases.put("encryptedDatabases", "false");
            } else {
                this.encryptedDatabases.put("encryptedDatabases", "true");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encryptedDatabases;
    }

    /**
     * 29
     *
     * @return the lastPatch
     */
    public JSONObject getLastPatch() {
        String sql = "SELECT SERVERPROPERTY('productversion')";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getString(1).equals("12.0.2269.0")) {
                this.lastPatch.put("lastPatch", "true");
            } else {
                this.lastPatch.put("lastPatch", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lastPatch;
    }

    /**
     * 13
     *
     * @return the autenticationmode
     */
    public JSONObject getAutenticationmode() {
        String sql = "SELECT SERVERPROPERTY ('IsIntegratedSecurityOnly')')";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getString(1).equals("1")) {
                this.autenticationmode.put("autenticationmode", "true");
            } else {
                this.autenticationmode.put("autenticationmode", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return autenticationmode;
    }

    /**
     * 19
     *
     * @return the filestreamUsers
     */
    public JSONObject getFilestreamUsers() {
        String sql = "SELECT * Â \n"
                + "FROM Â sys.configurations \n"
                + "where name = 'filestream access level' Â ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getString(3).equals("0")) {
                this.filestreamUsers.put("filestreamUsers", "true");
            } else {
                this.filestreamUsers.put("filestreamUsers", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return filestreamUsers;
    }

    /**20
     * @return the traceFilesDiagSecIssues
     */
    public JSONObject getTraceFilesDiagSecIssues() {
         String sql = "SELECT * \n" +
                    "FROM Â sys.configurations\n" +
                    "where name = 'default trace enabled'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getString(3).equals("1")) {
                this.traceFilesDiagSecIssues.put("traceFilesDiagSecIssues", "true");
            } else {
                this.traceFilesDiagSecIssues.put("traceFilesDiagSecIssues", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return traceFilesDiagSecIssues;
    }

    /**21
     * @return the directUpdInSystemTables
     */
    public JSONObject getDirectUpdInSystemTables() {
        String sql = "SELECT * \n" +
                    "FROM Â sys.configurations\n" +
                    "where name = 'allow updates'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getString(3).equals("0")) {
                this.directUpdInSystemTables.put("directUpdInSystemTables", "true");
            } else {
                this.directUpdInSystemTables.put("directUpdInSystemTables", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return directUpdInSystemTables;
    }

    /**22
     * @return the remoteAccessToServer
     */
    public JSONObject getRemoteAccessToServer() {
        String sql = "SELECT * \n" +
                    "FROM Â sys.configurations \n" +
                    "where name = 'remote access' ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getString(3).equals("0")) {
                this.remoteAccessToServer.put("remoteAccessToServer", "true");
            } else {
                this.remoteAccessToServer.put("remoteAccessToServer", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return remoteAccessToServer;
    }

    /**23
     * @return the remoteAdminAccess
     */
    public JSONObject getRemoteAdminAccess() {
               String sql = "SELECT * \n" +
                            "FROM Â sys.configurations\n" +
                            "where name = 'remote admin connections'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getString(3).equals("0")) {
                this.remoteAdminAccess.put("remoteAdminAccess", "true");
            } else {
                this.remoteAdminAccess.put("remoteAdminAccess", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return remoteAdminAccess;
    }

    /**24
     * @return the remoteLoginTimeout
     */
    public JSONObject getRemoteLoginTimeout() {
                String sql = "SELECT * \n" +
                            "FROM Â sys.configurations \n" +
                            "where name = 'remote login timeout (s)' ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {

            if (fields.next() && fields.getInt(3)<=10) {
                this.remoteLoginTimeout.put("remoteLoginTimeout", "true");
            } else {
                this.remoteLoginTimeout.put("remoteLoginTimeout", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return remoteLoginTimeout;
    }
    // Karlos acabou aqui.
}
