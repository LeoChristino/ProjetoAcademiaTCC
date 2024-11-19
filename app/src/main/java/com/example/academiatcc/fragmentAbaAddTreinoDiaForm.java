package com.example.academiatcc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class fragmentAbaAddTreinoDiaForm extends Fragment {

    private EditText edtNomeTreino;
    private CheckBox ckbDom, ckbSeg, ckbTer, ckbQua, ckbQui, ckbSex, ckbSab;
    private Button btnSalvar;
    private DatabaseReference databaseRef;
    private String codigoListaTreino;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aba_add_treino_dia_form, container, false);

        // Inicializa os campos e botões
        edtNomeTreino = view.findViewById(R.id.edtNomeTreino);
        ckbDom = view.findViewById(R.id.ckbDom);
        ckbSeg = view.findViewById(R.id.ckbSeg);
        ckbTer = view.findViewById(R.id.ckbTer);
        ckbQua = view.findViewById(R.id.ckbQua);
        ckbQui = view.findViewById(R.id.ckbQui);
        ckbSex = view.findViewById(R.id.ckbSex);
        ckbSab = view.findViewById(R.id.ckbSab);
        btnSalvar = view.findViewById(R.id.btnSalvar);

        // Obter o código da lista de treino (deve ser passado como argumento para o fragment)
        if (getArguments() != null) {
            codigoListaTreino = getArguments().getString("codigoListaTreino", "");
        }

        // Inicializa o Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference("Treinos");

        // Configura o botão salvar
        btnSalvar.setOnClickListener(v -> salvarTreino());

        return view;
    }

    private void salvarTreino() {
        String nomeTreino = edtNomeTreino.getText().toString().trim();

        // Valida o nome do treino
        if (nomeTreino.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, insira o nome do treino", Toast.LENGTH_SHORT).show();
            return;
        }

        // Captura o dia selecionado
        String diaSelecionado = "";

        if (ckbDom.isChecked()) {
            diaSelecionado = "Domingo";
        } else if (ckbSeg.isChecked()) {
            diaSelecionado = "Segunda-feira";
        } else if (ckbTer.isChecked()) {
            diaSelecionado = "Terça-feira";
        } else if (ckbQua.isChecked()) {
            diaSelecionado = "Quarta-feira";
        } else if (ckbQui.isChecked()) {
            diaSelecionado = "Quinta-feira";
        } else if (ckbSex.isChecked()) {
            diaSelecionado = "Sexta-feira";
        } else if (ckbSab.isChecked()) {
            diaSelecionado = "Sábado";
        }

        if (diaSelecionado.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, selecione um dia da semana", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cria o objeto Treino
        Treino treino = new Treino(nomeTreino, diaSelecionado, codigoListaTreino);

        // Salva no Firebase
        databaseRef.push().setValue(treino).addOnSuccessListener(aVoid -> {
            Toast.makeText(getContext(), "Treino salvo com sucesso!", Toast.LENGTH_SHORT).show();
            // Opcional: limpar campos após salvar
            edtNomeTreino.setText("");
            ckbDom.setChecked(false);
            ckbSeg.setChecked(false);
            ckbTer.setChecked(false);
            ckbQua.setChecked(false);
            ckbQui.setChecked(false);
            ckbSex.setChecked(false);
            ckbSab.setChecked(false);
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Erro ao salvar o treino: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    // Classe para representar os dados do treino
    public static class Treino {
        private String nomeTreino;
        private String diaTreino;
        private String listaDeTreino;

        public Treino() {
            // Construtor vazio necessário para o Firebase
        }

        public Treino(String nomeTreino, String diaTreino, String listaDeTreino) {
            this.nomeTreino = nomeTreino;
            this.diaTreino = diaTreino;
            this.listaDeTreino = listaDeTreino;
        }

        public String getNomeTreino() {
            return nomeTreino;
        }

        public String getDiaTreino() {
            return diaTreino;
        }

        public String getListaDeTreino() {
            return listaDeTreino;
        }
    }
}