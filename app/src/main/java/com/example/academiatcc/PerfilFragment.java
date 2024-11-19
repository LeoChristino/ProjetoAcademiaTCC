package com.example.academiatcc;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.UserData;

public class PerfilFragment extends Fragment {
    TextView nomeUsuario;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        nomeUsuario = view.findViewById(R.id.txtNomePerfil);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        FirebaseUser usuario = auth.getCurrentUser();
        if (usuario != null) {
            String userId = usuario.getUid();
            DocumentReference userRef = firestore.collection("Alunos").document(userId);
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String userName = document.getString("nome");
                            if (userName != null) {
                                String text = userName;
                                nomeUsuario.setText(text);
                            } else {
                                nomeUsuario.setText("Nome não encontrado");
                            }
                        } else {
                            DocumentReference personalRef = firestore.collection("Personais").document(userId);
                            personalRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot personalDoc = task.getResult();
                                        if (personalDoc.exists()) {
                                            String personalName = personalDoc.getString("nome");
                                            if (personalName != null) {
                                                String text = personalName;
                                                nomeUsuario.setText(text);
                                            } else {
                                                nomeUsuario.setText("Nome não encontrado");
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
        return view;
    }
}