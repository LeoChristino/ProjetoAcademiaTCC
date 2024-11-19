package com.example.academiatcc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListaAlunosPersonalFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Aluno> alunos;
    AlunosAdapter alunosAdapter;
    FirebaseFirestore db;
    FirebaseStorage storage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_alunos_personal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.idRecyclerViewAlunos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        alunos = new ArrayList<Aluno>();
        alunosAdapter = new AlunosAdapter(alunos, getContext());

        recyclerView.setAdapter(alunosAdapter);


        alunosAdapter.setOnItemClickListener(new AlunosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Aluno aluno) {
                String nomeAluno = aluno.getNome();
                String idAluno = aluno.getId(); // Obter o ID do aluno
                // Criar a Intent para a DetalhesActivity
                Intent intent = new Intent(getContext(), TelaAlunoPersonalActivity.class);
                intent.putExtra("NOME_ALUNO", nomeAluno);
                intent.putExtra("ID_ALUNO", idAluno); // Passar o ID do aluno
                startActivity(intent);
            }
        });

        EventChangeListener();
    }

    private void EventChangeListener() {

        db.collection("Alunos").orderBy("nome", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.e("Error", "Error: " + error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Aluno aluno = dc.getDocument().toObject(Aluno.class);
                                alunos.add(aluno);
                            }
                            alunosAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}