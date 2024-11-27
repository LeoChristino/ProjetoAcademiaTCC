package com.example.academiatcc.dataclass;

public class ExercicioTreino {
    public String grupoMuscular;
    public String nomeExercicio;
    public int series;
    public int repeticoes;
    public int carga;
    public String treinoId;
    public String id;

    public ExercicioTreino() {
    } // Empty constructor needed for Firestore

    public ExercicioTreino(String grupoMuscular, String nomeExercicio, int series, int repeticoes, int carga, String treinoId, String id) {
        this.grupoMuscular = grupoMuscular;
        this.nomeExercicio = nomeExercicio;
        this.series = series;
        this.repeticoes = repeticoes;
        this.carga = carga;
        this.treinoId = treinoId;
        this.id = id;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public String getNomeExercicio() {
        return nomeExercicio;
    }

    public void setNomeExercicio(String nomeExercicio) {
        this.nomeExercicio = nomeExercicio;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(int repeticoes) {
        this.repeticoes = repeticoes;
    }

    public int getCarga() {
        return carga;
    }

    public void setCarga(int carga) {
        this.carga = carga;
    }

    public String getTreinoId() {
        return treinoId;
    }

    public void setTreinoId(String treinoId) {
        this.treinoId = treinoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}