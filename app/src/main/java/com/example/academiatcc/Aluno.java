package com.example.academiatcc;


//create table tbAlunos(
//codAlun int not null auto_increment,
//nome varchar(50) not null,
//sexo char(1) default "M" check(sexo in('F','M')),
//dataNasc date,
//cell char(10),
//email varchar(50) not null,
//ftAlun varchar(200),
//codPer int not null,
//primary key (codAlun),
//foreign key(codPer)references tbPersonal(codPer));

public class Aluno {
    private int CodAluno;
    private String NomeAluno;
    private String Sexo;
    private int ftPerfil;
    private String Celular;
    private String Email;
    private String DataNasc;

    public int getCodAluno() {
        return CodAluno;
    }

    public void setCodAluno(int codAluno) {
        CodAluno = codAluno;
    }

    public String getNomeAluno() {
        return NomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        NomeAluno = nomeAluno;
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

    public Aluno(int codAluno, String nomeAluno, String sexo, int ftPerfil, String celular, String email, String dataNasc) {
        CodAluno = codAluno;
        NomeAluno = nomeAluno;
        Sexo = sexo;
        this.ftPerfil = ftPerfil;
        Celular = celular;
        Email = email;
        DataNasc = dataNasc;
    }
}
