package com.example.examfeverr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class exam_page_layout extends AppCompatActivity {

    private StorageReference storageReference;

    public static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    FirebaseAuth firebaseAuth;



    Boolean fvrt_Checker = false;

    ImageButton saved;

    private ImageView imageView;
    ProgressDialog progressDialog;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    String img_name;

    Toolbar toolbar;
    LinearLayout linearLayout;


    EditText msget;
    ImageButton subbtn;

    UserModel usrmdl;


    ChatModel chatModel;


    TextView examtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_exam);


        msget = findViewById(R.id.messageET);
        subbtn = findViewById(R.id.msgsubmit);

        chatModel = new ChatModel();

//        getSupportActionBar().hide();

        model usermodel = (model) getIntent().getSerializableExtra("model");
        String id = img_name = usermodel.getId().trim();
        saved = findViewById(R.id.el_saved);



        toolbar = findViewById(R.id.toolbar);

        favouritechecker(usermodel);


        Toast.makeText(exam_page_layout.this, id, Toast.LENGTH_SHORT).show();


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        linearLayout = findViewById(R.id.text_input);


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        examtitle = (TextView)findViewById(R.id.exam_title);


        imageView = (ImageView)findViewById(R.id.imageView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();


            examtitle.setText(usermodel.getNAME());




            storageReference = FirebaseStorage.getInstance().getReference().child(img_name+".jpg");

            showImage();



        tabLayout = findViewById(R.id.tabbed);
        viewPager = findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabAdapter.addFragment(new frag_1(), "OVERVIEW");
        tabAdapter.addFragment(new frag_2(), "HOW TO APPLY");
        tabAdapter.addFragment(new frag_3(), "DISCUSS");
        viewPager.setAdapter(tabAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int getPos = tab.getPosition();
                if(getPos == 2) {
                    linearLayout.setVisibility(View.VISIBLE);

                    subbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent();
                            intent.putExtra("Docid", id);

                            addMessage(id);
                            Toast.makeText(exam_page_layout.this, msget.getText(), Toast.LENGTH_SHORT).show();
                        }
                    });


                    Toast.makeText(exam_page_layout.this, "Tab 2 selected", Toast.LENGTH_SHORT).show();
                }
                else
                    linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fvrt_Checker = true;
                String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference fvrtref = firebaseDatabase.getReference("favourites");
                DatabaseReference fvrt_listRef = firebaseDatabase.getReference("favouriteList").child(currentUser);

                fvrtref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(fvrt_Checker.equals(true))
                        {
                            if(snapshot.child(usermodel.getId()).hasChild(currentUser)){
                                fvrtref.child(usermodel.getId()).child(currentUser).removeValue();
                                delete(usermodel.getId());
                                fvrt_Checker = false;
                            }
                            else {


                                fvrtref.child(usermodel.getId()).child(currentUser).setValue(true);


                                String idd = fvrt_listRef.push().getKey();
                                fvrt_listRef.child(idd).setValue(usermodel);
                                fvrt_Checker = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }




    private void addMessage(String Docid) {




        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(currentUserId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    String msg = msget.getText().toString().trim();



                    if(!TextUtils.isEmpty(msg))
                    {

                        DocumentSnapshot documentSnapshot = task.getResult();


                        Date today = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
                        String msgId = format.format(today);

                        /*
                        HashMap<String, Object> messageObj = new HashMap<>();
                        messageObj.put("message", msg);
                        messageObj.put("timestamp", FieldValue.serverTimestamp());
                        messageObj.put("username", documentSnapshot.getString("name"));
                        messageObj.put("userId", documentSnapshot.getString("uid"));

                         */

                        chatModel.setMessage(msg);
                        chatModel.setTimestamp(System.currentTimeMillis());
                        chatModel.setUserid(documentSnapshot.getString("uid"));
                        chatModel.setUsername(documentSnapshot.getString("name"));

                        CollectionReference CHAT_REF = firebaseFirestore.collection("exams").document(Docid).collection("CHAT");
                        CHAT_REF.document(msgId).set(chatModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(exam_page_layout.this, "Message sent", Toast.LENGTH_SHORT).show();
                                    msget.setText("");

                                }
                                else
                                {
                                    Toast.makeText(exam_page_layout.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(exam_page_layout.this, "error, snap!", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }



    private void favouritechecker(model userModel) {

        DatabaseReference favouriteRef;

        favouriteRef = firebaseDatabase.getReference("favourites");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        favouriteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(userModel.getId()).hasChild(uid)){
                    saved.setImageResource(R.drawable.ic_baseline_turned_in_24);
                }
                else
                    saved.setImageResource(R.drawable.ic_baseline_turned_in_not_24);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void delete(String id) {
        String  currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference fvrtref = firebaseDatabase.getReference("favourites");
        DatabaseReference fvrt_listRef = firebaseDatabase.getReference("favouriteList").child(currentUser);
        Query query = fvrt_listRef.orderByChild("id").equalTo(id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    dataSnapshot.getRef().removeValue();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }



    public void showImage() {


        try {
            final File localFile = File.createTempFile("img", "jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            if(bitmap==null)
                            {   Toast.makeText(exam_page_layout.this, "failed to load image", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();}
                            else {
                                imageView.setImageBitmap(bitmap);
                                if (progressDialog.isShowing()) ;
                                progressDialog.dismiss();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(progressDialog.isShowing());
                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}