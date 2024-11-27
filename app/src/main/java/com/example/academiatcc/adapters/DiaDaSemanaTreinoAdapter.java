package com.example.academiatcc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.academiatcc.R;
import com.example.academiatcc.dataclass.Treino;

import java.util.ArrayList;
import java.util.List;

public class DiaDaSemanaTreinoAdapter extends RecyclerView.Adapter<DiaDaSemanaTreinoAdapter.DiaDaSemanaTreinoViewHolder> {

    Context context;
    ArrayList<Treino> treinos;

    public DiaDaSemanaTreinoAdapter(ArrayList<Treino> treinos, Context context) {
        this.treinos = treinos;
        this.context = context;
    }

    @NonNull
    @Override
    public DiaDaSemanaTreinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.modelo_diatreino, parent, false);
        return new DiaDaSemanaTreinoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaDaSemanaTreinoViewHolder holder, int position) {
        Treino treino = treinos.get(position);
        holder.txtdiaDaSemana.setText(treino.getDiaDaSemana());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(treino);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return treinos.size();
    }

    public class DiaDaSemanaTreinoViewHolder extends RecyclerView.ViewHolder {
        TextView txtdiaDaSemana;

        public DiaDaSemanaTreinoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdiaDaSemana = itemView.findViewById(R.id.txtDiaDaSemana);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Treino treino);
    }

    private DiaDaSemanaTreinoAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(DiaDaSemanaTreinoAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}