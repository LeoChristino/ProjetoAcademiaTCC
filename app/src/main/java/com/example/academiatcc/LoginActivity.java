package com.example.academiatcc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseFirestore firestore;
    private TextInputLayout txtEmailLogin;
    private TextInputLayout txtSenhaLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_layout);

        // Configuração do Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização do FirebaseAuth
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Referências para os elementos da interface do usuário
        txtEmailLogin = findViewById(R.id.txtEmailLogin); // Substitua pelo ID correto
        txtSenhaLogin = findViewById(R.id.txtSenhaLogin); // Substitua pelo ID correto
        Button loginbtn = findViewById(R.id.btnLogin); // Substitua pelo ID correto
        Button cadastrobtn = findViewById(R.id.btnCriarConta);

        // Listener para o botão de login
        loginbtn.setOnClickListener(view -> {
            String email = txtEmailLogin.getEditText().getText().toString();
            String senha = txtSenhaLogin.getEditText().getText().toString();

            // Validação básica
            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }


            auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Login bem-sucedido
                        FirebaseUser user = auth.getCurrentUser();
                        String userId = user.getUid();
                        DocumentReference alunoRef = firestore.collection("Alunos").document(userId);
                        alunoRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot alunoDoc = task.getResult();
                                    if (alunoDoc.exists()) {
                                        // Navegar para a próxima activity ou realizar outras ações
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        txtEmailLogin.getEditText().setText("");
                                        txtSenhaLogin.getEditText().setText("");
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Navegar para a próxima activity ou realizar outras ações
                                        Intent intent = new Intent(LoginActivity.this, HomePersonalActivity.class);
                                        txtEmailLogin.getEditText().setText("");
                                        txtSenhaLogin.getEditText().setText("");
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        });
                    } else {
                        // Tratar erro de login
                        Toast.makeText(LoginActivity.this, "Erro ao fazer login", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        // Listener para o botão de criar conta
        cadastrobtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, NovoCadastroActivity.class);
            startActivity(intent);
        });
    }
}