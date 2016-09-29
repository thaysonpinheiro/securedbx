package sgbd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import libraries.ConnectionSGBD;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author Thayson
 */
public class SecureOracle {
    
    private ConnectionSGBD driver;
    
    public JSONObject invisibleUsers = new JSONObject();
    public JSONObject auditingIsEnabled = new JSONObject();
    public JSONObject nonAdministrativeUsers = new JSONObject();
    public JSONObject failedLogin = new JSONObject();
    public JSONObject loginAttempts = new JSONObject();
    public JSONObject loginAttempts2 = new JSONObject();
    public JSONObject distinctUsers = new JSONObject();
    public JSONObject nonAdministrativeUsers2 = new JSONObject();
    public JSONObject nonAdministrativeUsers3 = new JSONObject();
    public JSONObject nonDefaultPrivilege = new JSONObject();
    public JSONObject administrativeRoles = new JSONObject();
    public JSONObject systemPrivileges = new JSONObject();
    public JSONObject privilegesConfigured = new JSONObject();
    public JSONObject externalLibraries = new JSONObject();
    public JSONObject defaultDatabasePassword = new JSONObject();
    public JSONObject manyNonSystemUserSessions = new JSONObject();
    public JSONObject serverVersionInformation = new JSONObject();
    public JSONObject nonCaseSensitivePasswords = new JSONObject();
    public JSONObject enablesSystemAuditing = new JSONObject();
    public JSONObject writeFiles = new JSONObject();
    public JSONObject deprecatedOptimizer = new JSONObject();
    public JSONObject useMaximumMemorySize = new JSONObject();
    public JSONObject securityRoles = new JSONObject();
    

    /**
     *
     * @param driver
     */
    public SecureOracle(ConnectionSGBD driver){ 
        this.driver = driver;
        
        this.getInvisibleUsers();
        this.getAuditingIsEnabled();
        this.getNonAdministrativeUsers();
        this.getFailedLogin();
        this.getLoginAttempts();
        this.getLoginAttempts2();
        this.getDistinctUsers();
        this.getNonAdministrativeUsers2();
        this.getNonAdministrativeUsers3();
        this.getNonDefaultPrivilege();
        this.getAdministrativeRoles();
        this.getSystemPrivileges();
        this.getPrivilegesConfigured();
        this.getDefaultDatabasePassword();
        this.getManyNonSystemUserSessions();
        this.getServerVersionInformation();
        this.getNonCaseSensitivePasswords();
        this.getEnablesSystemAuditing();
        this.getWriteFiles();
        this.getDeprecatedOptimizer();
        this.getUseMaximumMemorySize();
        this.getSecurityRoles();
        
        
    }
    
