package io.github.lucasduete.upSafe.dao;

import io.github.lucasduete.upSafe.factories.Conexao;
import io.github.lucasduete.upSafe.models.Arquivo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArquivoDao {

    public ArquivoDao() {
    }

    public boolean salvar(Arquivo arquivo) throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO Arquivo (Nome, Content, Tamanho, IdUsuario) VALUES (?,?,?,?);";

        try (Connection conn = Conexao.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, arquivo.getNome());
            stmt.setString(2, arquivo.getContent());
            stmt.setInt(3, arquivo.getTamanho());
            stmt.setInt(4, arquivo.getIdUsuario());

            stmt.executeUpdate();

            stmt.close();
            conn.close();
        }
        return true;

    }

    public ArrayList<Arquivo> listar(int idUsuario) throws SQLException, ClassNotFoundException {

        String sql = "SELECT Id, Nome, Tamanho FROM Arquivo WHERE idUsuario = ?;";
        ArrayList<Arquivo> arquivos= new ArrayList<>();

        try (Connection conn = Conexao.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, idUsuario);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Arquivo file = new Arquivo();

                file.setNome(rs.getString("Nome"));
                file.setId(rs.getInt("Id"));
                file.setTamanho(rs.getInt("Tamanho"));
                file.setIdUsuario(rs.getInt("idUsuario"));

                arquivos.add(file);
            }

            rs.close();
            stmt.close();
            conn.close();

        }

        return arquivos;

    }

    public Arquivo getArquivo(int id) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM Arquivo WHERE ID = ?";
        Arquivo arquivo = null;

        try (Connection conn = Conexao.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                arquivo = new Arquivo();

                arquivo.setNome(rs.getString("Nome"));
                arquivo.setContent(rs.getString("Content"));
                arquivo.setTamanho(rs.getInt("Tamanho"));
                arquivo.setIdUsuario(rs.getInt("idUsuario"));
            }

            rs.close();
            stmt.close();
            conn.close();

        }
        return arquivo;

    }

    public boolean remover(int id) throws SQLException, ClassNotFoundException {

        String sql = "DELETE * FROM Arquivo WHERE ID = ?";

        try (Connection conn = Conexao.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);

            stmt.executeUpdate();

            stmt.close();
            conn.close();

        }
        return true;

    }

    public boolean atualizar(Arquivo arquivo) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE Arquivo SET Nome = ?, Content = ?, Tamanho = ? WHERE Id = ?";

        try (Connection conn = Conexao.getConnection()) {
             PreparedStatement stmt = conn.prepareStatement(sql);

             stmt.setString(1, arquivo.getNome());
             stmt.setString(2, arquivo.getContent());
             stmt.setInt(3, arquivo.getTamanho());
             stmt.setInt(4, arquivo.getId());

             stmt.executeUpdate();

             stmt.close();
             conn.close();
        }

        return true;
    }
}
