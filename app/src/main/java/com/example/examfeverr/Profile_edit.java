package com.example.examfeverr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile_edit extends AppCompatActivity {
    ImageButton ic_back;
    EditText textInputEditText_name, textInputEditText_email;
    MaterialButton update;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

//        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        uid = firebaseAuth.getCurrentUser().getUid();

        update = findViewById(R.id.button_update);

        textInputEditText_name = findViewById(R.id.txtinp_et_name);
        textInputEditText_email = findViewById(R.id.txtinp_et_email);



        textInputEditText_name.setText(getIntent().getStringExtra("name"));
        textInputEditText_email.setText(getIntent().getStringExtra("email"));

        ic_back = findViewById(R.id.profileEdit_backbtn);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("users").document(uid)
                        .update("name", textInputEditText_name.getText().toString().trim(),
                        "email", textInputEditText_email.getText().toString().trim())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Profile_edit.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Profile_edit.this, "Error occurred", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
    }
}