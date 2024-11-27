package com.example.academiatcc.telasaluno;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.academiatcc.R;
import com.example.academiatcc.adapters.DiaDaSemanaTreinoAdapter;
import com.example.academiatcc.adapters.ExerciciosPersonalAdapter;
import com.example.academiatcc.dataclass.Exercicio;
import com.example.academiatcc.dataclass.Treino;
import com.example.academiatcc.telaspersonal.TelaAlunoPersonalActivity;
import com.example.academiatcc.telaspersonal.TreinosAlunoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ExerciciosFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Treino> treinos;
    DiaDaSemanaTreinoAdapter diaDaSemanaTreinoAdapter;
    FirebaseFirestore db;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercicios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        recyclerView = view.findViewById(R.id.idRecyclerViewDiasDaSemana);
        db = FirebaseFirestore.getInstance();
        treinos = new ArrayList<>();
        diaDaSemanaTreinoAdapter = new DiaDaSemanaTreinoAdapter(treinos, getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(diaDaSemanaTreinoAdapter);

        EventChangeListener();

        diaDaSemanaTreinoAdapter.setOnItemClickListener(new DiaDaSemanaTreinoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Treino treino) {
                Intent intent = new Intent(getContext(), TreinosAlunoActivity.class);
                intent.putExtra("ID_ALUNO", userId);
                intent.putExtra("ID_TREINO", treino.getId());
                startActivity(intent);
            }
        });
    }

    private void EventChangeListener() {
        db.collection("Treinos").whereEqualTo("alunoId", userId).orderBy("diaDaSemanaNumero").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Error", "Error: " + error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        Treino treino = dc.getDocument().toObject(Treino.class);
                        treino.setId(dc.getDocument().getId());
                        treinos.add(treino);
                    }
                }
                diaDaSemanaTreinoAdapter.notifyDataSetChanged();
            }
        });
    }

}