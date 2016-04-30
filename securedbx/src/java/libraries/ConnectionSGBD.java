/*
 * DBX - Database eXternal Tuning
 * BioBD Lab - PUC-Rio && DC - UFC  *
 */
package libraries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionSGBD {

    private final Configuration config;
    //private final Log log;
    
    private String host;
    private String port;
    private String base;
    private String user;
    private String password;
    private String sgbd;
    
    public ConnectionSGBD(String host, String port, String base, String user, String password, String sgbd) {
        this.config = new Configuration();
        //this.log = new Log(this.config);
        
        this.host = host;
        this.host = port;
        this.host = base;
        this.host = user;
        this.host = password;
        this.sgbd = sgbd;
        connect();
    }

    protected static Connection connection = null;

    private void connect() {
        try {
            if (connection == null) {
                //System.out.println(config.getProperty("sgbd"));
                switch (this.sgbd) {
                    case "sqlserver":
                        connection = DriverManager.getConnection("jdbc:sqlserver://" + this.host + ":" + this.port + ";" + "databaseName=" + this.base + ";", this.user, this.password);
                        //log.msg("Conectado ao bd " + config.getProperty("urlSQLServer") + ":" + config.getProperty("databaseName"));
                        break;
                    case "postgresql":
                        Class.forName("org.postgresql.Driver");
                        connection = DriverManager.getConnection("jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.base, this.user, this.password);
                        //log.msg("Conectado ao bd " + config.getProperty("urlPostgres") + ":" + config.getProperty("databaseName"));
                        break;
                    case "oracle":
                        connection = DriverManager.getConnection("jdbc:oracle:thin:@" + this.host + ":" + this.port + ":" + this.base, this.user, this.password);
                        //log.msg("Conectado ao bd " + config.getProperty("urlOracle") + config.getProperty("databaseName"));
                        break;
                    default:
                        throw new UnsupportedOperationException("Atributo SGBD do arquivo de parâmetros (.properties) nao foi atribuido corretamente.");
                }
                //log.msg("Aplicação versão: " + config.getProperty("versao"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            //log.error(e);
        }
    }

    public void closeConnection() throws SQLException {
        ConnectionSGBD.connection.close();
    }

    public PreparedStatement prepareStatement(String query) {
        try {
            return ConnectionSGBD.connection.prepareStatement(config.getProperty("signature") + " " + query);
        } catch (SQLException e) {
            //log.error(e);
            return null;
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            Statement statement = ConnectionSGBD.connection.createStatement();
            statement.closeOnCompletion();
            return statement.executeQuery(config.getProperty("signature") + " " + query);
        } catch (SQLException e) {
            //log.msg(query);
            //log.error(e);
            return null;
        }
    }

    public void executeUpdate(PreparedStatement prepared) {
        try {
            prepared.executeUpdate();
        } catch (SQLException e) {
            //log.error(e);
        } finally {
            try {
                prepared.close();
            } catch (SQLException ex) {
                //log.error(ex);
            }
        }
    }

    public void executeUpdate(String query) {
        PreparedStatement prepared = this.prepareStatement(query);
        try {
            prepared.executeUpdate();
        } catch (SQLException e) {
            //log.msg("Query com erro:" + query);
            //log.error(e);
        } finally {
            try {
                prepared.close();
            } catch (SQLException ex) {
                //log.error(ex);
            }
        }
    }

    public ResultSet executeQuery(PreparedStatement prepared) {
        try {
            return prepared.executeQuery();
        } catch (SQLException e) {
            //log.error(e);
            return null;
        }
    }

    public Statement getStatement() {
        try {
            Statement statement = ConnectionSGBD.connection.createStatement();
            statement.closeOnCompletion();
            return statement;
        } catch (SQLException ex) {
            //log.error(ex);
            return null;
        }
    }
}
