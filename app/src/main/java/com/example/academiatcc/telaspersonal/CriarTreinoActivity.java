package com.example.academiatcc.telaspersonal;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.academiatcc.R;
import com.example.academiatcc.adapters.ExerciciosPersonalAdapter;
import com.example.academiatcc.dataclass.Exercicio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CriarTreinoActivity extends AppCompatActivity {

    private EditText txtNumSeries, txtNumRepeticoes;
    private FirebaseAuth auth;
    private int numSeries = 0, numRepeticoes = 0;
    private final int minValue = 0;
    private final int maxValue = 15;
    RecyclerView recyclerView;
    ArrayList<Exercicio> exercicios;
    ExerciciosPersonalAdapter exerciciosPersonalAdapter;
    FirebaseFirestore db;
    FirebaseStorage storage;
    String grupoMuscular;
    String[] diasDaSemana = {"Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado", "Domingo"};
    String nomeExercicio;
    Button btnAdicionarExercicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.criar_treino_layout);

        txtNumSeries = findViewById(R.id.txtNumSeries);
        txtNumRepeticoes = findViewById(R.id.txtNumRepeticoes);
        recyclerView = findViewById(R.id.idRecyclerViewExercicios);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        exercicios = new ArrayList<>();
        exerciciosPersonalAdapter = new ExerciciosPersonalAdapter(exercicios, CriarTreinoActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CriarTreinoActivity.this));
        recyclerView.setAdapter(exerciciosPersonalAdapter);


        txtNumSeries.setText(String.valueOf(numSeries));
        txtNumRepeticoes.setText(String.valueOf(numRepeticoes));
        findViewById(R.id.btn_menos1).setOnClickListener(v -> diminuirSeries());
        findViewById(R.id.btn_mais1).setOnClickListener(v -> aumentarSeries());
        findViewById(R.id.btn_menos2).setOnClickListener(v -> diminuirRepeticoes());
        findViewById(R.id.btn_mais2).setOnClickListener(v -> aumentarRepeticoes());

        ArrayAdapter<String> diaDaSemanaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, diasDaSemana);
        diaDaSemanaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerDiaDaSemana = findViewById(R.id.spinnerDiaDaSemana);
        spinnerDiaDaSemana.setAdapter(diaDaSemanaAdapter);

        spinnerDiaDaSemana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String diaSelecionado = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner spinnerGrupoMuscular = findViewById(R.id.spinnerGrupoMuscular);
        List<String> grupoMuscularOptions = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, grupoMuscularOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupoMuscular.setAdapter(adapter);

        db.collection("GrupoMuscular").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    grupoMuscularOptions.add(document.getString("Nome"));
                }
                adapter.notifyDataSetChanged();
            }
        });

        spinnerGrupoMuscular.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGrupoMuscular = parent.getItemAtPosition(position).toString();
                db.collection("GrupoMuscular").whereEqualTo("Nome", selectedGrupoMuscular).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            grupoMuscular = document.getString("Nome");
                            exercicios.clear();
                            exerciciosPersonalAdapter.notifyDataSetChanged();
                            listaExercicios();
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CriarTreinoActivity.this, "Selecione um grupo muscular!", Toast.LENGTH_SHORT).show();
            }
        });

        exerciciosPersonalAdapter.setOnItemClickListener(new ExerciciosPersonalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Exercicio exercicio) {
                nomeExercicio = exercicio.getNome();
            }
        });
        String idAluno = getIntent().getStringExtra("ID_ALUNO");
        btnAdicionarExercicio = findViewById(R.id.btnAdicionarExercicio);
        btnAdicionarExercicio.setOnClickListener(View -> {
            auth = FirebaseAuth.getInstance();
            FirebaseUser personal = auth.getCurrentUser();
            adicionarExercicio(personal.getUid(), idAluno, spinnerDiaDaSemana.getSelectedItem().toString(), nomeExercicio, numSeries, numRepeticoes);
            numSeries = 0;
            numRepeticoes = 0;
            txtNumSeries.setText(String.valueOf(0));
            txtNumRepeticoes.setText(String.valueOf(0));
        });

    }

    private void diminuirSeries() {
        numSeries = Math.max(numSeries - 1, minValue);
        txtNumSeries.setText(String.valueOf(numSeries));
    }

    private void aumentarSeries() {
        numSeries = Math.min(numSeries + 1, maxValue);
        txtNumSeries.setText(String.valueOf(numSeries));
    }

    private void diminuirRepeticoes() {
        numRepeticoes = Math.max(numRepeticoes - 1, minValue);
        txtNumRepeticoes.setText(String.valueOf(numRepeticoes));
    }

    private void aumentarRepeticoes() {
        numRepeticoes = Math.min(numRepeticoes + 1, maxValue);
        txtNumRepeticoes.setText(String.valueOf(numRepeticoes));
    }

    private void listaExercicios() {
        db.collection("Exercicios").whereEqualTo("grupoMuscular", grupoMuscular).orderBy("nome", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Error", "Error: " + error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        Exercicio exercicio = dc.getDocument().toObject(Exercicio.class);
                        exercicio.setId(dc.getDocument().getId());
                        StorageReference imageRef = storage.getReference().child("CapaExercicio/" + exercicio.getId() + ".jpg");
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                exercicio.setUrl(uri.toString());
                                exercicios.add(exercicio);
                                exerciciosPersonalAdapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.e("Error", "Failed to get download URL: " + exception.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    private int getDayOfWeekNumber(String dayOfWeek) {
        Map<String, Integer> dayOfWeekMap = new HashMap<>();
        dayOfWeekMap.put("Domingo", 0);
        dayOfWeekMap.put("Segunda-feira", 1);
        dayOfWeekMap.put("Terça-feira", 2);
        dayOfWeekMap.put("Quarta-feira", 3);
        dayOfWeekMap.put("Quinta-feira", 4);
        dayOfWeekMap.put("Sexta-feira", 5);
        dayOfWeekMap.put("Sábado", 6);

        return dayOfWeekMap.getOrDefault(dayOfWeek, -1); // Return -1 if dayOfWeek is not found
    }

    private void adicionarExercicio(String personalId, String alunoId, String diaDaSemana, String nomeExercicio, int series, int repeticoes) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Treinos").whereEqualTo("alunoId", alunoId).whereEqualTo("diaDaSemana", diaDaSemana).whereEqualTo("personalId", personalId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    criarNovoTreino(personalId, alunoId, diaDaSemana, nomeExercicio, series, repeticoes);
                } else {
                    DocumentReference treinoRef = task.getResult().getDocuments().get(0).getReference();
                    adicionarExercicioAoTreino(treinoRef.getId(), nomeExercicio, series, repeticoes);
                }
            } else {
                Toast.makeText(CriarTreinoActivity.this, "Erro ao buscar treino: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void criarNovoTreino(String personalId, String alunoId, String diaDaSemana, String nomeExercicio, int series, int repeticoes) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> treinoData = new HashMap<>();
        treinoData.put("alunoId", alunoId);
        treinoData.put("diaDaSemana", diaDaSemana);
        treinoData.put("personalId", personalId);
        treinoData.put("diaDaSemanaNumero", getDayOfWeekNumber(diaDaSemana));

        db.collection("Treinos").add(treinoData).addOnSuccessListener(documentReference -> {
            adicionarExercicioAoTreino(documentReference.getId(), nomeExercicio, series, repeticoes);
        }).addOnFailureListener(e -> {
            Toast.makeText(CriarTreinoActivity.this, "Erro ao criar treino: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void adicionarExercicioAoTreino(String treinoId, String nomeExercicio, int series, int repeticoes) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> exercicioData = new HashMap<>();
        exercicioData.put("grupoMuscular", grupoMuscular); // Assuming grupoMuscular is defined somewhere
        exercicioData.put("nomeExercicio", nomeExercicio);
        exercicioData.put("series", series);
        exercicioData.put("repeticoes", repeticoes);
        exercicioData.put("treinoId", treinoId);

        // Changed collection name to "ExercicioTreino"
        db.collection("ExercicioTreino").add(exercicioData).addOnSuccessListener(aVoid -> {
            Toast.makeText(CriarTreinoActivity.this, "Exercício adicionado ao treino!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(CriarTreinoActivity.this, "Erro ao adicionar exercício ao treino: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}