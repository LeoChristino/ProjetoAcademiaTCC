package com.example.academiatcc.dataclass;

public class Aluno {

    String nome, sexo, url, id;

    public Aluno() {}

    public Aluno(String nome, String sexo, String url, String id) {
        this.nome = nome;
        this.sexo = sexo;
        this.url = url;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
