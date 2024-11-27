package com.example.academiatcc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.academiatcc.R;
import com.example.academiatcc.dataclass.ExercicioTreino;

import java.util.ArrayList;

public class ExercicioTreinoAdapter extends RecyclerView.Adapter<ExercicioTreinoAdapter.ViewHolder> {

    private ArrayList<ExercicioTreino> exercicios;

    public ExercicioTreinoAdapter(ArrayList<ExercicioTreino> exercicios) {
        this.exercicios = exercicios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_exerciciotreino, parent, false); // Replace with your item layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExercicioTreino exercicio = exercicios.get(position);
        holder.grupoMuscularTextView.setText(exercicio.grupoMuscular);
        holder.nomeExercicioTextView.setText(exercicio.nomeExercicio);
        holder.seriesTextView.setText(String.valueOf(exercicio.series));
        holder.repeticoesTextView.setText(String.valueOf(exercicio.repeticoes));
        holder.cargaTextView.setText(String.valueOf(exercicio.carga+" Kg"));
    }

    @Override
    public int getItemCount() {
        return exercicios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView grupoMuscularTextView;
        public TextView nomeExercicioTextView;
        public TextView seriesTextView;
        public TextView repeticoesTextView;
        public TextView cargaTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            grupoMuscularTextView = itemView.findViewById(R.id.txtModeloGrupoMuscularTreino); // Replace with your view IDs
            nomeExercicioTextView = itemView.findViewById(R.id.txtModeloNomeExercicioTreino);
            seriesTextView = itemView.findViewById(R.id.txtModeloNumeroSeries);
            repeticoesTextView = itemView.findViewById(R.id.txtModeloNumeroRep);
            cargaTextView = itemView.findViewById(R.id.txtModeloCarga);
        }
    }
}