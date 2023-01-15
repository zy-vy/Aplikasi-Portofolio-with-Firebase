package com.binus.aplikasiportofolio.Repo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.binus.aplikasiportofolio.Goal.Goal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FirebaseRepo {
    private final String TAG = "+++REPO";

    private static FirebaseRepo instance = null;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference goalRef = db.collection("goal");
    CollectionReference milestoneRef = db.collection("milestone");

    private FirebaseRepo() {
    }

    public static FirebaseRepo getInstance() {
        if(instance == null) {
            instance = new FirebaseRepo();
        }
        return instance;
    }

    public List<Goal> getAllGoal(){
        List<Goal> list = new ArrayList<>();
        goalRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (!snapshot.isEmpty()) {
                        Log.d(TAG, "DocumentSnapshot data: " + snapshot.size());
                        snapshot.getDocuments().forEach(documentSnapshot -> {
                            list.add(Goal.fromDocument(documentSnapshot.getId(), Objects.requireNonNull(documentSnapshot.getData())));
                        });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return list;
    }

}
