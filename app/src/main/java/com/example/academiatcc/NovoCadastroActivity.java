package com.example.academiatcc;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NovoCadastroActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    TextInputLayout editTextCodPers, editTextNome, editTextEmail, editTextSenha, editTextCelular, editTextConfirmarSenha;
    RadioGroup radioGroupSexo, radioGroupTipoCadastro;
    Button btnCadastrar;

    List<Aluno> alunos = new ArrayList<>();
    boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novo_cadastro_layout);

        editTextCodPers = findViewById(R.id.txtIdPersonalCadastro);
        editTextNome = findViewById(R.id.txtNomeCadastro);
        editTextEmail = findViewById(R.id.txtEmailCadastro);
        editTextSenha = findViewById(R.id.txtSenhaCadastro);
        editTextConfirmarSenha = findViewById(R.id.txtConfirmaSenhaCadastro);
        editTextCelular = findViewById(R.id.txtCelCadastro);
        radioGroupSexo = findViewById(R.id.radiogroupSexoCadastro);
        radioGroupTipoCadastro = findViewById(R.id.radiogroupTipoCadastro);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAluno();
            }
        });
    }

    private void createAluno() {
        String nome = editTextNome.getEditText().getText().toString();
        String email = editTextEmail.getEditText().getText().toString();
        String celular = editTextCelular.getEditText().getText().toString();
        String sexo = ((RadioButton) findViewById(radioGroupSexo.getCheckedRadioButtonId())).getText().toString();
        String senha = editTextSenha.getEditText().getText().toString();
        String codPers = editTextCodPers.getEditText().getText().toString();

        if (TextUtils.isEmpty(nome)) {
            editTextNome.setError("Nome é obrigatório");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email é obrigatório");
            return;
        }
        if (TextUtils.isEmpty(celular)) {
            editTextCelular.setError("Celular é obrigatório");
            return;
        }
        if (TextUtils.isEmpty(sexo)) {
            Toast.makeText(this, "Sexo é obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(codPers)) {
            editTextCodPers.setError("Código do Personal é obrigatório");
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("nome", nome);
        params.put("email", email);
        params.put("celular", celular);
        params.put("sexo", sexo);
        params.put("codPers", codPers);

        PerformNetworkRequest request = new PerformNetworkRequest(ApiAPPTCC.URL_CREATE_ALUNO, params, CODE_POST_REQUEST);
        request.execute();
    }


    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

}