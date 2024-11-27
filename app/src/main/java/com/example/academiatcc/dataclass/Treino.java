package com.example.academiatcc.dataclass;

import java.util.List;

public class Treino {
    String diaDaSemana, grupoMuscular, exercicioId, id;
    int repeticoes; // Added field
    int series;

    public Treino() {
    }

    public Treino(String diaDaSemana, String grupoMuscular, String exercicioId, String id, int repeticoes, int series) {
        this.diaDaSemana = diaDaSemana;
        this.grupoMuscular = grupoMuscular;
        this.exercicioId = exercicioId;
        this.id = id;
        this.repeticoes = repeticoes;
        this.series = series;
    }

    public String getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(String diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public String getExercicioId() {
        return exercicioId;
    }

    public void setExercicioId(String exercicioId) {
        this.exercicioId = exercicioId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(int repeticoes) {
        this.repeticoes = repeticoes;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }
}
