package io.github.lucasduete.upSafe.models;

import java.util.Arrays;

public class Arquivo {

    private String nome;
    private byte[] content;
    private int tamanho;

    public Arquivo() {
    }

    public Arquivo(String nome, byte[] content, int tamanho) {
        this.nome = nome;
        this.content = content;
        this.tamanho = tamanho;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Arquivo arquivo = (Arquivo) o;

        if (tamanho != arquivo.tamanho) return false;
        if (!nome.equals(arquivo.nome)) return false;
        return Arrays.equals(content, arquivo.content);
    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
        result = 31 * result + Arrays.hashCode(content);
        result = 31 * result + tamanho;
        return result;
    }

    @Override
    public String toString() {
        return "Arquivo{" +
                "nome='" + nome + '\'' +
                ", content=" + Arrays.toString(content) +
                ", tamanho=" + tamanho +
                '}';
    }
}
