package com.example.examfeverr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Profile extends AppCompatActivity {

    ImageView img;
    ImageButton btn_back;
    TextView pr_name, pr_email, pr_phone;

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    String userid, name, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        getSupportActionBar().hide();

        btn_back = findViewById(R.id.profile_backbtn);
        img = (ImageButton) findViewById(R.id.profile_iconEdit);
        pr_name = (TextView)findViewById(R.id.profile_name);
        pr_email = (TextView)findViewById(R.id.profile_email);
        pr_phone = (TextView)findViewById(R.id.profile_mobile);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        userid = firebaseAuth.getCurrentUser().getUid();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Profile_edit.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });


        DocumentReference documentReference = db.collection("users").document(userid);


        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
              @Override
              public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
             if( error !=null)
             {
                 Toast.makeText(Profile.this, error.toString(), Toast.LENGTH_SHORT).show();

             }

             else
             {
                 name = value.getString("name");
                 email = value.getString("email");

                 pr_name.setText(name);
                 pr_email.setText(email);
                 pr_phone.setText("+91 "+value.getString("phoneNumber"));
             }





              }
          });


          /*      documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot!=null) {
                        name = documentSnapshot.getString("name");
                        email = documentSnapshot.getString("email");

                        pr_name.setText(name);
                        pr_email.setText(email);
                        pr_phone.setText("+91 "+documentSnapshot.getString("phoneNumber"));

                    }
                    else
                        Toast.makeText(Profile.this, "There's got to be some error", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(Profile.this, "There's an error", Toast.LENGTH_SHORT).show();
            }
        });

           */

    }
}