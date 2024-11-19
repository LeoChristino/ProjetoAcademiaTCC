package com.example.academiatcc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class fragmentAbaDeTreinoForm extends Fragment {

    private EditText edtNomeLista, edtObs, edtObj;
    private Button btnSalvarLista;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aba_de_treino_form, container, false);

        // Inicializa os componentes
        edtNomeLista = view.findViewById(R.id.edtNomeLista);
        edtObs = view.findViewById(R.id.edtObs);
        edtObj = view.findViewById(R.id.edtObj);
        btnSalvarLista = view.findViewById(R.id.btnSalvarLista);

        // Inicializa o Firestore e FirebaseAuth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Configura o clique do botão
        btnSalvarLista.setOnClickListener(v -> salvarListaDeTreino());

        return view;
    }

    private void salvarListaDeTreino() {
        // Coleta os dados do usuário
        String nomeDaLista = edtNomeLista.getText().toString().trim();
        String observacao = edtObs.getText().toString().trim();
        String objetivo = edtObj.getText().toString().trim();

        // Obtém o UID do personal autenticado
        String uidPersonal = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (nomeDaLista.isEmpty() || objetivo.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (uidPersonal == null) {
            Toast.makeText(getContext(), "Erro: Usuário não autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cria o mapa para salvar no Firestore
        Map<String, Object> listaDeTreino = new HashMap<>();
        listaDeTreino.put("Nome", nomeDaLista);
        listaDeTreino.put("Observacao", observacao);
        listaDeTreino.put("Objetivo", objetivo);
        listaDeTreino.put("PersonalUID", uidPersonal); // Associa o UID do personal

        // Salva no Firestore
        db.collection("ListaDeTreinos")
                .add(listaDeTreino)
                .addOnSuccessListener(documentReference -> {
                    String codigoLista = documentReference.getId(); // ID gerado automaticamente

                    // Navega para o próximo fragment automaticamente
                    Fragment fragmentAbaDeTreinoForm = new fragmentAbaDeTreinoForm();
                    Bundle args = new Bundle();
                    args.putString("codigoLista", codigoLista); // Passa o ID da lista para o próximo fragment
                    fragmentAbaDeTreinoForm.setArguments(args);

                    // Transição de fragment
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragmentAbaDeTreinoForm)
                            .addToBackStack(null)
                            .commit();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Erro ao criar lista: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
