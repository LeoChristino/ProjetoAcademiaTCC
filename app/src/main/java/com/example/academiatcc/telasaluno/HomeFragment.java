package com.example.academiatcc.telasaluno;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class HomeFragment extends Fragment {

    TextView nomeAluno, nomePersonal, seuPersonal;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        seuPersonal = view.findViewById(R.id.txtSeuPersonal);
        nomePersonal = view.findViewById(R.id.txtNomePersonal);
        nomeAluno = view.findViewById(R.id.txtBemVindo);
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
                        DocumentSnapshot alunoDoc = task.getResult();
                        if (alunoDoc.exists()) {
                            String userName = alunoDoc.getString("nome");
                            String idPersonal = alunoDoc.getString("idPersonal");
                            DocumentReference personalRef = firestore.collection("Personais").document(idPersonal);
                            personalRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot personalDoc = task.getResult();
                                        if (personalDoc.exists()) {
                                            String personalName = personalDoc.getString("nome");
                                            SpannableString spannable2 = new SpannableString(personalName);
                                            int startIndex = personalName.indexOf(personalName);
                                            int endIndex = startIndex + personalName.length();
                                            int color = Color.parseColor("#BF0426");
                                            seuPersonal.setVisibility(View.VISIBLE);
                                            nomePersonal.setVisibility(View.VISIBLE);
                                            spannable2.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            spannable2.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            nomePersonal.setText(spannable2);
                                        }
                                    }
                                }
                            });
                            if (userName != null) {
                                String text = "Bem vindo(a), " + userName + "!";
                                SpannableString spannable = new SpannableString(text);

                                int startIndex = text.indexOf(userName);
                                int endIndex = startIndex + userName.length();
                                int color = Color.parseColor("#BF0426");
                                spannable.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                nomeAluno.setText(spannable);
                            } else {
                                nomeAluno.setText("Bem vindo!");
                            }
                        }
                    }
                }
            });
        }
        return view;
    }
}