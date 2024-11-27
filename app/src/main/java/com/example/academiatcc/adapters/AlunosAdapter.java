package com.example.academiatcc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.academiatcc.dataclass.Aluno;
import com.example.academiatcc.R;

import java.util.ArrayList;

public class AlunosAdapter extends RecyclerView.Adapter<AlunosAdapter.AlunosViewHolder> {


    Context context;
    ArrayList<Aluno> alunos;

    public AlunosAdapter(ArrayList<Aluno> alunos, Context context) {
        this.alunos = alunos;
        this.context = context;
    }

    @NonNull
    @Override
    public AlunosAdapter.AlunosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.modelo_lista_alunos, parent, false);
        return new AlunosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlunosViewHolder holder, int position) {
        Aluno aluno = alunos.get(position);
        holder.txtNomeAluno.setText(aluno.getNome());
        holder.txtSexoAluno.setText(aluno.getSexo());
        Glide.with(context).load(aluno.getUrl()).into(holder.imgAluno);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(aluno);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    public class AlunosViewHolder extends RecyclerView.ViewHolder {

        TextView txtNomeAluno, txtSexoAluno;
        ImageView imgAluno;

        public AlunosViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNomeAluno = itemView.findViewById(R.id.txtModeloNomeAluno);
            txtSexoAluno = itemView.findViewById(R.id.txtModeloSexoAluno);
            imgAluno = itemView.findViewById(R.id.imgModeloAluno);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Aluno aluno);
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
