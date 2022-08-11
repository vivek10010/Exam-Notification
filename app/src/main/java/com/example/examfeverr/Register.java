package com.example.examfeverr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText e1, e2;
    Button btn_reg;
    ProgressBar progressBar;

    String userId;
    String phnnumber;

    FirebaseAuth fAuth;
    FirebaseUser firebaseUser;

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DocumentReference documentReference;
    DatabaseReference databaseReference;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userModel = new UserModel();

        progressBar = findViewById(R.id.reg_pb);

        e1 = (EditText) findViewById(R.id.name);
        e2 = (EditText) findViewById(R.id.email);


        btn_reg = (Button) findViewById(R.id.btn_reg);

        fAuth = FirebaseAuth.getInstance();
        firebaseUser = fAuth.getCurrentUser();
        userId = fAuth.getCurrentUser().getUid();

        phnnumber = getIntent().getStringExtra("mobile").trim();

        getSupportActionBar().hide();

        documentReference = fStore.collection("users").document(userId);
        databaseReference = database.getReference("All Users");


        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                uploadData();



            }
        });

    }

    private void uploadData() {
        String name = e1.getText().toString().trim();
        String email = e2.getText().toString().trim();

        if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email))
        {
            progressBar.setVisibility(View.VISIBLE);
            Map<String, String> user = new HashMap<>();

            user.put("name", name);
            user.put("email", email);
            user.put("phoneNumber", phnnumber);
            user.put("uid", userId);

            userModel.setName(name);
            userModel.setEmail(email);
            userModel.setPhoneNumber(phnnumber);
            userModel.setUid(userId);

            databaseReference.child(userId).setValue(userModel);
            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressBar.setVisibility(View.INVISIBLE);

                    Toast.makeText(getApplicationContext(), "User created successfully for userID " + userId, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Register.this, Dashboard.class);
                    startActivity(intent);
                }
            });
            }



        else
        {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
        }





    }
}