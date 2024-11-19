package com.example.academiatcc;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CriarExercicioActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    TextInputLayout txtNomeExercicio, txtDescExercicio, txtLinkVideoExercicio;
    StorageReference storageReference;
    Uri imagem;
    Button selecionarImagem, cadastrarExercicio;
    ImageView imageView;
    String grupoID, grupoMuscular;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    selecionarImagem.setEnabled(true);
                    imagem = result.getData().getData();
                    Glide.with(getApplicationContext()).load(imagem).into(imageView);
                }
            } else {
                Toast.makeText(CriarExercicioActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.criar_exercicio_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FirebaseApp.initializeApp(CriarExercicioActivity.this);
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        txtNomeExercicio = findViewById(R.id.txtNomeExercicio);
        txtDescExercicio = findViewById(R.id.txtDescExercicio);
        txtLinkVideoExercicio = findViewById(R.id.txtVideoExercicio);
        imageView = findViewById(R.id.imgExercicio);
        selecionarImagem = findViewById(R.id.btnEnviarFotoExercicio);
        cadastrarExercicio = findViewById(R.id.btnCadastrarExercicio);

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference subjectsRef = rootRef.collection("GrupoMuscular");
        Spinner spinner = (Spinner) findViewById(R.id.spinnerGrupoMuscular);
        List<String> GrupoMuscular = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, GrupoMuscular);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        subjectsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String grupos = document.getString("Nome");
                        GrupoMuscular.add(grupos);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGrupoMuscular = parent.getItemAtPosition(position).toString();
                subjectsRef.whereEqualTo("Nome", selectedGrupoMuscular).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String grupo = document.getString("Nome");
                                String documentId = document.getId();
                                grupoID = documentId;
                                grupoMuscular = grupo;
                            }
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CriarExercicioActivity.this, "Selecione um grupo muscular!", Toast.LENGTH_SHORT).show();
            }
        });

        selecionarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });

        cadastrarExercicio.setOnClickListener(view -> {
            String nomeExercicio = txtNomeExercicio.getEditText().getText().toString();
            String descExercicio = txtDescExercicio.getEditText().getText().toString();
            String linkVideoExercicio = txtLinkVideoExercicio.getEditText().getText().toString();
            if (nomeExercicio.isEmpty()) {
                Toast.makeText(CriarExercicioActivity.this, "Preencha o campo nome!", Toast.LENGTH_SHORT).show();
                txtNomeExercicio.requestFocus();
                return;
            }
            if (descExercicio.isEmpty()) {
                Toast.makeText(CriarExercicioActivity.this, "Preencha o campo descrição!", Toast.LENGTH_SHORT).show();
                txtDescExercicio.requestFocus();
                return;
            }
            if (linkVideoExercicio.isEmpty()) {
                Toast.makeText(CriarExercicioActivity.this, "Preencha o campo link do video!", Toast.LENGTH_SHORT).show();
                txtLinkVideoExercicio.requestFocus();
                return;
            }
            if (imagem == null) {
                Toast.makeText(CriarExercicioActivity.this, "Selecione uma imagem!", Toast.LENGTH_SHORT).show();
                return;
            }
            auth = FirebaseAuth.getInstance();
            FirebaseUser personal = auth.getCurrentUser();
            criarExercicio(personal.getUid(), nomeExercicio, descExercicio, linkVideoExercicio, grupoID, grupoMuscular,imagem);
        });
    }

    private void cadastrarExercicio(Uri file, String exercicioId) {
        StorageReference ref = storageReference.child("CapaExercicio/" + exercicioId + ".jpg");
        ref.putFile(file).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(CriarExercicioActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(CriarExercicioActivity.this, "Failed!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void criarExercicio(String personalId, String nome, String desc, String linkVideo,String idGrupoMuscular, String grupoMuscular,Uri file) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> exercicio = new HashMap<>();
        exercicio.put("personalId", personalId);
        exercicio.put("nome", nome);
        exercicio.put("descricao", desc);
        exercicio.put("grupoMuscularID", idGrupoMuscular);
        exercicio.put("grupoMuscular", grupoMuscular);
        exercicio.put("linkVideo", linkVideo);

        db.collection("Exercicios").add(exercicio).addOnSuccessListener(documentReference -> {
            String exercicioId = documentReference.getId();
            cadastrarExercicio(file, exercicioId);
            Toast.makeText(CriarExercicioActivity.this, "Exercicio criado com sucesso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CriarExercicioActivity.this, HomePersonalActivity.class);
            startActivity(intent);
        }).addOnFailureListener(e -> {
            Toast.makeText(CriarExercicioActivity.this, "Erro ao criar exercicio", Toast.LENGTH_SHORT).show();
        });
        Fragment fragment = new HomeFragment();
    }
}