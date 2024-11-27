package com.example.academiatcc.telaspersonal;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class HomePersonalFragment extends Fragment {

    private TextView nomePersonal;
    private TextView txtSeuIdPersonal;
    private LinearLayout linearLayoutCopiarID;
    private TextView txtIdPersonal;
    private Button btnCopiar;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        nomePersonal = view.findViewById(R.id.txtBemVindo);
        txtSeuIdPersonal = view.findViewById(R.id.txtSeuIdPersonal);
        linearLayoutCopiarID = view.findViewById(R.id.LinearLayoutCopiarID);
        txtIdPersonal = view.findViewById(R.id.txtIdPersonal);
        btnCopiar = view.findViewById(R.id.btnCopiar);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        setupUserInfo();
        setupCopyIdButton();

        return view;
    }

    private void setupUserInfo() {
        FirebaseUser usuario = auth.getCurrentUser();

        if (usuario != null) {
            String idPersonal = usuario.getUid();
            DocumentReference userRef = firestore.collection("Personais").document(idPersonal);

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot personalDoc = task.getResult();
                        if (personalDoc.exists()) {
                            String userName = personalDoc.getString("nome");

                            linearLayoutCopiarID.setVisibility(View.VISIBLE);
                            txtSeuIdPersonal.setVisibility(View.VISIBLE);
                            txtIdPersonal.setText(idPersonal);

                            if (userName != null) {
                                String text = "Bem vindo(a), " + userName + "!";
                                SpannableString spannable = new SpannableString(text);
                                int startIndex = text.indexOf(userName);
                                int endIndex = startIndex + userName.length();
                                int color = Color.parseColor("#BF0426");

                                spannable.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

                                nomePersonal.setText(spannable);
                            } else {
                                nomePersonal.setText("Bem vindo!");
                            }
                        }
                    }
                }
            });
        }
    }

    private void setupCopyIdButton() {
        btnCopiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", txtIdPersonal.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(requireContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }
}