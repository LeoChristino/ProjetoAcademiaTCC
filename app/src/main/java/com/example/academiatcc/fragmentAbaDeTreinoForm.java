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

import java.util.HashMap;

public class fragmentAbaDeTreinoForm extends Fragment {

    private EditText etNomeTreino;
    private CheckBox ckbDom, ckbSeg, ckbTer, ckbQua, ckbQui, ckbSex, ckbSab;
    private Button btnSalvar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aba_add_treino_dia_form, container, false);
        // Inicializar os componentes
        etNomeTreino = view.findViewById(R.id.NomeDaLista);
        ckbDom = view.findViewById(R.id.ckbDom);
        ckbSeg = view.findViewById(R.id.ckbSeg);
        ckbTer = view.findViewById(R.id.ckbTer);
        ckbQua = view.findViewById(R.id.ckbQua);
        ckbQui = view.findViewById(R.id.ckbQui);
        ckbSex = view.findViewById(R.id.ckbSex);
        ckbSab = view.findViewById(R.id.ckbSab);
        btnSalvar = view.findViewById(R.id.buttonSalvar);

        btnSalvar.setOnClickListener(v -> salvarTreino());

        return view;
    }

    private void salvarTreino() {
        String nomeTreino = etNomeTreino.getText().toString();
        HashMap<String, Boolean> diasSelecionados = new HashMap<>();
        diasSelecionados.put("Domingo", ckbDom.isChecked());
        diasSelecionados.put("Segunda-feira", ckbSeg.isChecked());
        diasSelecionados.put("Terça-feira", ckbTer.isChecked());
        diasSelecionados.put("Quarta-feira", ckbQua.isChecked());
        diasSelecionados.put("Quinta-feira", ckbQui.isChecked());
        diasSelecionados.put("Sexta-feira", ckbSex.isChecked());
        diasSelecionados.put("Sábado", ckbSab.isChecked());

        if (nomeTreino.isEmpty()) {
            Toast.makeText(getContext(), "Preencha o nome do treino!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("treinos");
        String treinoId = databaseReference.push().getKey();

        HashMap<String, Object> treinoData = new HashMap<>();
        treinoData.put("nome", nomeTreino);
        treinoData.put("dias", diasSelecionados);

        databaseReference.child(treinoId).setValue(treinoData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Treino salvo com sucesso!", Toast.LENGTH_SHORT).show();
                        // Navegar para outro fragment ou atualizar a interface
                    } else {
                        Toast.makeText(getContext(), "Erro ao salvar treino!", Toast.LENGTH_SHORT).show();
                    }
                });
    // TODO: Rename and change types and number of parameters
    public static fragmentAbaDeTreinoForm newInstance(String param1, String param2) {
        fragmentAbaDeTreinoForm fragment = new fragmentAbaDeTreinoForm();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aba_de_treino_form, container, false);
    }
}