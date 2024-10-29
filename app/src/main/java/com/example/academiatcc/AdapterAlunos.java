package com.example.academiatcc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterAlunos extends RecyclerView.Adapter<AdapterAlunos.ViewHolder> {
    private Context context;
    private List<Alunos> lstAlunos;

    public AdapterAlunos(Context context, List<Alunos> lstAlunos) {
        this.context = context;
        this.lstAlunos = lstAlunos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.modelo_lista_alunos, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtModeloNomeAluno.setText(lstAlunos.get(position).getTitulo());
        holder.imgModeloAluno.setImageResource(lstAlunos.get(position).getImgFilmes());

    }

    @Override
    public int getItemCount() {
        return lstAlunos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtModeloNomeAluno;
        ImageView imgModeloAluno;
        CardView cardModeloListaAluno;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtModeloNomeAluno = itemView.findViewById(R.id.txtModeloNomeAluno);
            imgModeloAluno = itemView.findViewById(R.id.imgModeloAluno);
            cardModeloListaAluno = itemView.findViewById(R.id.cardModeloListaAluno);
        }
    }
}