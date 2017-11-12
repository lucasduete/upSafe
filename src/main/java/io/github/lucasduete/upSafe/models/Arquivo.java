package io.github.lucasduete.upSafe.models;

import java.util.Arrays;

public class Arquivo {

    private int id;
    private String nome;
    private String content;
    private int tamanho;
    private int idUsuario;

    public Arquivo() {

    }

    public Arquivo(int id, String nome, String content, int tamanho, int idUsuario) {
        this.id = id;
        this.nome = nome;
        this.content = content;
        this.tamanho = tamanho;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Arquivo arquivo = (Arquivo) o;

        if (id != arquivo.id) return false;
        if (tamanho != arquivo.tamanho) return false;
        if (idUsuario != arquivo.idUsuario) return false;
        if (!nome.equals(arquivo.nome)) return false;
        return content.equals(arquivo.content);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + nome.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + tamanho;
        result = 31 * result + idUsuario;
        return result;
    }

    @Override
    public String toString() {
        return "Arquivo{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", content='" + content + '\'' +
                ", tamanho=" + tamanho +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
