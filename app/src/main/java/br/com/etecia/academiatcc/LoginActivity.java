package br.com.etecia.academiatcc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    EditText txtUsuario,txtSenha;
    Button btnEntrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button botao = findViewById(R.id.btnCriarConta);
        botao.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });
        Button btnEntrar = findViewById(R.id.btnEntrar);
        EditText edtUsuario = findViewById(R.id.txtUsuario);
        EditText edtSenha = findViewById(R.id.txtSenha);

        btnEntrar.setOnClickListener(view -> {
            String usuario = edtUsuario.getText().toString();
            String senha = edtSenha.getText().toString();
            if (usuario.equals("admin") && senha.equals("admin")) {
                Intent intent2 = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent2);
            }
        });
    }
}