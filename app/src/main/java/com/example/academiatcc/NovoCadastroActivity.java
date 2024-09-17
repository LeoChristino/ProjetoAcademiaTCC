package com.example.academiatcc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NovoCadastroActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextInputLayout emailEditText;
    private TextInputLayout passwordEditText;
    private TextInputLayout confirmarSenhaEditText;
    private TextInputLayout nomeEditText;
    private TextInputLayout celularEditText;
    private RadioGroup radioGroupSexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novo_cadastro_layout); // Substitua pelo nome do seu layout

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.txtEmailCadastro);
        passwordEditText = findViewById(R.id.txtSenhaCadastro);
        confirmarSenhaEditText = findViewById(R.id.txtConfirmaSenhaCadastro);
        nomeEditText = findViewById(R.id.txtNomeCadastro);
        celularEditText = findViewById(R.id.txtCelCadastro);
        radioGroupSexo = findViewById(R.id.radiogroupSexoCadastro);
        Button cadastroButton = findViewById(R.id.btnCadastrar);

        cadastroButton.setOnClickListener(view -> {
                String email = emailEditText.getEditText().getText().toString();
                String password = passwordEditText.getEditText().getText().toString();
                String confirmarSenha = confirmarSenhaEditText.getEditText().getText().toString();
                String nome = nomeEditText.getEditText().getText().toString();
                String celular = celularEditText.getEditText().getText().toString();

            // Validação básica (adicione mais validações conforme necessário)
            if (email.isEmpty() || password.isEmpty() || nome.isEmpty() || celular.isEmpty()) {
                Toast.makeText(NovoCadastroActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }
            // Obter o sexo selecionado
            int selectedId = radioGroupSexo.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(NovoCadastroActivity.this, "Selecione o sexo", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton radioButton =findViewById(selectedId);
            String sexo = radioButton.getText().toString();
            if (!password.equals(confirmarSenha)) {
                Toast.makeText(NovoCadastroActivity.this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = auth.getCurrentUser();
                        salvarDadosUsuario(user.getUid(), email, nome, celular, sexo);

                        Toast.makeText(NovoCadastroActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NovoCadastroActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(NovoCadastroActivity.this, "Erro ao criar conta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void salvarDadosUsuario(String userId, String nome, String email, String celular, String sexo) {
        Map<String, Object> user = new HashMap<>();
        user.put("nome", nome);
        user.put("email", email);
        user.put("celular", celular);
        user.put("sexo", sexo);

        db.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    // Dados salvos com sucesso
                })
                .addOnFailureListener(e -> {
                    // Erro ao salvar dados - Trate o erro de forma mais robusta
                    Toast.makeText(NovoCadastroActivity.this, "Erro ao salvar dados do usuário", Toast.LENGTH_SHORT).show();
                });
    }
}