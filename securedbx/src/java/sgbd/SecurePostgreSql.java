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
import libraries.Configuration;
import libraries.ConnectionSGBD;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Thayson
 */
public class SecurePostgreSql {

    private ConnectionSGBD driver;
    
    public JSONObject superUsers = new JSONObject();
    public JSONObject usersAccessOtherUsers = new JSONObject();
    public JSONObject auditingEnabled = new JSONObject();
    public JSONObject tablesWithRowSecurity = new JSONObject();
    public JSONObject securityPolicies = new JSONObject();
    public JSONObject objectsInPublicSchema = new JSONObject();
    public JSONObject publicObjectsInsDelUp = new JSONObject();
    public JSONObject publicObjectsPrivileges = new JSONObject();
    public JSONObject defaultProceduralLang = new JSONObject();
    public JSONObject nonTrustedProceduralLang = new JSONObject();
    public JSONObject latestVersionBin = new JSONObject();
    public JSONObject noADMStreamOrOffBackup = new JSONObject();
    public JSONObject usersEternalPass = new JSONObject();
    public JSONObject usersNoADMCreateDB = new JSONObject();
    public JSONObject listenAddressesDefault = new JSONObject();
    public JSONObject serverWithDefaultEncription = new JSONObject();
    public JSONObject dbServerGivesRowSecurity = new JSONObject();
    public JSONObject dbServerUseSSL = new JSONObject();
    public JSONObject shortTimeoutAut = new JSONObject();
    public JSONObject perDBUserNames = new JSONObject();
    public JSONObject functionsHighNumbersOfParameters = new JSONObject();
    public JSONObject dbHighNumberOfConnections = new JSONObject();

    public SecurePostgreSql(ConnectionSGBD driver) {
        this.driver = driver;
    }

    /* verifica os usuÃ¡rios que ainda estÃ£o com senha padrÃ£o*/
    public String pwdDefault() {

        String sql = driver.config.getProperty("getUsersPwdDefaultoracle");
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        return sql;
    }

    public static void main(String[] args) {
        ConnectionSGBD driver = new ConnectionSGBD("localhost", "5434", "postgres", "postgres", "123456", "postgresql");

        SecurePostgreSql securePost = new SecurePostgreSql(driver);
        System.out.println("0");
        securePost.getSuperUsers();
        System.out.println("1");
        securePost.getUsersAccessOtherUsers();
        System.out.println("2");
        securePost.getAuditingEnabled();
        System.out.println("3");
        securePost.getTablesWithRowSecurity();
        System.out.println("4");
        securePost.getSecurityPolicies();
        System.out.println("5");
        securePost.getObjectsInPublicSchema();
        System.out.println("6");
        securePost.getPublicObjectsInsDelUp();
        System.out.println("7");
        securePost.getPublicObjectsPrivileges();
        System.out.println("8");
        securePost.getDefaultProceduralLang();
        System.out.println("9");
        securePost.getNonTrustedProceduralLang();
        System.out.println("10");
        securePost.getLatestVersionBin();
        System.out.println("11");
        securePost.getNoADMStreamOrOffBackup();
        System.out.println("12");
        securePost.getUsersEternalPass();
        System.out.println("13");
        securePost.getUsersNoADMCreateDB();
        System.out.println("14");
        securePost.getListenAddressesDefault();
        System.out.println("15");
        securePost.getServerWithDefaultEncription();
        System.out.println("16");
        securePost.getDbServerGivesRowSecurity();
        System.out.println("17");
        securePost.getDbServerUseSSL();
        System.out.println("18");
        securePost.getShortTimeoutAut();
        System.out.println("19");
        securePost.getPerDBUserNames();
        System.out.println("20");
        securePost.getFunctionsHighNumbersOfParameters();
        System.out.println("21");
        securePost.getDbHighNumberOfConnections();
        System.out.println("22");
                
    }

