package com.example.academiatcc;

public class Aluno {
    private int codAlun,codPer;
    private String nome, email, cell, sexo;

    public Aluno() {
    }

    public Aluno(int codAlun, String nome, String email, String cell, String sexo, int codPer) {
        this.codAlun = codAlun;
        this.nome = nome;
        this.email = email;
        this.cell = cell;
        this.sexo = sexo;
        this.codPer = codPer;
    }

    public int getCodAlun(){return codAlun;}
    public void setCodAlun(int codAlun){this.codAlun = codAlun;}

    public String getNome(){return nome;}
    public void setNome(String nome){this.nome = nome;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    public String getCell(){return cell;}
    public void setCell(String ce){this.cell = cell;}

    public String getSexo(){return sexo;}
    public void setSexo(String sexo){this.sexo = sexo;}

    public int getCodPer(){return codPer;}
    public void setCodPer(int codPer){this.codPer = codPer;}

}
