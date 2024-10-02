package com.example.academiatcc;

//create table tbTreinos(
//codTreino int not null auto_increment,
//codLista int not null,
//nomeTreino varchar(100),
//diaTreino ENUM('segunda', 'terca', 'quarta', 'quinta', 'sexta', 'sabado', 'domingo'),
//primary key(codTreino),
//foreign key(codLista)references tbListaTreinos(codLista));



public class Treino{

    private int codTreino;
    private int codLista;
    private String nomeTreino;
    private String diaTreino;



    public void setCodTreino(int codTreino) {
        this.codTreino = codTreino;
    }

    public void setCodLista(int codLista) {
        this.codLista = codLista;
    }

    public void setNomeTreino(String nomeTreino) {
        this.nomeTreino = nomeTreino;
    }

    public void setDiaTreino(String diaTreino) {
        this.diaTreino = diaTreino;
    }

    public int getCodTreino() {
        return codTreino;
    }

    public int getCodLista() {
        return codLista;
    }

    public String getNomeTreino() {
        return nomeTreino;
    }

    public String getDiaTreino() {
        return diaTreino;
    }

    public Treino(int codTreino, int codLista, String nomeTreino, String diaTreino) {
        this.codTreino = codTreino;
        this.codLista = codLista;
        this.nomeTreino = nomeTreino;
        this.diaTreino = diaTreino;
    }
}


