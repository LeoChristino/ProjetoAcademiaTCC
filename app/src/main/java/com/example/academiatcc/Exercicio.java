package com.example.academiatcc;

public class Exercicio {

    String nome, grupoMuscular, url, id;

    public Exercicio(){}

    public Exercicio(String nome, String url, String grupoMuscular) {
        this.nome = nome;
        this.url = url;
        this.grupoMuscular = grupoMuscular;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}