    /**
     * @return the superUsers
     */
    public JSONObject getSuperUsers() {
        String sql = "SELECT u.usename AS \"User name\",\n"
                + "  u.usesysid AS \"User ID\", \n"
                + "  CASE WHEN u.usesuper AND u.usecreatedb THEN CAST('superuser, create \n"
                + "database' AS pg_catalog.text) \n"
                + "       WHEN u.usesuper THEN CAST('superuser' AS pg_catalog.text) \n"
                + "       WHEN u.usecreatedb THEN CAST('create database' AS \n"
                + "pg_catalog.text) \n"
                + "       ELSE CAST('' AS pg_catalog.text) \n"
                + "  END AS \"Attributes\" \n"
                + "FROM pg_catalog.pg_user u \n"
                + "where u.usename <> 'postgres' \n"
                + "ORDER BY 1 ";
        //System.out.println(sql);
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.superUsers.put("superUsers", "true");
            } else {
                this.superUsers.put("superUsers", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return superUsers;
    }

    /**
     * @return the usersAccessOtherUsers
     */
    public JSONObject getUsersAccessOtherUsers() {
        String sql = "SELECT\n"
                + "  use.usename as subject,\n"
                + "  nsp.nspname as namespace, \n"
                + "  c.relname as item,  \n"
                + "  c.relkind as type, \n"
                + "  use2.usename as owner,  \n"
                + "  c.relacl,  \n"
                + "  (use2.usename != use.usename and c.relacl::text !~ ('({|,)' || use.usename || '=')) as public \n"
                + "FROM  \n"
                + "  pg_user use   \n"
                + "  cross join pg_class c  \n"
                + "  left join pg_namespace nsp on (c.relnamespace = nsp.oid)  \n"
                + "  left join pg_user use2 on (c.relowner = use2.usesysid)  \n"
                + "WHERE   \n"
                + "  (  \n"
                + "  c.relowner = use.usesysid or   \n"
                + "  c.relacl::text ~ ('({|,)(|' || use.usename || ')=')   \n"
                + "  )  \n"
                + " and use.usename <> use2.usename  \n"
                + "ORDER BY \n"
                + "  subject, \n"
                + "  namespace, \n"
                + "  item";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.usersAccessOtherUsers.put("usersAccessOtherUsers", "true");
            } else {
                this.usersAccessOtherUsers.put("usersAccessOtherUsers", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usersAccessOtherUsers;
    }

    /**
     * @return the auditingEnabled
     */
    public JSONObject getAuditingEnabled() {
        String sql = "select * \n"
                + "from pg_available_extensions \n"
                + "where name = 'pgaudit' \n"
                + "and installed_version <> '' ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© false.
            if (fields.next()) {
                this.auditingEnabled.put("auditingEnabled", "true");
            } else {
                this.auditingEnabled.put("auditingEnabled", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return auditingEnabled;
    }

    /**
     * @return the tablesWithRowSecurity
     */
    public JSONObject getTablesWithRowSecurity() {
        String sql = "SELECT oid, relname  \n"
                + "FROM pg_class   \n"
                + "WHERE relrowsecurity = 'true'   ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© false.
            if (fields.next()) {
                this.tablesWithRowSecurity.put("tablesWithRowSecurity", "true");
            } else {
                this.tablesWithRowSecurity.put("tablesWithRowSecurity", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tablesWithRowSecurity;
    }

    /**
     * @return the securityPolicies
     */
    public JSONObject getSecurityPolicies() {
        String sql = "select *\n"
                + "from pg_policy";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© false.
            if (fields.next()) {
                this.securityPolicies.put("securityPolicies", "true");
            } else {
                this.securityPolicies.put("securityPolicies", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return securityPolicies;
    }

    /**
     * @return the objectsInPublicSchema
     */
    public JSONObject getObjectsInPublicSchema() {
        String sql = "select table_catalog,  \n"
                + "table_schema, table_name, table_type  \n"
                + "from information_schema.tables   \n"
                + "where table_schema in ('public')   ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
         try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.objectsInPublicSchema.put("objectsInPublicSchema", "true");
            } else {
                this.objectsInPublicSchema.put("objectsInPublicSchema", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return objectsInPublicSchema;
    }

    /**
     * @return the publicObjectsInsDelUp
     */
    public JSONObject getPublicObjectsInsDelUp() {
        String sql = "SELECT * -- grantee, privilege_type \n"
                + "FROM information_schema.role_table_grants \n"
                + "where table_schema = 'public'\n"
                + "and privilege_type in ('INSERT','UPDATE','DELETE')  ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
         try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.publicObjectsInsDelUp.put("publicObjectsInsDelUp", "true");
            } else {
                this.publicObjectsInsDelUp.put("publicObjectsInsDelUp", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return publicObjectsInsDelUp;
    }

    /**
     * @return the publicObjectsPrivileges
     */
    public JSONObject getPublicObjectsPrivileges() {
        String sql = "SELECT *  \n"
                + "FROM information_schema.role_table_grants \n"
                + "where table_schema = 'public' \n"
                + "and is_grantable = 'YES'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
         try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.publicObjectsPrivileges.put("publicObjectsPrivileges", "true");
            } else {
                this.publicObjectsPrivileges.put("publicObjectsPrivileges", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return publicObjectsPrivileges;
    }

    /**
     * @return the defaultProceduralLang
     */
    public JSONObject getDefaultProceduralLang() {
        String sql = "select * \n"
                + "from pg_language \n"
                + "where lanname not in ('internal','c','sql','plpgsql')";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
         try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.defaultProceduralLang.put("defaultProceduralLang", "true");
            } else {
                this.defaultProceduralLang.put("defaultProceduralLang", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return defaultProceduralLang;
    }

    /**
     * @return the nonTrustedProceduralLang
     */
    public JSONObject getNonTrustedProceduralLang() {
        String sql = "select * \n"
                + "from pg_language \n"
                + "where lanpltrusted = 'f'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);

         try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.nonTrustedProceduralLang.put("nonTrustedProceduralLang", "true");
            } else {
                this.nonTrustedProceduralLang.put("nonTrustedProceduralLang", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nonTrustedProceduralLang;
    }

    /**
     * @return the latestVersionBin
     */
    public JSONObject getLatestVersionBin() {
        String sql = "select substr(version(),0,17) ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
    
        try {
            fields.next();
            //System.out.println("AAA:"+fields.getString(1));
            if(fields.getString(1).equals("PostgreSQL 9.5.4")){
                this.latestVersionBin.put("latestVersionBin", "true");
                //System.out.print("BATEU");
            }else{
                this.latestVersionBin.put("latestVersionBin", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return latestVersionBin;
    }

    /**
     * @return the noADMStreamOrOffBackup
     */
    public JSONObject getNoADMStreamOrOffBackup() {
        String sql = "SELECT * \n"
                + "FROM pg_catalog.pg_user u \n"
                + "where  u.userepl='t' \n"
                + "and usename <> 'postgres'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.noADMStreamOrOffBackup.put("noADMStreamOrOffBackup", "true");
            } else {
                this.noADMStreamOrOffBackup.put("noADMStreamOrOffBackup", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return noADMStreamOrOffBackup;
    }

    /**
     * @return the usersEternalPass
     */
    public JSONObject getUsersEternalPass() {
        String sql = "SELECT * , \n"
                + "  CASE \n"
                + "	WHEN u.usesuper AND u.usecreatedb THEN CAST('superuser, create\n"
                + "database' AS pg_catalog.text) \n"
                + "       WHEN u.usesuper THEN CAST('superuser' AS pg_catalog.text)\n"
                + "       WHEN u.usecreatedb THEN CAST('create database' AS\n"
                + "pg_catalog.text) \n"
                + "       ELSE CAST('' AS pg_catalog.text) \n"
                + "  END AS \"Attributes\" \n"
                + "FROM pg_catalog.pg_user u  \n"
                + "where valuntil = 'infinity'\n"
                + "ORDER BY 1 ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.usersEternalPass.put("usersEternalPass", "true");
            } else {
                this.usersEternalPass.put("usersEternalPass", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usersEternalPass;
    }

    /**
     * @return the usersNoADMCreateDB
     */
    public JSONObject getUsersNoADMCreateDB() {
        String sql = "SELECT * \n"
                + "FROM pg_catalog.pg_user u \n"
                + "where  u.usecreatedb='t' \n"
                + "and usename <> 'postgres' ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.usersNoADMCreateDB.put("usersNoADMCreateDB", "true");
            } else {
                this.usersNoADMCreateDB.put("usersNoADMCreateDB", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usersNoADMCreateDB;
    }

    /**
     * @return the listenAddressesDefault
     */
    public JSONObject getListenAddressesDefault() {
        String sql = "select *\n"
                + "from pg_catalog.pg_settings  \n"
                + "where  name  = 'listen_addresses'\n"
                + "and setting = '*'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
         try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.listenAddressesDefault.put("listenAddressesDefault", "true");
            } else {
                this.listenAddressesDefault.put("listenAddressesDefault", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listenAddressesDefault;
    }

    /**
     * @return the serverWithDefaultEncription
     */
    public JSONObject getServerWithDefaultEncription() {
        String sql = "select *\n"
                + "from pg_catalog.pg_settings  \n"
                + "where  name  = 'password_encryption'\n"
                + "and setting = 'on'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
         try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.serverWithDefaultEncription.put("serverWithDefaultEncription", "true");
            } else {
                this.serverWithDefaultEncription.put("serverWithDefaultEncription", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return serverWithDefaultEncription;
    }

    /**
     * @return the dbServerGivesRowSecurity
     */
    public JSONObject getDbServerGivesRowSecurity() {
        String sql = "select * \n"
                + "from pg_catalog.pg_settings   \n"
                + "where  name  = 'row_security' \n"
                + "and setting = 'on' ";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© false.
            if (fields.next()) {
                this.dbServerGivesRowSecurity.put("dbServerGivesRowSecurity", "true");
            } else {
                this.dbServerGivesRowSecurity.put("dbServerGivesRowSecurity", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dbServerGivesRowSecurity;
    }

    /**
     * @return the dbServerUseSSL
     */
    public JSONObject getDbServerUseSSL() {
        String sql = "select *\n"
                + "from pg_catalog.pg_settings  \n"
                + "where  name  = 'ssl'\n"
                + "and setting = 'off'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        
        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© false.
            if (fields.next()) {
                this.dbServerUseSSL.put("dbServerUseSSL", "true");
            } else {
                this.dbServerUseSSL.put("dbServerUseSSL", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dbServerUseSSL;
    }

    /**
     * @return the shortTimeoutAut
     */
    public JSONObject getShortTimeoutAut() {
        String sql = "select *\n"
                + "from pg_catalog.pg_settings  \n"
                + "where  name  = 'authentication_timeout'\n"
                + "and setting <= '60'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© false.
            if (fields.next()) {
                this.shortTimeoutAut.put("shortTimeoutAut", "true");
            } else {
                this.shortTimeoutAut.put("shortTimeoutAut", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return shortTimeoutAut;
    }

    /**
     * @return the perDBUserNames
     */
    public JSONObject getPerDBUserNames() {
        String sql = "select *\n"
                + "from pg_catalog.pg_settings  \n"
                + "where  name  = 'db_user_namespace'\n"
                + "and setting <= 'off'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
         try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© true.
            if (!fields.next()) {
                this.perDBUserNames.put("perDBUserNames", "true");
            } else {
                this.perDBUserNames.put("perDBUserNames", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return perDBUserNames;
    }

    /**
     * @return the functionsHighNumbersOfParameters
     */
    public JSONObject getFunctionsHighNumbersOfParameters() {
        String sql = "select *\n"
                + "from pg_catalog.pg_settings  \n"
                + "where  name  = 'max_function_args'\n"
                + "and setting <= '100'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© false.
            if (fields.next()) {
                this.functionsHighNumbersOfParameters.put("functionsHighNumbersOfParameters", "true");
            } else {
                this.functionsHighNumbersOfParameters.put("functionsHighNumbersOfParameters", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return functionsHighNumbersOfParameters;
    }

    /**
     * @return the dbHighNumberOfConnections
     */
    public JSONObject getDbHighNumberOfConnections() {
        String sql = "select *\n"
                + "from pg_catalog.pg_settings  \n"
                + "where  name  = 'max_connections'\n"
                + "and setting <= '100'";

        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            //checa se o result set possui dados, caso ele esteja vazio a resposta Ã© false.
            if (fields.next()) {
                this.dbHighNumberOfConnections.put("dbHighNumberOfConnections", "true");
            } else {
                this.dbHighNumberOfConnections.put("dbHighNumberOfConnections", "false");
            }

        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dbHighNumberOfConnections;
    }
}
