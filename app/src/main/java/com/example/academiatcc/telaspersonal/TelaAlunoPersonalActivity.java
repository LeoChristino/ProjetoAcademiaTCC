package com.example.academiatcc.telaspersonal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.academiatcc.R;
import com.example.academiatcc.adapters.DiaDaSemanaTreinoAdapter;
import com.example.academiatcc.dataclass.Treino;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TelaAlunoPersonalActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Treino> treinos;
    DiaDaSemanaTreinoAdapter diaDaSemanaTreinoAdapter;
    FirebaseFirestore db;
    String idAluno;
    TextView txtNomeAluno, txtSexoAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_aluno_personal_layout);
        db = FirebaseFirestore.getInstance();
        idAluno = getIntent().getStringExtra("ID_ALUNO");
        String nomeAluno = getIntent().getStringExtra("NOME_ALUNO");
        String sexoAluno = getIntent().getStringExtra("SEXO_ALUNO");

        txtNomeAluno = findViewById(R.id.txtNomeAluno);
        txtSexoAluno = findViewById(R.id.txtSexoAluno);
        FloatingActionButton btnCriarExercicio = findViewById(R.id.btnCriarTreino);
        btnCriarExercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaAlunoPersonalActivity.this, CriarTreinoActivity.class);
                intent.putExtra("ID_ALUNO", idAluno);
                startActivity(intent);
            }
        });

        txtNomeAluno.setText(nomeAluno);
        txtSexoAluno.setText(sexoAluno);

        recyclerView = findViewById(R.id.idRecyclerViewDiasDaSemana);
        db = FirebaseFirestore.getInstance();
        treinos = new ArrayList<>();
        diaDaSemanaTreinoAdapter = new DiaDaSemanaTreinoAdapter(treinos, TelaAlunoPersonalActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TelaAlunoPersonalActivity.this));
        recyclerView.setAdapter(diaDaSemanaTreinoAdapter);

        EventChangeListener();

        diaDaSemanaTreinoAdapter.setOnItemClickListener(new DiaDaSemanaTreinoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Treino treino) {
                Intent intent = new Intent(TelaAlunoPersonalActivity.this, TreinosAlunoActivity.class);
                intent.putExtra("ID_ALUNO", idAluno);
                intent.putExtra("ID_TREINO", treino.getId());
                startActivity(intent);
            }
        });
    }

    private void EventChangeListener() {
        db.collection("Treinos").whereEqualTo("alunoId", idAluno).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Error", "Error: " + error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        Treino treino = dc.getDocument().toObject(Treino.class);
                        treino.setId(dc.getDocument().getId());
                        treinos.add(treino);
                    }
                }
                diaDaSemanaTreinoAdapter.notifyDataSetChanged();
            }
        });
    }
}