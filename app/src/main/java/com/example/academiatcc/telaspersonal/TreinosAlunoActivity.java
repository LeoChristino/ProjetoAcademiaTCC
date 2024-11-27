package com.example.academiatcc.telaspersonal;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.academiatcc.R;
import com.example.academiatcc.adapters.DiaDaSemanaTreinoAdapter;
import com.example.academiatcc.adapters.ExercicioTreinoAdapter;
import com.example.academiatcc.dataclass.ExercicioTreino;
import com.example.academiatcc.dataclass.Treino;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TreinosAlunoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExercicioTreinoAdapter adapter;
    private ArrayList<ExercicioTreino> exercicios;
    String treino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.treinos_aluno_layout);

        treino = getIntent().getStringExtra("ID_TREINO");
        recyclerView = findViewById(R.id.idRecyclerViewTreinos);
        exercicios = new ArrayList<>();
        adapter = new ExercicioTreinoAdapter(exercicios);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String treinoId = treino; // Replace with the actual treinoId

        db.collection("ExercicioTreino")
                .whereEqualTo("treinoId", treinoId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        ExercicioTreino exercicio = document.toObject(ExercicioTreino.class);
                        exercicios.add(exercicio);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.w("MainActivity", "Error getting documents.", e);
                });
    }
}