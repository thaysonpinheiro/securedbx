package libraries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionSGBD {

    public final Configuration config;    
    private String host;
    private String port;
    private String base;
    private String user;
    private String password;
    private String sgbd;
    public int estado;
    
    public ConnectionSGBD(String host, String port, String base, String user, String password, String sgbd) {
        System.out.println("Criando conexoes...");
        this.config = new Configuration();
        
        this.host = host;
        this.port = port;
        this.base = base;
        this.user = user;
        this.password = password;
        this.sgbd = sgbd;
        this.estado = 0;
        connect();
    }

    public static Connection connection = null;

    private void connect() {
        try {
            switch (this.sgbd) {
                case "sqlserver":
                    Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver"); 
                    connection = DriverManager.getConnection("jdbc:sqlserver://" + this.host + ":" + this.port + ";" + "databaseName=" + this.base + ";", this.user, this.password);
                    System.out.println("Conectado ao bd " + "jdbc:sqlserver://" + this.host + ":" + this.port + ";" + "databaseName=" + this.base);
                    this.estado = 1;
                    break;
                case "postgresql":
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection("jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.base, this.user, this.password);                       
                    System.out.println("Conectado ao bd " + "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.base);
                    this.estado = 1;
                    break;
                case "oracle":
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    connection = DriverManager.getConnection("jdbc:oracle:thin:@" + this.host + ":" + this.port + ":" + this.base, this.user, this.password);
                    System.out.println("Conectado ao bd " + "jdbc:oracle:thin:@" + this.host + ":" + this.port + ":" + this.base);
                    System.out.println(connection.isValid(0));
                    this.estado = 1;
                    break;
                default:
                    throw new UnsupportedOperationException("Atributo SGBD do arquivo de parâmetros (.properties) nao foi atribuido corretamente.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Erro: " + e);
        }
    }

    public void closeConnection() throws SQLException {
        System.out.println("Fechando conexão");
        ConnectionSGBD.connection.close();
    }

    public PreparedStatement prepareStatement(String query) {
        try {
            return ConnectionSGBD.connection.prepareStatement(query);
        } catch (SQLException e) {
            System.out.println("Erro: "+e);
            return null;
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            Statement statement = ConnectionSGBD.connection.createStatement();
            statement.closeOnCompletion();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Erro: "+e);
            return null;
        }
    }

    public void executeUpdate(PreparedStatement prepared) {
        try {
            prepared.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro: "+e);
        } finally {
            try {
                prepared.close();
            } catch (SQLException ex) {
                System.out.println("Erro: "+ex);
            }
        }
    }

    public void executeUpdate(String query) {
        PreparedStatement prepared = this.prepareStatement(query);
        try {
            prepared.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Query com erro:" + query);
            System.out.println("Erro: "+e);
        } finally {
            try {
                prepared.close();
            } catch (SQLException ex) {
                System.out.println("Erro: "+ex);
            }
        }
    }

    public ResultSet executeQuery(PreparedStatement prepared) {
        try {
            return prepared.executeQuery();
        } catch (SQLException e) {
            System.out.println("Erro: "+e);
            return null;
        }
    }

    public Statement getStatement() {
        try {
            Statement statement = ConnectionSGBD.connection.createStatement();
            statement.closeOnCompletion();
            return statement;
        } catch (SQLException ex) {
            System.out.println("Erro: "+ex);
            return null;
        }
    }
}