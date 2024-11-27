package com.example.academiatcc.telaspersonal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.academiatcc.R;
import com.example.academiatcc.adapters.ExerciciosPersonalAdapter;
import com.example.academiatcc.dataclass.Exercicio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ExerciciosPersonalFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Exercicio> exercicios;
    ExerciciosPersonalAdapter exerciciosPersonalAdapter;
    FirebaseFirestore db;
    FirebaseStorage storage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercicios_personal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.idRecyclerViewExercicios);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        exercicios = new ArrayList<Exercicio>();
        exerciciosPersonalAdapter = new ExerciciosPersonalAdapter(exercicios, getContext());

        recyclerView.setAdapter(exerciciosPersonalAdapter);

        EventChangeListener();

        FloatingActionButton btnCriarExercicio = view.findViewById(R.id.btnCriarExercicio);
        btnCriarExercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CriarExercicioActivity.class);
                startActivity(intent);
            }
        });
    }

    private void EventChangeListener() {
        db.collection("Exercicios").orderBy("nome", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Error", "Error: " + error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Exercicio exercicio = dc.getDocument().toObject(Exercicio.class);
                                exercicio.setId(dc.getDocument().getId());
                                storage = FirebaseStorage.getInstance();
                                StorageReference imageRef = storage.getReference().child("CapaExercicio/" + exercicio.getId() + ".jpg");
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        exercicio.setUrl(uri.toString());
                                        exercicios.add(exercicio);
                                        exerciciosPersonalAdapter.notifyDataSetChanged();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Log.e("Error", "Failed to get download URL: " + exception.getMessage());
                                    }
                                });
                            }
                        }
                    }
                });
    }
}