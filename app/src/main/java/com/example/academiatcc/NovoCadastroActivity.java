package com.example.academiatcc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NovoCadastroActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextInputLayout idPersonalEditText, crefPersonaltEditText, nomeEditText, emailEditText, celularEditText, passwordEditText, confirmarSenhaEditText;
    private RadioGroup radioGroupSexo, radioGroupTipo;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novo_cadastro_layout); // Substitua pelo nome do seu layout

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        idPersonalEditText = findViewById(R.id.txtIdPersonalCadastro);
        crefPersonaltEditText = findViewById(R.id.txtCrefPersonalCadastro);
        emailEditText = findViewById(R.id.txtEmailCadastro);
        passwordEditText = findViewById(R.id.txtSenhaCadastro);
        confirmarSenhaEditText = findViewById(R.id.txtConfirmaSenhaCadastro);
        nomeEditText = findViewById(R.id.txtNomeCadastro);
        celularEditText = findViewById(R.id.txtCelCadastro);
        radioGroupSexo = findViewById(R.id.radiogroupSexoCadastro);
        radioGroupTipo = findViewById(R.id.radiogroupTipoCadastro);
        Button cadastroButton = findViewById(R.id.btnCadastrar);
        //-----------------------------------------------------------------------------------------
        // Exibe ou oculta o campo de ID Personal com base no tipo de cadastro selecionado
        radioGroupTipo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbAlunoCadastro) { // Assuming radioButtonPersonal is the ID of your "Personal" radio button
                    idPersonalEditText.setVisibility(View.VISIBLE);
                    crefPersonaltEditText.setVisibility(View.GONE);
                } else {
                    crefPersonaltEditText.setVisibility(View.VISIBLE);
                    idPersonalEditText.setVisibility(View.GONE);
                }
            }
        });
        //-----------------------------------------------------------------------------------------
        // Configurar o clique do botão de cadastro
        cadastroButton.setOnClickListener(view -> {
            String idpersonal = idPersonalEditText.getEditText().getText().toString();
            String crefpersonal = crefPersonaltEditText.getEditText().getText().toString();
            String nome = nomeEditText.getEditText().getText().toString();
            String email = emailEditText.getEditText().getText().toString();
            String celular = celularEditText.getEditText().getText().toString();
            String password = passwordEditText.getEditText().getText().toString();
            String confirmarSenha = confirmarSenhaEditText.getEditText().getText().toString();


            // obter o tipo de cadastro selecionado
            int TipoSelecionadoId = radioGroupTipo.getCheckedRadioButtonId();
            if (TipoSelecionadoId == -1) {
                Toast.makeText(NovoCadastroActivity.this, "Selecione o tipo de cadastro", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton radioButtonTipo = findViewById(TipoSelecionadoId);
            String tipo = radioButtonTipo.getText().toString();

            if (tipo.equals("Aluno")) {
                if (idpersonal.isEmpty()) {
                    Toast.makeText(NovoCadastroActivity.this, "Preencha o campo IdPersonal!", Toast.LENGTH_SHORT).show();
                    idPersonalEditText.requestFocus();
                    return;
                }
            } else {
                if (crefpersonal.isEmpty()) {
                    Toast.makeText(NovoCadastroActivity.this, "Preencha o campo CREF!", Toast.LENGTH_SHORT).show();
                    crefPersonaltEditText.requestFocus();
                    return;
                }
            }
            // Validações básicas
            if (nome.isEmpty()) {
                Toast.makeText(NovoCadastroActivity.this, "Preencha o campo nome!", Toast.LENGTH_SHORT).show();
                nomeEditText.requestFocus();
                return;
            }
            if (email.isEmpty()) {
                Toast.makeText(NovoCadastroActivity.this, "Preencha o campo email!", Toast.LENGTH_SHORT).show();
                emailEditText.requestFocus();
                return;
            }
            if (password.isEmpty() || confirmarSenha.isEmpty()) {
                Toast.makeText(NovoCadastroActivity.this, "Preencha os campos de senha!", Toast.LENGTH_SHORT).show();
                confirmarSenhaEditText.requestFocus();
                return;
            }
            if (celular.isEmpty()) {
                Toast.makeText(NovoCadastroActivity.this, "Preencha o campo celular!", Toast.LENGTH_SHORT).show();
                celularEditText.requestFocus();
                return;
            }
            if (!password.equals(confirmarSenha)) {
                Toast.makeText(NovoCadastroActivity.this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                return;
            }


            // Obter o sexo selecionado
            int SexoSelecionadoId = radioGroupSexo.getCheckedRadioButtonId();
            if (SexoSelecionadoId == -1) {
                Toast.makeText(NovoCadastroActivity.this, "Selecione o sexo", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton radioButtonSexo = findViewById(SexoSelecionadoId);
            String sexo = radioButtonSexo.getText().toString();

            //-----------------------------------------------------------------------------------------
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                FirebaseUser user = auth.getCurrentUser();
                salvarDadosUsuario(user.getUid(), idpersonal, crefpersonal, tipo, email, nome, celular, sexo);

                Toast.makeText(NovoCadastroActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NovoCadastroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(NovoCadastroActivity.this, "Erro ao criar conta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void salvarDadosUsuario(String userId, String idpersonal, String crefpersonal, String tipo, String email, String nome, String celular, String sexo) {
        // Salvar dados do usuário no Firestore
        Map<String, Object> user = new HashMap<>();
        user.put("tipo", tipo);
        user.put("email", email);
        user.put("nome", nome);
        user.put("celular", celular);
        user.put("sexo", sexo);

        if (tipo.equals("Aluno")) {
            user.put("idPersonal", idpersonal);
            db.collection("Alunos").document(userId).set(user).addOnSuccessListener(aVoid -> {
                cadastroRealizado();
            }).addOnFailureListener(e -> {
                erroDeCadastro();
            });
        } else {
            user.put("cref", crefpersonal);
            db.collection("Personais").document(userId).set(user).addOnSuccessListener(aVoid -> {
                cadastroRealizado();
            }).addOnFailureListener(e -> {
                erroDeCadastro();
            });
        }
    }

    private void cadastroRealizado() {
        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
    }

    private void erroDeCadastro() {
        Toast.makeText(NovoCadastroActivity.this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
    }

}