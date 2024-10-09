package com.example.academiatcc;

import android.annotation.SuppressLint;
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
    private RadioGroup radioGroupTipo;

    @SuppressLint("WrongViewCast")
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
        radioGroupTipo = findViewById(R.id.radiogroupTipoCadastro);
        Button cadastroButton = findViewById(R.id.btnCadastrar);

        cadastroButton.setOnClickListener(view -> {
            String email = emailEditText.getEditText().getText().toString();
            String password = passwordEditText.getEditText().getText().toString();
            String confirmarSenha = confirmarSenhaEditText.getEditText().getText().toString();
            String nome = nomeEditText.getEditText().getText().toString();
            String celular = celularEditText.getEditText().getText().toString();
            //-----------------------------------------------------------------------------------------
            // obter o tipo de cadastro selecionado
            int TipoSelecionadoId = radioGroupTipo.getCheckedRadioButtonId();
            if (TipoSelecionadoId == -1) {
                Toast.makeText(NovoCadastroActivity.this, "Selecione o tipo de cadastro", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton radioButtonTipo = findViewById(TipoSelecionadoId);
            String tipo = radioButtonTipo.getText().toString();
            //-----------------------------------------------------------------------------------------
            // Obter o sexo selecionado
            int SexoSelecionadoId = radioGroupSexo.getCheckedRadioButtonId();
            if (SexoSelecionadoId == -1) {
                Toast.makeText(NovoCadastroActivity.this, "Selecione o sexo", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton radioButtonSexo = findViewById(SexoSelecionadoId);
            String sexo = radioButtonSexo.getText().toString();
            //-----------------------------------------------------------------------------------------
            if (!password.equals(confirmarSenha)) {
                Toast.makeText(NovoCadastroActivity.this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                return;
            }
            //-----------------------------------------------------------------------------------------
            // Validação básica (adicione mais validações conforme necessário)
            if (email.isEmpty() || password.isEmpty() || nome.isEmpty() || celular.isEmpty()) {
                Toast.makeText(NovoCadastroActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }
            //-----------------------------------------------------------------------------------------
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                FirebaseUser user = auth.getCurrentUser();
                salvarDadosUsuario(user.getUid(),tipo, email, nome, celular, sexo);

                Toast.makeText(NovoCadastroActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NovoCadastroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(NovoCadastroActivity.this, "Erro ao criar conta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void salvarDadosUsuario(String userId, String tipo,String email, String nome, String celular, String sexo) {
        // Salvar dados do usuário no Firestore
        Map<String, Object> user = new HashMap<>();
        user.put("tipo", tipo);
        user.put("email", email);
        user.put("nome", nome);
        user.put("celular", celular);
        user.put("sexo", sexo);
        if (tipo.equals("Aluno")) {
        db.collection("Alunos").document(userId).set(user).addOnSuccessListener(aVoid -> {
            // Dados salvos com sucesso
        }).addOnFailureListener(e -> {
            // Erro ao salvar dados - Trate o erro de forma mais robusta
            Toast.makeText(NovoCadastroActivity.this, "Erro ao salvar dados do usuário", Toast.LENGTH_SHORT).show();
        });
        } else{db.collection("Personais").document(userId).set(user).addOnSuccessListener(aVoid -> {
            // Dados salvos com sucesso
        }).addOnFailureListener(e -> {
            // Erro ao salvar dados - Trate o erro de forma mais robusta
            Toast.makeText(NovoCadastroActivity.this, "Erro ao salvar dados do usuário", Toast.LENGTH_SHORT).show();
        });

        }
    }
}