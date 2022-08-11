package com.example.examfeverr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class manageotp extends AppCompatActivity {


    String phnnumber;
    EditText t2;
    MaterialButton btn2;
    private String verificationId;
    String userId;

    FirebaseAuth fAuth;
    FirebaseUser firebaseUser;

    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageotp);

//        getSupportActionBar().hide();


        t2 = (EditText) findViewById(R.id.t2);
        btn2 =  findViewById(R.id.btn_2);




        fAuth = FirebaseAuth.getInstance();
        firebaseUser = fAuth.getCurrentUser();
        fstore = FirebaseFirestore.getInstance();



        verificationId = getIntent().getStringExtra("verificationId");




        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(t2.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Enter otp", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(verificationId != null);
                    {
                        String code = t2.getText().toString().trim();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

                        fAuth.signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    userId = fAuth.getCurrentUser().getUid();

                                    DocumentReference docRef = fstore.collection("users").document(userId);

                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Intent intent = new Intent(manageotp.this, Dashboard.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);


                                                }
                                                else {
                                                    Intent intent = new Intent(manageotp.this, Register.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intent.putExtra("mobile", phnnumber);
                                                    startActivity(intent);
                                            }
                                            }
                                            else
                                                Toast.makeText(manageotp.this, "Error occured", Toast.LENGTH_SHORT).show();
                                        }
                                    });





                                }
                                else {
                                    Toast.makeText(manageotp.this, "INVALID OTP", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }

            }
        });







    }

}