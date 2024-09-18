package br.com.etecia.academiatcc;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.academiatcc.R;

public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cadastro_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RadioGroup radioGroup = findViewById(R.id.radiogrougTipo);
        com.google.android.material.textfield.TextInputLayout componenteEscondido = findViewById(R.id.txtCref);

        radioGroup.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            if (checkedId == R.id.rbPersonal) {
                componenteEscondido.setVisibility(View.VISIBLE);
            } else {
                componenteEscondido.setVisibility(View.GONE);
            }
        });
    }
}