package com.example.academiatcc.geral;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.academiatcc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PerfilFragment extends Fragment {

    private TextView nomeUsuario;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        nomeUsuario = view.findViewById(R.id.txtNomePerfil);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        btnLogout = view.findViewById(R.id.sair);

        btnLogout.setOnClickListener(v -> logout());
        fetchAndDisplayUserName();

        return view;
    }

    private void fetchAndDisplayUserName() {
        FirebaseUser usuario = auth.getCurrentUser();
        if (usuario != null) {
            String userId = usuario.getUid();
            fetchUserNameFromCollection("Alunos", userId);
        }
    }

    private void fetchUserNameFromCollection(String collectionName, String userId) {
        DocumentReference userRef = firestore.collection(collectionName).document(userId);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String userName = document.getString("nome");
                    nomeUsuario.setText(userName != null ? userName : "Nome não encontrado");
                } else if (collectionName.equals("Alunos")) {
                    fetchUserNameFromCollection("Personais", userId);
                } else {
                    nomeUsuario.setText("Nome não encontrado");
                }
            }
        });
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}