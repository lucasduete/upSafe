package io.github.lucasduete.upSafe.dao;

import io.github.lucasduete.upSafe.factories.Conexao;
import io.github.lucasduete.upSafe.models.Usuario;
import io.github.lucasduete.upSafe.resources.Encryption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDao {

    public UsuarioDao() {

    }

    public boolean salvar(Usuario user) throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO Usuario(Nome, Email, Password) VALUES (?,?,?);";

        try (Connection conn = Conexao.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, user.getNome());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, Encryption.encrypt(user.getPassword()));

            stmt.executeUpdate();

            stmt.close();
            conn.close();
        }
        return true;

    }

    public Usuario getUsuario(int id) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM Usuario WHERE Id = ?;";
        Usuario user = null;

        try (Connection conn = Conexao.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                user = new Usuario(
                        id,
                        rs.getString("Nome"),
                        rs.getString("Email"),
                        rs.getString("Password")
                );

            }

            rs.close();
            stmt.close();
            conn.close();
        }
        return user;

    }

    public boolean remover(int id) throws SQLException, ClassNotFoundException {

        String sql = "DELETE FROM Arquivo WHERE IdUsuario = ?;" +
                    "DELETE FROM Usuario WHERE Id = ?";

        try (Connection conn = Conexao.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);
            stmt.setInt(2, id);

            stmt.executeUpdate();

            stmt.close();
            conn.close();
        }

        return true;
    }

    public boolean atualizar(Usuario user) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE Usuario SET Nome = ?, Email = ?, Password = ? WHERE Id = ?";

        try (Connection conn = Conexao.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, user.getNome());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, Encryption.encrypt(user.getPassword()));
            stmt.setInt(4, user.getId());

            stmt.executeUpdate();

            stmt.close();
            conn.close();

        }
        return true;
    }

    public Usuario login(String email, String senha) throws SQLException, ClassNotFoundException {

        Usuario user= null;

        String sql = "SELECT * FROM Usuario WHERE Email ILIKE ?;";

        Connection conn = Conexao.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, email);

        ResultSet rs = stmt.executeQuery();

        if(!rs.next())
            throw new SQLException("Credenciais Inválidas");


        if(Encryption.checkPassword(senha, rs.getString("Password"))) {
            user.setId(rs.getInt("Id"));
            user.setNome(rs.getString("Nome"));
            user.setEmail(rs.getString("Email"));
        }
        else
            throw new SQLException("Credenciais Inválidas");

        rs.close();
        stmt.close();
        conn.close();

        return user;
    }
}
