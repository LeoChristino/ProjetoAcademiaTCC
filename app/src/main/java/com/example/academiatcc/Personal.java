package com.example.academiatcc;

//create table tbPersonal(
//codPer int not null auto_increment,
//nome varchar(50) not null,
//cpf char(14) not null unique,
//sexo char(1) default "M" check(sexo in('F','M')),
//ftPer varchar(200),
//cell char(12) not null unique,
//cref varchar(15) not null unique,
//email varchar(50) not null,
//dataNasc date,
//primary key(codPer));



public class Personal {
    private int CodPersonal;
    private String NomePersonal;
    private String CPF;
    private String Sexo;
    private int ftPerfil;
    private String Celular;
    private String CREF;
    private String Email;
    private String DataNasc;

    public int getCodPersonal() {
        return CodPersonal;
    }

    public void setCodPersonal(int codPersonal) {
        CodPersonal = codPersonal;
    }

    public String getNomePersonal() {
        return NomePersonal;
    }

    public void setNomePersonal(String nomePersonal) {
        NomePersonal = nomePersonal;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String sexo) {
        Sexo = sexo;
    }

    public int getFtPerfil() {
        return ftPerfil;
    }

    public void setFtPerfil(int ftPerfil) {
        this.ftPerfil = ftPerfil;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String celular) {
        Celular = celular;
    }

    public String getCREF() {
        return CREF;
    }

    public void setCREF(String CREF) {
        this.CREF = CREF;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDataNasc() {
        return DataNasc;
    }

    public void setDataNasc(String dataNasc) {
        DataNasc = dataNasc;
    }

    public Personal(int codPersonal, String nomePersonal, String CPF, String sexo, int ftPerfil, String celular, String CREF, String email, String dataNasc) {
        CodPersonal = codPersonal;
        NomePersonal = nomePersonal;
        this.CPF = CPF;
        Sexo = sexo;
        this.ftPerfil = ftPerfil;
        Celular = celular;
        this.CREF = CREF;
        Email = email;
        DataNasc = dataNasc;
    }
}