    /**
     * There exists invisible users 
     * Info: Invisible users are users that may have been inserted directly in database dictionary metadata
     */
    public void getInvisibleUsers(){
        String sql =    "select name \n" +
                        "from sys.user$ where type#>1 \n" +
                        "minus \n" +
                        "select username from SYS.dba_users\n" +
                        "UNION\n" +
                        "select name \n" +
                        " from sys.user$ \n" +
                        "where type#>1 \n" +
                        "minus \n" +
                        "select username from SYS.all_users";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.invisibleUsers.put("invisibleUsers", "true");
            }else
                this.invisibleUsers.put("invisibleUsers", "false");
            /*
            -- OK: retornar vazio
            -- NAO OK: retornar mais que zero linhas
            */
        } catch (SQLException | JSONException ex) {
            
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    /**
     * Check if database auditing is enabled
     */
    public void getAuditingIsEnabled(){
        
        String sql = "select name,value from v$parameter\n" +
                    "where name like 'audit%'";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            this.auditingIsEnabled.put("auditingIsEnabled", "true");
            while(fields.next()){                
                String name = fields.getString("name");
                String value = fields.getString("value");
                if( name.equalsIgnoreCase("audit_trail") && value.equalsIgnoreCase("NONE") ){
                    this.auditingIsEnabled.put("auditingIsEnabled", "false");
                    break;
                }
            }
            /*
            -- OK audit_trail value != NONE
            -- NAO OK audit_trail value = NONE
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * There exists non administrative users that received audit privileges directly
     */
    public void getNonAdministrativeUsers(){
        
        String sql = "select *\n" +
                        "from dba_sys_privs\n" +
                        "where privilege like '%AUDIT%'\n" +
                        "and grantee not in ('SYS','IMP_FULL_DATABASE','DATAPUMP_IMP_FULL_DATABASE',\n" +
                        "'DATAPUMP_IMP_FULL_DATABASE','DBA') ";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.nonAdministrativeUsers.put("nonAdministrativeUsers", "true");
            }else
                this.nonAdministrativeUsers.put("nonAdministrativeUsers", "false");
            /*
            -- OK: VAZIO 
            -- NAO OK: retornar mais que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * There exists several (>=100) failed login attempts in a same day
     */
    public void getFailedLogin(){
        
        String sql =    "select count(*),username,terminal,to_char(timestamp,'DD-MON-YYYY')\n" +
                        "from dba_audit_session \n" +
                        "where returncode<>0 \n" +
                        "group by username,terminal,to_char(timestamp,'DD-MON-YYYY')\n" +
                        "having count(*)>100 ";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.failedLogin.put("failedLogin", "true");
            }else
                this.failedLogin.put("failedLogin", "false");
            /*
            -- OK: VAZIO 
            -- NAO OK: retornar mais que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     /**
     * There exists login attempts without existing users
     */
    public void getLoginAttempts(){
        
        String sql =    "select username,terminal,to_char(timestamp,'DD-MON-YYYY HH24:MI:SS')\n" +
                        "from dba_audit_session\n" +
                        "where returncode<>0\n" +
                        "and not exists (select 'x'\n" +
                        "   from dba_users\n" +
                        "   where dba_users.username=dba_audit_session.username)";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.loginAttempts.put("loginAttempts", "true");
            }else
                this.loginAttempts.put("loginAttempts", "false");
            /*
            -- OK: retornar vazio
            -- NAO OK: retormar mais do que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     /**
     * There exists login Attempts in unusual hours (>1h and <4h)
     */
    public void getLoginAttempts2(){
        
        String sql =    "select	username,\n" +
                        "	terminal,\n" +
                        "    	action_name,\n" +
                        "    	returncode,\n" +
                        "    	to_char(timestamp,'DD-MON-YYYY HH24:MI:SS'),\n" +
                        "    	to_char(logoff_time,'DD-MON-YYYY HH24:MI:SS')\n" +
                        "from dba_audit_session\n" +
                        "where to_date(to_char(timestamp,'HH24:MI:SS'),'HH24:MI:SS') < to_date('04:00:00','HH24:MI:SS')\n" +
                        "or to_date(to_char(timestamp,'HH24:MI:SS'),'HH24:MI:SS') > to_date('01:00:00','HH24:MI:SS')";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.loginAttempts2.put("loginAttempts2", "true");
            }else
                this.loginAttempts2.put("loginAttempts2", "false");
            /*
            -- OK: retornar vazio
            -- NAO OK: retormar mais do que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * There exists distinct users sharing the same database login 
     * Info: same user in distinct terminals
     */
    public void getDistinctUsers(){
        
        String sql =    "select count(distinct(terminal)),username\n" +
                        "from dba_audit_session \n" +
                        "having count(distinct(terminal))>1\n" +
                        "group by username";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.distinctUsers.put("distinctUsers", "true");
            }else
                this.distinctUsers.put("distinctUsers", "false");
            /*
            -- OK: retornar vazio
            -- NAO OK: retormar mais do que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * There exists non administrative users with grantable roles 
     * Info: Grantable roles can be granted to other users 
     */
    public void getNonAdministrativeUsers3(){
        
        String sql =    "select *  \n" +
                        "from dba_role_privs  \n" +
                        "where grantee not in ('DBA','SYS','SYSTEM','APEX_040000','CTXSYS') \n" +
                        "and admin_option = 'YES' \n" +
                        "order by grantee, granted_role";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.nonAdministrativeUsers3.put("nonAdministrativeUsers3", "true");
            }else
                this.nonAdministrativeUsers3.put("nonAdministrativeUsers3", "false");
            /*
            -- OK: retornar vazio
            -- NAO OK: retormar mais do que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * There exists non administrative users that received DBA ROLE directly
     */
    public void getNonAdministrativeUsers2(){
        
        String sql =    "select * \n" +
                        "from dba_role_privs \n" +
                        "where granted_role = 'DBA'\n" +
                        "and grantee not in ('SYS','SYSTEM') \n" +
                        "order by granted_role, grantee";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.nonAdministrativeUsers2.put("nonAdministrativeUsers2", "true");
            }else
                this.nonAdministrativeUsers2.put("nonAdministrativeUsers2", "false");
            /*
            -- OK: retornar vazio
            -- NAO OK: retormar mais do que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * There exists non default privilege ROLES enabled  
     * Info: Non default ROLES should be explicitly enabled by DBA  
     */
    public void getNonDefaultPrivilege(){
        
        String sql =    "select * \n" +
                        "from dba_role_privs \n" +
                        "where default_role = 'NO' ";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.nonDefaultPrivilege.put("nonDefaultPrivilege", "true");
            }else
                this.nonDefaultPrivilege.put("nonDefaultPrivilege", "false");
            /*
            -- OK: retornar vazio
            -- NAO OK: retormar mais do que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * There exists administrative ROLES with password SET 
     */
    public void getAdministrativeRoles(){
        
        String sql =    "select *\n" +
                        "from dba_roles\n" +
                        "where password_required='YES'";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.administrativeRoles.put("administrativeRoles", "true");
            }else
                this.administrativeRoles.put("administrativeRoles", "false");
            /*
            -- OK: retornar vazio
            -- NAO OK: retormar mais do que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * System privileges have been granted directly to non administrative users
     */
    public void getSystemPrivileges(){
        
        String sql =    "select *\n" +
                        "from dba_sys_privs \n" +
                        "where ( grantee = 'PUBLIC'\n" +
                        "or grantee in (select USERNAME from dba_users) )\n" +
                        "and grantee not in ('ADMIN',\n" +
                        "                    'MDSYS',\n" +
                        "                    'DIP',\n" +
                        "                    'HR',\n" +
                        "                    'FLOWS_FILES',\n" +
                        "                    'CTXSYS',\n" +
                        "                    'OUTLN',\n" +
                        "                    'APEX_040000',\n" +
                        "                    'SYSTEM',\n" +
                        "                    'ORACLE_OCM',\n" +
                        "                    'DBSNMP',\n" +
                        "                    'APEX_PUBLIC_USER',\n" +
                        "                    'XDB',\n" +
                        "                    'APPQOSSYS',\n" +
                        "                    'SYS',\n" +
                        "                    'ANONYMOUS')\n" +
                        "order by grantee, privilege";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.systemPrivileges.put("systemPrivileges", "true");
            }else
                this.systemPrivileges.put("systemPrivileges", "false");
            /*
            -- OK: retornar vazio
            -- NAO OK: retormar mais do que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * There exists privileges configured with the GRANTABLE option
     * Info: Privileges with GRANTABLE option could be granted by non administrative user to others non administrative users
     */
    public void getPrivilegesConfigured(){
        
        String sql =    "select grantee, privilege, grantable, count(*) \n" +
                        "from dba_tab_privs \n" +
                        "where grantable='YES' \n" +
                        "and grantee not in ('PUBLIC','SYS','SYSTEM') \n" +
                        "group by grantee, privilege, grantable\n" +
                        "order by grantee";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.privilegesConfigured.put("privilegesConfigured", "true");
            }else
                this.privilegesConfigured.put("privilegesConfigured", "false");
            /*
            -- OK: retornar vazio
            -- NAO OK: retormar mais do que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * There exists external libraries than can be used by database users
     */
    public void getExternalLibraries(){
        
        String sql =    "select owner, library_name, file_spec \n" +
                        "from dba_libraries \n" +
                        "where file_spec is not null";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.externalLibraries.put("externalLibraries", "true");
            }else
                this.externalLibraries.put("externalLibraries", "false");
            /*
            -- OK: retornar vazio
            -- NAO OK: retormar mais do que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * There exists users with default database password
     * Info: Users with default database password can be very harmful, since it will be easy to gain database access and perform database DML Operations
     */
    public void getDefaultDatabasePassword(){
        
        String sql =    "SELECT username \"Account Name\", account_status \"Account Status\"\n" +
                        "  FROM sys.dba_users\n" +
                        " WHERE (username, password) IN\n" +
                        "       (('AASH', '9B52488370BB3D77'), ('ABA1', '30FD307004F350DE'),\n" +
                        "        ('ABM', 'D0F2982F121C7840'), ('AD_MONITOR', '54F0C83F51B03F49'),\n" +
                        "        ('ADAMS', '72CDEF4A3483F60D'), ('ADS', 'D23F0F5D871EB69F'),\n" +
                        "        ('ADSEUL_US', '4953B2EB6FCB4339'), ('AHL', '7910AE63C9F7EEEE'),\n" +
                        "        ('AHM', '33C2E27CF5E401A4'), ('AK', '8FCB78BBA8A59515'),\n" +
                        "        ('AL', '384B2C568DE4C2B5'), ('ALA1', '90AAC5BD7981A3BA'),\n" +
                        "        ('ALLUSERS', '42F7CD03B7D2CA0F'), ('ALR', 'BE89B24F9F8231A9'),\n" +
                        "        ('AMA1', '585565C23AB68F71'), ('AMA2', '37E458EE1688E463'),\n" +
                        "        ('AMA3', '81A66D026DC5E2ED'), ('AMA4', '194CCC94A481DCDE'),\n" +
                        "        ('AMF', 'EC9419F55CDC666B'), ('AMS', 'BD821F59270E5F34'),\n" +
                        "        ('AMS1', 'DB8573759A76394B'), ('AMS2', 'EF611999C6AD1FD7'),\n" +
                        "        ('AMS3', '41D1084F3F966440'), ('AMS4', '5F5903367FFFB3A3'),\n" +
                        "        ('AMSYS', '4C1EF14ECE13B5DE'), ('AMV', '38BC87EB334A1AC4'),\n" +
                        "        ('AMW', '0E123471AACA2A62'), ('ANNE', '1EEA3E6F588599A6'),\n" +
                        "        ('ANONYMOUS', '94C33111FD9C66F3'), ('AOLDEMO', 'D04BBDD5E643C436'),\n" +
                        "        ('AP', 'EED09A552944B6AD'), ('APA1', 'D00197BF551B2A79'),\n" +
                        "        ('APA2', '121C6F5BD4674A33'), ('APA3', '5F843C0692560518'),\n" +
                        "        ('APA4', 'BF21227532D2794A'), ('APPLEAD', '5331DB9C240E093B'),\n" +
                        "        ('APPLSYS', '0F886772980B8C79'), ('APPLSYS', 'E153FFF4DAE6C9F7'),\n" +
                        "        ('APPLSYSPUB', 'D2E3EF40EE87221E'), ('APPS', 'D728438E8A5925E0'),\n" +
                        "        ('APS1', 'F65751C55EA079E6'), ('APS2', '5CACE7B928382C8B'),\n" +
                        "        ('APS3', 'C786695324D7FB3B'), ('APS4', 'F86074C4F4F82D2C'),\n" +
                        "        ('AQDEMO', '5140E342712061DD'), ('AQJAVA', '8765D2543274B42E'),\n" +
                        "        ('AQUSER', '4CF13BDAC1D7511C'), ('AR', 'BBBFE175688DED7E'),\n" +
                        "        ('ARA1', '4B9F4E0667857EB8'), ('ARA2', 'F4E52BFBED4652CD'),\n" +
                        "        ('ARA3', 'E3D8D73AE399F7FE'), ('ARA4', '758FD31D826E9143'),\n" +
                        "        ('ARS1', '433263ED08C7A4FD'), ('ARS2', 'F3AF9F26D0213538'),\n" +
                        "        ('ARS3', 'F6755F08CC1E7831'), ('ARS4', '452B5A381CABB241'),\n" +
                        "        ('ART', '665168849666C4F3'), ('ASF', 'B6FD427D08619EEE'),\n" +
                        "        ('ASG', '1EF8D8BD87CF16BE'), ('ASL', '03B20D2C323D0BFE'),\n" +
                        "        ('ASN', '1EE6AEBD9A23D4E0'), ('ASO', 'F712D80109E3C9D8'),\n" +
                        "        ('ASP', 'CF95D2C6C85FF513'), ('AST', 'F13FF949563EAB3C'),\n" +
                        "        ('AUC_GUEST', '8A59D349DAEC26F7'),\n" +
                        "        ('AURORA$ORB$UNAUTHENTICATED', '80C099F0EADF877E'),\n" +
                        "        ('AUTHORIA', 'CC78120E79B57093'), ('AX', '0A8303530E86FCDD'),\n" +
                        "        ('AZ', 'AAA18B5D51B0D5AC'), ('B2B', 'CC387B24E013C616'),\n" +
                        "        ('BAM', '031091A1D1A30061'), ('BCA1', '398A69209360BD9D'),\n" +
                        "        ('BCA2', '801D9C90EBC89371'), ('BEN', '9671866348E03616'),\n" +
                        "        ('BIC', 'E84CC95CBBAC1B67'), ('BIL', 'BF24BCE2409BE1F7'),\n" +
                        "        ('BIM', '6026F9A8A54B9468'), ('BIS', '7E9901882E5F3565'),\n" +
                        "        ('BIV', '2564B34BE50C2524'), ('BIX', '3DD36935EAEDE2E3'),\n" +
                        "        ('BLAKE', '9435F2E60569158E'), ('BMEADOWS', '2882BA3D3EE1F65A'),\n" +
                        "        ('BNE', '080B5C7EE819BF78'), ('BOM', '56DB3E89EAE5788E'),\n" +
                        "        ('BP01', '612D669D2833FACD'), ('BP02', 'FCE0C089A3ECECEE'),\n" +
                        "        ('BP03', '0723FFEEFBA61545'), ('BP04', 'E5797698E0F8934E'),\n" +
                        "        ('BP05', '58FFC821F778D7E9'), ('BP06', '2F358909A4AA6059'),\n" +
                        "        ('BSC', 'EC481FD7DCE6366A'), ('BUYACCT', 'D6B388366ECF2F61'),\n" +
                        "        ('BUYAPPR1', 'CB04931693309228'), ('BUYAPPR2', '3F98A3ADC037F49C'),\n" +
                        "        ('BUYAPPR3', 'E65D8AD3ACC23DA3'), ('BUYER', '547BDA4286A2ECAE'),\n" +
                        "        ('BUYMTCH', '0DA5E3B504CC7497'), ('CAMRON', '4384E3F9C9C9B8F1'),\n" +
                        "        ('CANDICE', 'CF458B3230215199'), ('CARL', '99ECCC664FFDFEA2'),\n" +
                        "        ('CARLY', 'F7D90C099F9097F1'), ('CARMEN', '46E23E1FD86A4277'),\n" +
                        "        ('CARRIECONYERS', '9BA83B1E43A5885B'),\n" +
                        "        ('CATADMIN', 'AF9AB905347E004F'), ('CE', 'E7FDFE26A524FE39'),\n" +
                        "        ('CEASAR', 'E69833B8205D5DD7'), ('CENTRA', '63BF5FFE5E3EA16D'),\n" +
                        "        ('CFD', '667B018D4703C739'), ('CHANDRA', '184503FA7786C82D'),\n" +
                        "        ('CHARLEY', 'E500DAA705382E8D'), ('CHRISBAKER', '52AFB6B3BE485F81'),\n" +
                        "        ('CHRISTIE', 'C08B79CCEC43E798'), ('CINDY', '3AB2C717D1BD0887'),\n" +
                        "        ('CLARK', '74DF527800B6D713'), ('CLARK', '7AAFE7D01511D73F'),\n" +
                        "        ('CLAUDE', 'C6082BCBD0B69D20'), ('CLINT', '163FF8CCB7F11691'),\n" +
                        "        ('CLN', 'A18899D42066BFCA'), ('CN', '73F284637A54777D'),\n" +
                        "        ('CNCADMIN', 'C7C8933C678F7BF9'), ('CONNIE', '982F4C420DD38307'),\n" +
                        "        ('CONNOR', '52875AEB74008D78'), ('CORY', '93CE4CCE632ADCD2'),\n" +
                        "        ('CRM1', '6966EA64B0DFC44E'), ('CRM2', 'B041F3BEEDA87F72'),\n" +
                        "        ('CRP', 'F165BDE5462AD557'), ('CRPB733', '2C9AB93FF2999125'),\n" +
                        "        ('CRPCTL', '4C7A200FB33A531D'), ('CRPDTA', '6665270166D613BC'),\n" +
                        "        ('CS', 'DB78866145D4E1C3'), ('CSADMIN', '94327195EF560924'),\n" +
                        "        ('CSAPPR1', '47D841B5A01168FF'), ('CSC', 'EDECA9762A8C79CD'),\n" +
                        "        ('CSD', '144441CEBAFC91CF'), ('CSDUMMY', '7A587C459B93ACE4'),\n" +
                        "        ('CSE', 'D8CC61E8F42537DA'), ('CSF', '684E28B3C899D42C'),\n" +
                        "        ('CSI', '71C2B12C28B79294'), ('CSL', 'C4D7FE062EFB85AB'),\n" +
                        "        ('CSM', '94C24FC0BE22F77F'), ('CSMIG', '09B4BB013FBD0D65'),\n" +
                        "        ('CSP', '5746C5E077719DB4'), ('CSR', '0E0F7C1B1FE3FA32'),\n" +
                        "        ('CSS', '3C6B8C73DDC6B04F'), ('CTXDEMO', 'CB6B5E9D9672FE89'),\n" +
                        "        ('CTXSYS', '24ABAB8B06281B4C'), ('CTXSYS', '71E687F036AD56E5'),\n" +
                        "        ('CTXTEST', '064717C317B551B6'), ('CUA', 'CB7B2E6FFDD7976F'),\n" +
                        "        ('CUE', 'A219FE4CA25023AA'), ('CUF', '82959A9BD2D51297'),\n" +
                        "        ('CUG', '21FBCADAEAFCC489'), ('CUI', 'AD7862E01FA80912'),\n" +
                        "        ('CUN', '41C2D31F3C85A79D'), ('CUP', 'C03082CD3B13EC42'),\n" +
                        "        ('CUS', '00A12CC6EBF8EDB8'), ('CZ', '9B667E9C5A0D21A6'),\n" +
                        "        ('DAVIDMORGAN', 'B717BAB262B7A070'), ('DBSNMP', 'E066D214D5421CCC'),\n" +
                        "        ('DCM', '45CCF86E1058D3A5'), ('DD7333', '44886308CF32B5D4'),\n" +
                        "        ('DD7334', 'D7511E19D9BD0F90'), ('DD810', '0F9473D8D8105590'),\n" +
                        "        ('DD811', 'D8084AE609C9A2FD'), ('DD812', 'AB71915CF21E849E'),\n" +
                        "        ('DD9', 'E81821D03070818C'), ('DDB733', '7D11619CEE99DE12'),\n" +
                        "        ('DDD', '6CB03AF4F6DD133D'), ('DEMO8', '0E7260738FDFD678'),\n" +
                        "        ('DES', 'ABFEC5AC2274E54D'), ('DES2K', '611E7A73EC4B425A'),\n" +
                        "        ('DEV2000_DEMOS', '18A0C8BD6B13BEE2'),\n" +
                        "        ('DEVB733', '7500DF89DC99C057'), ('DEVUSER', 'C10B4A80D00CA7A5'),\n" +
                        "        ('DGRAY', '5B76A1EB8F212B85'), ('DIP', 'CE4A36B8E06CA59C'),\n" +
                        "        ('DISCOVERER5', 'AF0EDB66D914B731'), ('DKING', '255C2B0E1F0912EA'),\n" +
                        "        ('DLD', '4454B932A1E0E320'), ('DMADMIN', 'E6681A8926B40826'),\n" +
                        "        ('DMATS', '8C692701A4531286'), ('DMS', '1351DC7ED400BD59'),\n" +
                        "        ('DMSYS', 'BFBA5A553FD9E28A'), ('DOM', '51C9F2BECA78AE0E'),\n" +
                        "        ('DPOND', '79D6A52960EEC216'), ('DSGATEWAY', '6869F3CFD027983A'),\n" +
                        "        ('DV7333', '36AFA5CD674BA841'), ('DV7334', '473B568021BDB428'),\n" +
                        "        ('DV810', '52C38F48C99A0352'), ('DV811', 'B6DC5AAB55ECB66C'),\n" +
                        "        ('DV812', '7359E6E060B945BA'), ('DV9', '07A1D03FD26E5820'),\n" +
                        "        ('DVP1', '0559A0D3DE0759A6'), ('EAA', 'A410B2C5A0958CDF'),\n" +
                        "        ('EAM', 'CE8234D92FCFB563'), ('EC', '6A066C462B62DD46'),\n" +
                        "        ('ECX', '0A30645183812087'), ('EDR', '5FEC29516474BB3A'),\n" +
                        "        ('EDWEUL_US', '5922BA2E72C49787'), ('EDWREP', '79372B4AB748501F'),\n" +
                        "        ('EGC1', 'D78E0F2BE306450D'), ('EGD1', 'DA6D6F2089885BA6'),\n" +
                        "        ('EGM1', 'FB949D5E4B5255C0'), ('EGO', 'B9D919E5F5A9DA71'),\n" +
                        "        ('EGR1', 'BB636336ADC5824A'), ('END1', '688499930C210B75'),\n" +
                        "        ('ENG', '4553A3B443FB3207'), ('ENI', '05A92C0958AFBCBC'),\n" +
                        "        ('ENM1', '3BDABFD1246BFEA2'), ('ENS1', 'F68A5D0D6D2BB25B'),\n" +
                        "        ('ENTMGR_CUST', '45812601EAA2B8BD'),\n" +
                        "        ('ENTMGR_PRO', '20002682991470B3'),\n" +
                        "        ('ENTMGR_TRAIN', 'BE40A3BE306DD857'),\n" +
                        "        ('EOPP_PORTALADM', 'B60557FD8C45005A'),\n" +
                        "        ('EOPP_PORTALMGR', '9BB3CF93F7DE25F1'),\n" +
                        "        ('EOPP_USER', '13709991FC4800A1'), ('EUL_US', '28AEC22561414B29'),\n" +
                        "        ('EVM', '137CEDC20DE69F71'), ('EXA1', '091BCD95EE112EE3'),\n" +
                        "        ('EXA2', 'E4C0A21DBD06B890'), ('EXA3', '40DC4FA801A73560'),\n" +
                        "        ('EXA4', '953885D52BDF5C86'), ('EXFSYS', '66F4EF5650C20355'),\n" +
                        "        ('EXS1', 'C5572BAB195817F0'), ('EXS2', '8FAA3AC645793562'),\n" +
                        "        ('EXS3', 'E3050174EE1844BA'), ('EXS4', 'E963BFE157475F7D'),\n" +
                        "        ('FA', '21A837D0AED8F8E5'), ('FEM', 'BD63D79ADF5262E7'),\n" +
                        "        ('FIA1', '2EB76E07D3E094EC'), ('FII', 'CF39DE29C08F71B9'),\n" +
                        "        ('FLM', 'CEE2C4B59E7567A3'), ('FNI1', '308839029D04F80C'),\n" +
                        "        ('FNI2', '05C69C8FEAB4F0B9'), ('FPA', '9FD6074B9FD3754C'),\n" +
                        "        ('FPT', '73E3EC9C0D1FAECF'), ('FRM', '9A2A7E2EBE6E4F71'),\n" +
                        "        ('FTA1', '65FF9AB3A49E8A13'), ('FTE', '2FB4D2C9BAE2CCCA'),\n" +
                        "        ('FUN', '8A7055CA462DB219'), ('FV', '907D70C0891A85B1'),\n" +
                        "        ('FVP1', '6CC7825EADF994E8'), ('GALLEN', 'F8E8ED9F15842428'),\n" +
                        "        ('GCA1', '47DA9864E018539B'), ('GCA2', 'FD6E06F7DD50E868'),\n" +
                        "        ('GCA3', '4A4B9C2E9624C410'), ('GCA9', '48A7205A4C52D6B5'),\n" +
                        "        ('GCMGR1', '14A1C1A08EA915D6'), ('GCMGR2', 'F4F11339A4221A4D'),\n" +
                        "        ('GCMGR3', '320F0D4258B9D190'), ('GCS', '7AE34CA7F597EBF7'),\n" +
                        "        ('GCS1', '2AE8E84D2400E61D'), ('GCS2', 'C242D2B83162FF3D'),\n" +
                        "        ('GCS3', 'DCCB4B49C68D77E2'), ('GEORGIAWINE', 'F05B1C50A1C926DE'),\n" +
                        "        ('GL', 'CD6E99DACE4EA3A6'), ('GLA1', '86C88007729EB36F'),\n" +
                        "        ('GLA2', '807622529F170C02'), ('GLA3', '863A20A4EFF7386B'),\n" +
                        "        ('GLA4', 'DB882CF89A758377'), ('GLS1', '7485C6BD564E75D1'),\n" +
                        "        ('GLS2', '319E08C55B04C672'), ('GLS3', 'A7699C43BB136229'),\n" +
                        "        ('GLS4', '7C171E6980BE2DB9'), ('GM_AWDA', '4A06A107E7A3BB10'),\n" +
                        "        ('GM_COPI', '03929AE296BAAFF2'), ('GM_DPHD', '0519252EDF68FA86'),\n" +
                        "        ('GM_MLCT', '24E8B569E8D1E93E'), ('GM_PLADMA', '2946218A27B554D8'),\n" +
                        "        ('GM_PLADMH', '2F6EDE96313AF1B7'), ('GM_PLCCA', '7A99244B545A038D'),\n" +
                        "        ('GM_PLCCH', '770D9045741499E6'), ('GM_PLCOMA', '91524D7DE2B789A8'),\n" +
                        "        ('GM_PLCOMH', 'FC1C6E0864BF0AF2'), ('GM_PLCONA', '1F531397B19B1E05'),\n" +
                        "        ('GM_PLCONH', 'C5FE216EB8FCD023'), ('GM_PLNSCA', 'DB9DD2361D011A30'),\n" +
                        "        ('GM_PLNSCH', 'C80D557351110D51'), ('GM_PLSCTA', '3A778986229BA20C'),\n" +
                        "        ('GM_PLSCTH', '9E50865473B63347'), ('GM_PLVET', '674885FDB93D34B9'),\n" +
                        "        ('GM_SPO', 'E57D4BD77DAF92F0'), ('GM_STKH', 'C498A86BE2663899'),\n" +
                        "        ('GMA', 'DC7948E807DFE242'), ('GMD', 'E269165256F22F01'),\n" +
                        "        ('GME', 'B2F0E221F45A228F'), ('GMF', 'A07F1956E3E468E1'),\n" +
                        "        ('GMI', '82542940B0CF9C16'), ('GML', '5F1869AD455BBA73'),\n" +
                        "        ('GMP', '450793ACFCC7B58E'), ('GMS', 'E654261035504804'),\n" +
                        "        ('GR', 'F5AB0AA3197AEE42'), ('GUEST', '1C0A090E404CECD0'),\n" +
                        "        ('HCC', '25A25A7FEFAC17B6'), ('HHCFO', '62DF37933FB35E9F'),\n" +
                        "        ('HR', '4C6D73C3E8B0F0DA'), ('HRI', '49A3A09B8FC291D0'),\n" +
                        "        ('HXC', '4CEA0BF02214DA55'), ('HXT', '169018EB8E2C4A77'),\n" +
                        "        ('IA', '42C7EAFBCEEC09CC'), ('IBA', '0BD475D5BF449C63'),\n" +
                        "        ('IBC', '9FB08604A30A4951'), ('IBE', '9D41D2B3DD095227'),\n" +
                        "        ('IBP', '840267B7BD30C82E'), ('IBU', '0AD9ABABC74B3057'),\n" +
                        "        ('IBY', 'F483A48F6A8C51EC'), ('ICX', '7766E887AF4DCC46'),\n" +
                        "        ('IEB', 'A695699F0F71C300'), ('IEC', 'CA39F929AF0A2DEC'),\n" +
                        "        ('IEM', '37EF7B2DD17279B5'), ('IEO', 'E93196E9196653F1'),\n" +
                        "        ('IES', '30802533ADACFE14'), ('IEU', '5D0E790B9E882230'),\n" +
                        "        ('IEX', '6CC978F56D21258D'), ('IGC', 'D33CEB8277F25346'),\n" +
                        "        ('IGF', '1740079EFF46AB81'), ('IGI', '8C69D50E9D92B9D0'),\n" +
                        "        ('IGS', 'DAF602231281B5AC'), ('IGW', 'B39565F4E3CF744B'),\n" +
                        "        ('IMC', 'C7D0B9CDE0B42C73'), ('IMT', 'E4AAF998653C9A72'),\n" +
                        "        ('INS1', '2ADC32A0B154F897'), ('INS2', 'EA372A684B790E2A'),\n" +
                        "        ('INTERNET_APPSERVER_REGISTRY', 'A1F98A977FFD73CD'),\n" +
                        "        ('INV', 'ACEAB015589CF4BC'), ('IP', 'D29012C144B58A40'),\n" +
                        "        ('IPA', 'EB265A08759A15B4'), ('IPD', '066A2E3072C1F2F3'),\n" +
                        "        ('ISC', '373F527DC0CFAE98'), ('ISTEWARD', '8735CA4085DE3EEA'),\n" +
                        "        ('ITG', 'D90F98746B68E6CA'), ('JA', '9AC2B58153C23F3D'),\n" +
                        "        ('JD7333', 'FB5B8A12AE623D52'), ('JD7334', '322810FCE43285D9'),\n" +
                        "        ('JD9', '9BFAEC92526D027B'), ('JDE', '7566DC952E73E869'),\n" +
                        "        ('JDEDBA', 'B239DD5313303B1D'), ('JE', 'FBB3209FD6280E69'),\n" +
                        "        ('JG', '37A99698752A1CF1'), ('JL', '489B61E488094A8D'),\n" +
                        "        ('JOHNINARI', 'B3AD4DA00F9120CE'), ('JONES', 'B9E99443032F059D'),\n" +
                        "        ('JTF', '5C5F6FC2EBB94124'), ('JTI', 'B8F03D3E72C96F71'),\n" +
                        "        ('JTM', '6D79A2259D5B4B5A'), ('JTR', 'B4E2BE38B556048F'),\n" +
                        "        ('JTS', '4087EE6EB7F9CD7C'), ('JUNK_PS', 'BBC38DB05D2D3A7A'),\n" +
                        "        ('JUSTOSHUM', '53369CD63902FAAA'),\n" +
                        "        ('KELLYJONES', 'DD4A3FF809D2A6CF'),\n" +
                        "        ('KEVINDONS', '7C6D9540B45BBC39'), ('KPN', 'DF0AED05DE318728'),\n" +
                        "        ('LADAMS', 'AE542B99505CDCD2'), ('LBA', '18E5E15A436E7157'),\n" +
                        "        ('LBACSYS', 'AC9700FD3F1410EB'), ('LDQUAL', '1274872AB40D4FCD'),\n" +
                        "        ('LHILL', 'E70CA2CA0ED555F5'), ('LNS', 'F8D2BC61C10941B2'),\n" +
                        "        ('LQUINCY', '13F9B9C1372A41B6'), ('LSA', '2D5E6036E3127B7E'),\n" +
                        "        ('MDDATA', 'DF02A496267DEE66'), ('MDSYS', '72979A94BAD2AF80'),\n" +
                        "        ('MDSYS', '9AAEB2214DCC9A31'), ('ME', 'E5436F7169B29E4D'),\n" +
                        "        ('MFG', 'FC1B0DD35E790847'), ('MGR1', 'E013305AB0185A97'),\n" +
                        "        ('MGR2', '5ADE358F8ACE73E8'), ('MGR3', '05C365C883F1251A'),\n" +
                        "        ('MGR4', 'E229E942E8542565'), ('MIKEIKEGAMI', 'AAF7A168C83D5C47'),\n" +
                        "        ('MJONES', 'EE7BB3FEA50A21C5'), ('MLAKE', '7EC40274AC1609CA'),\n" +
                        "        ('MM1', '4418294570E152E7'), ('MM2', 'C06B5B28222E1E62'),\n" +
                        "        ('MM3', 'A975B1BD0C093DA3'), ('MM4', '88256901EB03A012'),\n" +
                        "        ('MM5', '4CEA62CBE776DCEC'), ('MMARTIN', 'D52F60115FE87AA4'),\n" +
                        "        ('MOBILEADMIN', '253922686A4A45CC'), ('MRP', 'B45D4DF02D4E0C85'),\n" +
                        "        ('MSC', '89A8C104725367B2'), ('MSD', '6A29482069E23675'),\n" +
                        "        ('MSO', '3BAA3289DB35813C'), ('MSR', 'C9D53D00FE77D813'),\n" +
                        "        ('MST', 'A96D2408F62BE1BC'), ('MWA', '1E2F06BE2A1D41A6'),\n" +
                        "        ('NEILKATSU', '1F625BB9FEBC7617'), ('OBJ7333', 'D7BDC9748AFEDB52'),\n" +
                        "        ('OBJ7334', 'EB6C5E9DB4643CAC'), ('OBJB733', '61737A9F7D54EF5F'),\n" +
                        "        ('OCA', '9BC450E4C6569492'), ('ODM', 'C252E8FA117AF049'),\n" +
                        "        ('ODM_MTR', 'A7A32CD03D3CE8D5'), ('ODS', '89804494ADFC71BC'),\n" +
                        "        ('ODSCOMMON', '59BBED977430C1A8'), ('OE', 'D1A2DFC623FDA40A'),\n" +
                        "        ('OKB', 'A01A5F0698FC9E31'), ('OKC', '31C1DDF4D5D63FE6'),\n" +
                        "        ('OKE', 'B7C1BB95646C16FE'), ('OKI', '991C817E5FD0F35A'),\n" +
                        "        ('OKL', 'DE058868E3D2B966'), ('OKO', '6E204632EC7CA65D'),\n" +
                        "        ('OKR', 'BB0E28666845FCDC'), ('OKS', 'C2B4C76AB8257DF5'),\n" +
                        "        ('OKX', 'F9FDEB0DE52F5D6B'), ('OL810', 'E2DA59561CBD0296'),\n" +
                        "        ('OL811', 'B3E88767A01403F8'), ('OL812', 'AE8C7989346785BA'),\n" +
                        "        ('OL9', '17EC83E44FB7DB5B'), ('OLAPSYS', '3FB8EF9DB538647C'),\n" +
                        "        ('ONT', '9E3C81574654100A'), ('OPI', '1BF23812A0AEEDA0'),\n" +
                        "        ('ORABAM', 'D0A4EA93EF21CE25'),\n" +
                        "        ('ORABAMSAMPLES', '507F11063496F222'),\n" +
                        "        ('ORABPEL', '26EFDE0C9C051988'), ('ORAESB', 'CC7FCCB3A1719EDA'),\n" +
                        "        ('ORAOCA_PUBLIC', 'FA99021634DDC111'),\n" +
                        "        ('ORASAGENT', '234B6F4505AD8F25'), ('ORASSO', 'F3701A008AA578CF'),\n" +
                        "        ('ORASSO_DS', '17DC8E02BC75C141'), ('ORASSO_PA', '133F8D161296CB8F'),\n" +
                        "        ('ORASSO_PS', '63BB534256053305'),\n" +
                        "        ('ORASSO_PUBLIC', 'C6EED68A8F75F5D3'),\n" +
                        "        ('ORDPLUGINS', '88A2B2C183431F00'), ('ORDSYS', '7EFA02EC7EA6B86F'),\n" +
                        "        ('OSM', '106AE118841A5D8C'), ('OTA', 'F5E498AC7009A217'),\n" +
                        "        ('OUTLN', '4A3BA55E08595C81'), ('OWAPUB', '6696361B64F9E0A9'),\n" +
                        "        ('OWF_MGR', '3CBED37697EB01D1'), ('OZF', '970B962D942D0C75'),\n" +
                        "        ('OZP', 'B650B1BB35E86863'), ('OZS', '0DABFF67E0D33623'),\n" +
                        "        ('PA', '8CE2703752DB36D8'), ('PABLO', '5E309CB43FE2C2FF'),\n" +
                        "        ('PAIGE', '02B6B704DFDCE620'), ('PAM', '1383324A0068757C'),\n" +
                        "        ('PARRISH', '79193FDACFCE46F6'), ('PARSON', 'AE28B2BD64720CD7'),\n" +
                        "        ('PAT', 'DD20769D59F4F7BF'), ('PATORILY', '46B7664BD15859F9'),\n" +
                        "        ('PATRICKSANCHEZ', '47F74BD3AD4B5F0A'),\n" +
                        "        ('PATSY', '4A63F91FEC7980B7'), ('PAUL', '35EC0362643ADD3F'),\n" +
                        "        ('PAULA', 'BB0DC58A94C17805'), ('PAXTON', '4EB5D8FAD3434CCC'),\n" +
                        "        ('PCA1', '8B2E303DEEEEA0C0'), ('PCA2', '7AD6CE22462A5781'),\n" +
                        "        ('PCA3', 'B8194D12FD4F537D'), ('PCA4', '83AD05F1D0B0C603'),\n" +
                        "        ('PCS1', '2BE6DD3D1DEA4A16'), ('PCS2', '78117145145592B1'),\n" +
                        "        ('PCS3', 'F48449F028A065B1'), ('PCS4', 'E1385509C0B16BED'),\n" +
                        "        ('PD7333', '5FFAD8604D9DC00F'), ('PD7334', 'CDCF262B5EE254E1'),\n" +
                        "        ('PD810', 'EB04A177A74C6BCB'), ('PD811', '3B3C0EFA4F20AC37'),\n" +
                        "        ('PD812', 'E73A81DB32776026'), ('PD9', 'CACEB3F9EA16B9B7'),\n" +
                        "        ('PDA1', 'C7703B70B573D20F'), ('PEARL', 'E0AFD95B9EBD0261'),\n" +
                        "        ('PEG', '20577ED9A8DB8D22'), ('PENNY', 'BB6103E073D7B811'),\n" +
                        "        ('PEOPLE', '613459773123B38A'), ('PERCY', 'EB9E8B33A2DDFD11'),\n" +
                        "        ('PERRY', 'D62B14B93EE176B6'), ('PETE', '4040619819A9C76E'),\n" +
                        "        ('PEYTON', 'B7127140004677FC'), ('PHIL', '181446AE258EE2F6'),\n" +
                        "        ('PJI', '5024B1B412CD4AB9'), ('PJM', '021B05DBB892D11F'),\n" +
                        "        ('PMI', 'A7F7978B21A6F65E'), ('PN', 'D40D0FEF9C8DC624'),\n" +
                        "        ('PO', '355CBEC355C10FEF'), ('POA', '2AB40F104D8517A0'),\n" +
                        "        ('POLLY', 'ABC770C112D23DBE'), ('POM', '123CF56E05D4EF3C'),\n" +
                        "        ('PON', '582090FD3CC44DA3'), ('PORTAL', 'A96255A27EC33614'),\n" +
                        "        ('PORTAL_APP', '831A79AFB0BD29EC'),\n" +
                        "        ('PORTAL_DEMO', 'A0A3A6A577A931A3'),\n" +
                        "        ('PORTAL_PUBLIC', '70A9169655669CE8'),\n" +
                        "        ('PORTAL30', '969F9C3839672C6D'),\n" +
                        "        ('PORTAL30_DEMO', 'CFD1302A7F832068'),\n" +
                        "        ('PORTAL30_PUBLIC', '42068201613CA6E2'),\n" +
                        "        ('PORTAL30_SSO', '882B80B587FCDBC8'),\n" +
                        "        ('PORTAL30_SSO_PS', 'F2C3DC8003BC90F8'),\n" +
                        "        ('PORTAL30_SSO_PUBLIC', '98741BDA2AC7FFB2'),\n" +
                        "        ('POS', '6F6675F272217CF7'), ('PPM1', 'AA4AE24987D0E84B'),\n" +
                        "        ('PPM2', '4023F995FF78077C'), ('PPM3', '12F56FADDA87BBF9'),\n" +
                        "        ('PPM4', '84E17CB7A3B0E769'), ('PPM5', '804C159C660F902C'),\n" +
                        "        ('PRISTB733', '1D1BCF8E03151EF5'), ('PRISTCTL', '78562A983A2F78FB'),\n" +
                        "        ('PRISTDTA', '3FCBC379C8FE079C'), ('PRODB733', '9CCD49EB30CB80C4'),\n" +
                        "        ('PRODCTL', 'E5DE2F01529AE93C'), ('PRODDTA', '2A97CD2281B256BA'),\n" +
                        "        ('PRODUSER', '752E503EFBF2C2CA'), ('PROJMFG', '34D61E5C9BC7147E'),\n" +
                        "        ('PRP', 'C1C4328F8862BC16'), ('PS', '0AE52ADF439D30BD'),\n" +
                        "        ('PS810', '90C0BEC7CA10777E'), ('PS810CTL', 'D32CCE5BDCD8B9F9'),\n" +
                        "        ('PS810DTA', 'AC0B7353A58FC778'), ('PS811', 'B5A174184403822F'),\n" +
                        "        ('PS811CTL', '18EDE0C5CCAE4C5A'), ('PS811DTA', '7961547C7FB96920'),\n" +
                        "        ('PS812', '39F0304F007D92C8'), ('PS812CTL', 'E39B1CE3456ECBE5'),\n" +
                        "        ('PS812DTA', '3780281C933FE164'), ('PSA', 'FF4B266F9E61F911'),\n" +
                        "        ('PSB', '28EE1E024FC55E66'), ('PSBASS', 'F739804B718D4406'),\n" +
                        "        ('PSEM', '40ACD8C0F1466A57'), ('PSFT', '7B07F6F3EC08E30D'),\n" +
                        "        ('PSFTDBA', 'E1ECD83073C4E134'), ('PSP', '4FE07360D435E2F0'),\n" +
                        "        ('PTADMIN', '4C35813E45705EBA'), ('PTCNE', '463AEFECBA55BEE8'),\n" +
                        "        ('PTDMO', '251D71390034576A'), ('PTE', '380FDDB696F0F266'),\n" +
                        "        ('PTESP', '5553404C13601916'), ('PTFRA', 'A360DAD317F583E3'),\n" +
                        "        ('PTG', '7AB0D62E485C9A3D'), ('PTGER', 'C8D1296B4DF96518'),\n" +
                        "        ('PTJPN', '2159C2EAF20011BF'), ('PTUKE', 'D0EF510BCB2992A3'),\n" +
                        "        ('PTUPG', '2C27080C7CC57D06'), ('PTWEB', '8F7F509D4DC01DF6'),\n" +
                        "        ('PTWEBSERVER', '3C8050536003278B'), ('PV', '76224BCC80895D3D'),\n" +
                        "        ('PY7333', '2A9C53FE066B852F'), ('PY7334', 'F3BBFAE0DDC5F7AC'),\n" +
                        "        ('PY810', '95082D35E94B88C2'), ('PY811', 'DC548D6438E4D6B7'),\n" +
                        "        ('PY812', '99C575A55E9FDA63'), ('PY9', 'B8D4E503D0C4FCFD'),\n" +
                        "        ('QA', 'C7AEAA2D59EB1EAE'), ('QOT', 'B27D0E5BA4DC8DEA'),\n" +
                        "        ('QP', '10A40A72991DCA15'), ('QRM', '098286E4200B22DE'),\n" +
                        "        ('QS', '4603BCD2744BDE4F'), ('QS_ADM', '3990FB418162F2A0'),\n" +
                        "        ('QS_CB', '870C36D8E6CD7CF5'), ('QS_CBADM', '20E788F9D4F1D92C'),\n" +
                        "        ('QS_CS', '2CA6D0FC25128CF3'), ('QS_ES', '9A5F2D9F5D1A9EF4'),\n" +
                        "        ('QS_OS', '0EF5997DC2638A61'), ('QS_WS', '0447F2F756B4F460'),\n" +
                        "        ('RENE', '9AAD141AB0954CF0'), ('REPADMIN', '915C93F34954F5F8'),\n" +
                        "        ('REPORTS', '0D9D14FE6653CF69'),\n" +
                        "        ('REPORTS_USER', '635074B4416CD3AC'),\n" +
                        "        ('RESTRICTED_US', 'E7E67B60CFAFBB2D'), ('RG', '0FAA06DA0F42F21F'),\n" +
                        "        ('RHX', 'FFDF6A0C8C96E676'), ('RLA', 'C1959B03F36C9BB2'),\n" +
                        "        ('RLM', '4B16ACDA351B557D'), ('RM1', 'CD43500DAB99F447'),\n" +
                        "        ('RM2', '2D8EE7F8857D477E'), ('RM3', '1A95960A95AC2E1D'),\n" +
                        "        ('RM4', '651BFD4E1DE4B040'), ('RM5', 'FDCC34D74A22517C'),\n" +
                        "        ('RMAN', 'E7B5D92911C831E1'), ('ROB', '94405F516486CA24'),\n" +
                        "        ('RPARKER', 'CEBFE4C41BBCC306'), ('RWA1', 'B07E53895E37DBBB'),\n" +
                        "        ('SALLYH', '21457C94616F5716'), ('SAM', '4B95138CB6A4DB94'),\n" +
                        "        ('SARAHMANDY', '60BE21D8711EE7D9'), ('SCM1', '507306749131B393'),\n" +
                        "        ('SCM2', 'CBE8D6FAC7821E85'), ('SCM3', '2B311B9CDC70F056'),\n" +
                        "        ('SCM4', '1FDF372790D5A016'), ('SCOTT', 'F894844C34402B67'),\n" +
                        "        ('SDAVIS', 'A9A3B88C6A550559'), ('SECDEMO', '009BBE8142502E10'),\n" +
                        "        ('SEDWARDS', '00A2EDFD7835BC43'), ('SELLCM', '8318F67F72276445'),\n" +
                        "        ('SELLER', 'B7F439E172D5C3D0'), ('SELLTREAS', '6EE7BA85E9F84560'),\n" +
                        "        ('SERVICES', 'B2BE254B514118A5'), ('SETUP', '9EA55682C163B9A3'),\n" +
                        "        ('SH', '54B253CBBAAA8C48'),\n" +
                        "        ('SI_INFORMTN_SCHEMA', '84B8CBCA4D477FA3'),\n" +
                        "        ('SID', 'CFA11E6EBA79D33E'), ('SKAYE', 'ED671B63BDDB6B50'),\n" +
                        "        ('SKYTETSUKA', 'EB5DA777D1F756EC'), ('SLSAA', '99064FC6A2E4BBE8'),\n" +
                        "        ('SLSMGR', '0ED44093917BE294'), ('SLSREP', '847B6AAB9471B0A5'),\n" +
                        "        ('SRABBITT', '85F734E71E391DF5'), ('SRALPHS', '975601AA57CBD61A'),\n" +
                        "        ('SRAY', 'C233B26CFC5DC643'), ('SRIVERS', '95FE94ADC2B39E08'),\n" +
                        "        ('SSA1', 'DEE6E1BEB962AA8B'), ('SSA2', '96CA278B20579E34'),\n" +
                        "        ('SSA3', 'C3E8C3B002690CD4'), ('SSC1', '4F7AC652CC728980'),\n" +
                        "        ('SSC2', 'A1350B328E74AE87'), ('SSC3', 'EE3906EC2DA586D8'),\n" +
                        "        ('SSOSDK', '7C48B6FF3D54D006'), ('SSP', '87470D6CE203FB4D'),\n" +
                        "        ('SSS1', 'E78C515C31E83848'), ('SUPPLIER', '2B45928C2FE77279'),\n" +
                        "        ('SVM7333', '04B731B0EE953972'), ('SVM7334', '62E2A2E886945CC8'),\n" +
                        "        ('SVM810', '0A3DCD8CA3B6ABD9'), ('SVM811', '2B0CD57B1091C936'),\n" +
                        "        ('SVM812', '778632974E3947C9'), ('SVM9', '552A60D8F84441F1'),\n" +
                        "        ('SVMB733', 'DD2BFB14346146FE'), ('SVP1', 'F7BF1FFECE27A834'),\n" +
                        "        ('SY810', 'D56934CED7019318'), ('SY811', '2FDC83B401477628'),\n" +
                        "        ('SY812', '812B8D7211E7DEF1'), ('SY9', '3991E64C4BC2EC5D'),\n" +
                        "        ('SYS', '43CA255A7916ECFE'), ('SYS', '5638228DAF52805F'),\n" +
                        "        ('SYS', 'D4C5016086B2DC6A'), ('SYS7333', 'D7CDB3124F91351E'),\n" +
                        "        ('SYS7334', '06959F7C9850F1E3'), ('SYSADMIN', 'DC86E8DEAA619C1A'),\n" +
                        "        ('SYSB733', '7A7F5C90BEC02F0E'), ('SYSMAN', 'EB258E708132DD2D'),\n" +
                        "        ('SYSTEM', '4D27CA6E3E3066E6'), ('SYSTEM', 'D4DF7931AB130E37'),\n" +
                        "        ('TDEMARCO', 'CAB71A14FA426FAE'), ('TDOS_ICSAP', '7C0900F751723768'),\n" +
                        "        ('TESTCTL', '205FA8DF03A1B0A6'), ('TESTDTA', 'EEAF97B5F20A3FA3'),\n" +
                        "        ('TRA1', 'BE8EDAE6464BA413'), ('TRACESVR', 'F9DA8977092B7B81'),\n" +
                        "        ('TRBM1', 'B10ED16CD76DBB60'), ('TRCM1', '530E1F53715105D0'),\n" +
                        "        ('TRDM1', 'FB1B8EF14CF3DEE7'), ('TRRM1', '4F29D85290E62EBE'),\n" +
                        "        ('TWILLIAMS', '6BF819CE663B8499'), ('UDDISYS', 'BF5E56915C3E1C64'),\n" +
                        "        ('VEA', 'D38D161C22345902'), ('VEH', '72A90A786AAE2914'),\n" +
                        "        ('VIDEO31', '2FA72981199F9B97'), ('VIDEO4', '9E9B1524C454EEDE'),\n" +
                        "        ('VIDEO5', '748481CFF7BE98BB'), ('VP1', '3CE03CD65316DBC7'),\n" +
                        "        ('VP2', 'FCCEFD28824DFEC5'), ('VP3', 'DEA4D8290AA247B2'),\n" +
                        "        ('VP4', 'F4730B0FA4F701DC'), ('VP5', '7DD67A696734AE29'),\n" +
                        "        ('VP6', '45660DEE49534ADB'), ('WAA1', 'CF013DC80A9CBEE3'),\n" +
                        "        ('WAA2', '6160E7A17091741A'), ('WCRSYS', '090263F40B744BD8'),\n" +
                        "        ('WEBDB', 'D4C4DCDD41B05A5D'), ('WEBSYS', '54BA0A1CB5994D64'),\n" +
                        "        ('WENDYCHO', '7E628CDDF051633A'), ('WH', '91792EFFCB2464F9'),\n" +
                        "        ('WIP', 'D326D25AE0A0355C'), ('WIRELESS', '1495D279640E6C3A'),\n" +
                        "        ('WIRELESS', 'EB9615631433603E'), ('WK_TEST', '29802572EB547DBF'),\n" +
                        "        ('WKPROXY', 'AA3CB2A4D9188DDB'), ('WKSYS', '545E13456B7DDEA0'),\n" +
                        "        ('WMS', 'D7837F182995E381'), ('WMSYS', '7C9BA362F8314299'),\n" +
                        "        ('WPS', '50D22B9D18547CF7'), ('WSH', 'D4D76D217B02BD7A'),\n" +
                        "        ('WSM', '750F2B109F49CC13'), ('XDB', '88D8364765FCE6AF'),\n" +
                        "        ('XDO', 'E9DDE8ACFA7FE8E4'), ('XDP', 'F05E53C662835FA2'),\n" +
                        "        ('XLA', '2A8ED59E27D86D41'), ('XLE', 'CEEBE966CC6A3E39'),\n" +
                        "        ('XNB', '03935918FA35C993'), ('XNC', 'BD8EA41168F6C664'),\n" +
                        "        ('XNI', 'F55561567EF71890'), ('XNM', '92776EA17B8B5555'),\n" +
                        "        ('XNP', '3D1FB783F96D1F5E'), ('XNS', 'FABA49C38150455E'),\n" +
                        "        ('XTR', 'A43EE9629FA90CAE'), ('YCAMPOS', 'C3BBC657F099A10F'),\n" +
                        "        ('YSANCHEZ', 'E0C033C4C8CC9D84'), ('ZFA', '742E092A27DDFB77'),\n" +
                        "        ('ZPB', 'CAF58375B6D06513'), ('ZSA', 'AFD3BD3C7987CBB6'),\n" +
                        "        ('ZX', '7B06550956254585'))\n" +
                        "  AND account_status NOT IN ('EXPIRED', 'LOCKED')\n" +
                        " ORDER BY username";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.defaultDatabasePassword.put("defaultDatabasePassword", "true");
            }else
                this.defaultDatabasePassword.put("defaultDatabasePassword", "false");
            /*
            -- OK: retornar vazio
            -- NAO OK: retormar mais do que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * The database warns the DBA if there exists many non-system user sessions in the DBMS server 
     */
    public void getManyNonSystemUserSessions(){
        
        String sql =    "select name, value, description  \n" +
                        "from v$parameter   \n" +
                        "where name = 'license_sessions_warning'   \n" +
                        "and value <> '0'   ";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(fields.next()){                
                this.manyNonSystemUserSessions.put("manyNonSystemUserSessions", "true");
            }else
                this.manyNonSystemUserSessions.put("manyNonSystemUserSessions", "false");
            /*
            -- OK: retornar mais do que ZERO tuplas  
            -- NAO OK: vazio
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * The database returns the detailed/complete server version information to the users 
     */
    public void getServerVersionInformation(){
        
        String sql =    "select name, value, description  \n" +
                        "from v$parameter\n" +
                        "where name = 'sec_return_server_release_banner'\n" +
                        "and value <> 'FALSE'  ";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.serverVersionInformation.put("serverVersionInformation", "true");
            }else
                this.serverVersionInformation.put("serverVersionInformation", "false");
            /*
            -- OK: retornar vazio
            -- NAO OK: retormar mais do que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * The database allow users to perform login using non case sensitive passwords  
     */
    public void getNonCaseSensitivePasswords(){
        
        String sql =    "select name, value, description  \n" +
                        "from v$parameter \n" +
                        "where name = 'sec_case_sensitive_logon' \n" +
                        "and value = 'TRUE' ";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(fields.next()){                
                this.nonCaseSensitivePasswords.put("nonCaseSensitivePasswords", "true");
            }else
                this.nonCaseSensitivePasswords.put("nonCaseSensitivePasswords", "false");
            /*
            -- OK: retornar mais do que ZERO tuplas  
            -- NAO OK: vazio
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * The database enables system auditing   
     */
    public void getEnablesSystemAuditing(){
        
        String sql =    "select name, value, description  \n" +
                        "from v$parameter \n" +
                        "where name = 'audit_trail' \n" +
                        "and value = 'NONE'  ";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.enablesSystemAuditing.put("enablesSystemAuditing", "true");
            }else
                this.enablesSystemAuditing.put("enablesSystemAuditing", "false");
            /*
            -- OK: retornar vazio
            -- NAO OK: retormar mais do que zero tuplas
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * The database allows write files in specific file system directories  
     */
    public void getWriteFiles(){
        
        String sql =    "select name, value, description  \n" +
                        "from v$parameter \n" +
                        "where name = 'utl_file_dir' \n" +
                        "and value is   null ";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(fields.next()){                
                this.writeFiles.put("writeFiles", "true");
            }else
                this.writeFiles.put("writeFiles", "false");
            /*
            -- OK: retornar mais do que ZERO tuplas  
            -- NAO OK: vazio
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * The database server uses a DEPRECATED optimizer 
     */
    public void getDeprecatedOptimizer(){
        
        String sql =    "select name, value, description  \n" +
                        "from v$parameter \n" +
                        "where name = 'optimizer_mode' \n" +
                        "and value = 'RULE' ";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.deprecatedOptimizer.put("deprecatedOptimizer", "true");
            }else
                this.deprecatedOptimizer.put("deprecatedOptimizer", "false");
            /*
            -- OK: vazio  
            -- NAO OK: retornar mais do que ZERO tuplas 
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * The database allows users to allocate and use the maximum memory size of a Java sessionspace
     */
    public void getUseMaximumMemorySize(){
        
        String sql =    "select name, value, description  \n" +
                        "from v$parameter \n" +
                        "where name = 'java_max_sessionspace_size' \n" +
                        "and value = '0'  ";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.useMaximumMemorySize.put("useMaximumMemorySize", "true");
            }else
                this.useMaximumMemorySize.put("useMaximumMemorySize", "false");
            /*
            -- OK: vazio  
            -- NAO OK: retornar mais do que ZERO tuplas 
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * The database allows users to inherit a high number of enabled security roles 
     */
    public void getSecurityRoles(){
        
        String sql =    "select name, value, description  \n" +
                        "from v$parameter \n" +
                        "where name = 'max_enabled_roles' \n" +
                        "and value > '150' ";
        PreparedStatement preparedStatement = driver.prepareStatement(sql);
        ResultSet fields = driver.executeQuery(preparedStatement);
        try {
            if(!fields.next()){                
                this.securityRoles.put("securityRoles", "true");
            }else
                this.securityRoles.put("securityRoles", "false");
            /*
            -- OK: vazio  
            -- NAO OK: retornar mais do que ZERO tuplas 
            */
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(SecureSqlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}