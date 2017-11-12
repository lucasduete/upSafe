package io.github.lucasduete.upSafe.factories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String url = "jdbc:postgresql://ec2-107-22-211-182.compute-1.amazonaws.com/d5qptji4icd9l0";
    private static final String usuario = "uigpviycyhaytu";
    private static final String senha = "ed4f5570118b0c9cbae684319e65979ed1d9483747b93952cc5bffed0f275da5";
    
    public static Connection getConnection() throws SQLException, ClassNotFoundException{
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(url, usuario, senha);
    }

}
