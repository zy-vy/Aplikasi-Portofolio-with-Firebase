package com.binus.aplikasiportofolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText txtEmail, txtPassword;
    Button btnReg;
    ProgressBar progressBar;
    TextView txtLoginNow;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        txtEmail = findViewById(R.id.email);
        txtPassword = findViewById(R.id.password);
        btnReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progress_bar);
        txtLoginNow = findViewById(R.id.login_now);

        txtLoginNow.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnReg.setOnClickListener(view -> {
            btnReg.setClickable(false);
            progressBar.setVisibility(View.VISIBLE);
            String email,password;
            email = String.valueOf(txtEmail.getText());
            password = String.valueOf(txtPassword.getText());

            if (email.isEmpty()||password.isEmpty()){
                Toast.makeText(this, "Input email and password", Toast.LENGTH_SHORT).show();
                btnReg.setClickable(true);
                progressBar.setVisibility(View.GONE);
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            btnReg.setClickable(true);
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                Log.d("+++REGISTER", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

//                                Map<String,Object> userObj = new HashMap<>();
//                                userObj.put("user", user.getUid());
//                                FirebaseFirestore.getInstance().collection("goal").document(user.getUid()).set(userObj, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//
//                                        if (task.isSuccessful()){
//                                        }
//                                        else {
//                                            Log.w("+++REGISTER", "createFirestore:failure", task.getException());
//                                            Toast.makeText(RegisterActivity.this, "Firestore failed.",
//                                                    Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("+++REGISTER", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        });

    }
}