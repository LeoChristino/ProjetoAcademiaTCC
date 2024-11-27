package com.example.academiatcc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.academiatcc.dataclass.Exercicio;
import com.example.academiatcc.R;

import java.util.ArrayList;

public class ExerciciosPersonalAdapter extends RecyclerView.Adapter<ExerciciosPersonalAdapter.ExerciciosViewHolder> {

    Context context;
    ArrayList<Exercicio> exercicios;


    public ExerciciosPersonalAdapter(ArrayList<Exercicio> exercicios, Context context) {
        this.context = context;
        this.exercicios = exercicios;
    }

    @NonNull
    @Override
    public ExerciciosPersonalAdapter.ExerciciosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.modelo_exercicios_personal, parent, false);
        return new ExerciciosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciciosPersonalAdapter.ExerciciosViewHolder holder, int position) {

        Exercicio exercicio = exercicios.get(position);
        holder.txtNomeExercicio.setText(exercicio.getNome());
        holder.txtGrupoMuscular.setText(exercicio.getGrupoMuscular());
        Glide.with(context).load(exercicio.getUrl()).into(holder.imgExercicio);

        if (position == itemSelecionado) {
            CardView cardView = holder.itemView.findViewById(R.id.cardModeloListaExercicios);
            cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.Cor05));
        } else {
            CardView cardView = holder.itemView.findViewById(R.id.cardModeloListaExercicios);
            cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.Cor04));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousSelectedPosition = itemSelecionado;
                itemSelecionado = holder.getAdapterPosition();
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(itemSelecionado);

                if (OnItemClickListener != null && itemSelecionado != RecyclerView.NO_POSITION) {
                    OnItemClickListener.onItemClick(exercicios.get(itemSelecionado));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercicios.size();
    }

    public class ExerciciosViewHolder extends RecyclerView.ViewHolder {

        TextView txtNomeExercicio, txtGrupoMuscular;
        ImageView imgExercicio;

        public ExerciciosViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNomeExercicio = itemView.findViewById(R.id.txtModeloNomeExercicio);
            txtGrupoMuscular = itemView.findViewById(R.id.txtModeloGrupoMuscular);
            imgExercicio = itemView.findViewById(R.id.imgExercicio);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(Exercicio exercicio);
    }
    private OnItemClickListener OnItemClickListener;
    private int itemSelecionado = RecyclerView.NO_POSITION;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.OnItemClickListener = listener;
    }
}